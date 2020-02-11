(ns chess.movement
  (:require [chess.constants :refer :all]
            [chess.utils :refer :all]))

(defn- lies-in-range [pos [min-threshold max-threshold]]
  (every? #(<= min-threshold % max-threshold) pos))

(defn- possible-moves [pos offset]
  (iterate (partial mapv + offset) pos))

(defn possible-move [pos offset]
  (vector (mapv + pos offset)))

(defn empty-place? [board pos] (nil? (board (twod-1d pos))))

(defn ally? [piece board pos]
  (= (:color piece) (-> pos
                        twod-1d
                        board
                        :color)))

;******************************************************************
(defmulti moves-of (fn [piece _] (isa? (:type piece) ::one-jump-piece)))

::one-jump-piece ::continuous-jump-piece

(derive ::king ::one-jump-piece)
(derive ::knight ::one-jump-piece)
(derive ::pawn ::one-jump-piece)

(derive ::queen ::continuous-jump-piece)
(derive ::rook ::continuous-jump-piece)
(derive ::bishop ::continuous-jump-piece)

(defmethod moves-of true [piece displacement]
  (take-while #(lies-in-range % THRESHOLD) (possible-move (:pos piece) displacement)))

(defmethod moves-of false [piece displacement]
  (rest
    (take-while #(lies-in-range % THRESHOLD) (possible-moves (:pos piece) displacement))))

;******************************************************************
(defmulti piece-possible-moves :type)

(defmethod piece-possible-moves ::queen [piece]
  (map (partial moves-of piece) ALL-DISPLACEMENTS))

(defmethod piece-possible-moves ::king [piece]
  (map (partial moves-of piece) ALL-DISPLACEMENTS))

(defmethod piece-possible-moves ::rook [piece]
  (map (partial moves-of piece) ROOK-DISPLACEMENTS))

(defmethod piece-possible-moves ::pawn [piece]
  (let [displacements (cond
                        (= (:color piece) :black) BLACK-PAWN-DISPLACEMENTS
                        (= (:color piece) :white) WHITE-PAWN-DISPLACEMENTS)]
    (map (partial moves-of piece) displacements)))

(defmethod piece-possible-moves ::bishop [piece]
  (map (partial moves-of piece) BISHOP-DISPLACEMENTS))

(defmethod piece-possible-moves ::knight [piece]
  (map (partial moves-of piece) KNIGHT-DISPLACEMENTS))

;******************************************************************

(defn filter-valid [piece board moves]
  (->> moves
       (take-until (partial empty-place? board))
       (remove (partial ally? piece board))))

(defn filter-pawn-moves [board moves]
  (letfn [(pred (partial empty-place? board))]
    (concat
      (filter pred (take-last 1 moves))
      (remove pred (butlast moves)))))

(defn valid-moves [piece board]
  (cond->> (mapcat (partial filter-valid piece board) (piece-possible-moves piece))
           (= (:type piece) ::pawn) (filter-pawn-moves board)))

(ns chess.movement
  (:require '[chess.constants :refer :all]
            '[chess.utils :refer :all]))

(defn- lies-in-range [pos [min-threshold max-threshold]]
  (every? #(<= min-threshold % max-threshold) pos))

(defn- possible-moves [pos offset]
  (iterate (partial mapv + offset) pos))

(defn possible-move [pos offset]
  (vector (mapv + pos offset)))

(defn twod-1d [[x y]]
  (-> y
      (* 8)
      (+ x)))

(defn empty-place? [board pos] (nil? (board (twod-1d pos))))

;******************************************************************
(defmulti moves-of :type)

(defmethod moves-of :king [piece displacement]
  (take-while #(lies-in-range % THRESHOLD) (possible-move (:pos piece) displacement)))

(defmethod moves-of :knight [piece displacement]
  (take-while #(lies-in-range % THRESHOLD) (possible-move (:pos piece) displacement)))

(defmethod moves-of :pawn [piece displacement]
  (take-while #(lies-in-range % THRESHOLD) (possible-move (:pos piece) displacement)))

(defmethod moves-of :default [piece displacement]
  (rest
    (take-while #(lies-in-range % THRESHOLD) (possible-moves (:pos piece) displacement))))

;******************************************************************
(defmulti piece-possible-moves :type)

(defmethod piece-possible-moves [:queen :king] [piece]
  (map (partial moves-of piece) ALL-DISPLACEMENTS))

(defmethod piece-possible-moves :rook [piece]
  (map (partial moves-of piece) ROOK-DISPLACEMENTS))

(defmethod piece-possible-moves :pawn [piece]
  (let [displacements (cond
                        (= (:color piece) :black BLACK-PAWN-DISPLACEMENTS)
                        (= (:color piece) :white) WHITE-PAWN-DISPLACEMENTS)]
    (map (partial moves-of piece) displacements)))

(defmethod piece-possible-moves :bishop [piece]
  (map (partial moves-of piece) BISHOP-DISPLACEMENTS))

(defmethod piece-possible-moves :knight [piece]
  (map (partial moves-of piece) KNIGHT-DISPLACEMENTS))

;******************************************************************
(def create-board (vec (repeat 64 nil)))

(defn filter-valid [possible-moves board]
  (mapcat #(take-while (partial empty-place? board) %) possible-moves))
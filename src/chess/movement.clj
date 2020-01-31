(ns chess.movement)

(def THRESHOLD [0 7])
(def EMPTY nil)

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


(defmulti piece-possible-moves :type)

(defmulti moves-of :type)

(defmethod moves-of :king [piece displacement]
  (take-while #(lies-in-range %1 THRESHOLD) (possible-move (:pos piece) displacement)))

(defmethod moves-of :default [piece displacement]
  (rest
    (take-while #(lies-in-range %1 THRESHOLD) (possible-moves (:pos piece) displacement))))


(defmethod piece-possible-moves [:queen :king] [piece]
  (map (partial moves-of piece) [[0 1]
                                 [0 -1]
                                 [-1 0]
                                 [1 0]
                                 [1 1]
                                 [1 -1]
                                 [-1 1]
                                 [-1 -1]]))

(defmethod piece-possible-moves :rook [piece]
  (map (partial moves-of piece) [[0 1]
                                 [0 -1]
                                 [-1 0]
                                 [1 0]]))

(defmethod piece-possible-moves :bishop [piece]
  (map (partial moves-of piece) [[1 1]
                                 [1 -1]
                                 [-1 1]
                                 [-1 -1]]))

(def create-board (vec (repeat 64 nil)))

(defn filter-valid [possible-moves board]
  (mapcat #(take-while (partial empty-place? board) %) possible-moves))
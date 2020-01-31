(ns chess.movement)

(def THRESHOLD [0 7])
(def EMPTY nil)

(defn- lies-in-range [pos [min-threshold max-threshold]]
  (every? #(<= min-threshold % max-threshold) pos))

(defn- possible-moves [pos offset]
  (iterate (partial mapv + offset) pos))

(defn continuous-possible-moves [pos velocities threshold]
  (mapcat
    (comp rest
          (partial take-while
                   (fn [possible-move] (lies-in-range possible-move threshold)))
          #(possible-moves pos %))
    velocities))

(defn one-possible-moves [pos velocities threshold]
  (filter #(lies-in-range % threshold)
          (map #(second (possible-moves pos %)) velocities)))

(defn north-moves [pos] (possible-moves pos [0 1]))
(defn south-moves [pos] (possible-moves pos [0 -1]))
(defn west-moves [pos] (possible-moves pos [-1 0]))
(defn east-moves [pos] (possible-moves pos [1 0]))
(defn north-east-moves [pos] (possible-moves pos [1 1]))
(defn south-east-moves [pos] (possible-moves pos [1 -1]))
(defn north-west-moves [pos] (possible-moves pos [-1 1]))
(defn south-west-moves [pos] (possible-moves pos [-1 -1]))

(defn empty-place? [place board]
  (-> board
      (get (first place))
      (get (second place))
      (nil?)))

(defn black-pawn-valid [pawn]
  {})

(defn twod-1d [[x y]]
  (-> y
      (* 8)
      (+ x)))

(defn empty? [board pos] (nil? (board (twod-1d pos))))

;(defmulti valid-moves? :pawn [])

(defmulti piece-possible-moves :type)

;(defmethod piece-possible-moves :pawn [pawn board]
;  (filter empty? (one-possible-moves
;                   (:pos pawn)
;                   (:velocities pawn)
;                   THRESHOLD)))

(defn moves-of [piece direction]
  (rest
    (take-while #(lies-in-range %1 THRESHOLD) (direction (:pos piece)))))

(defmethod piece-possible-moves :queen [queen]
  (map (partial moves-of queen) [north-moves
                                 south-moves
                                 west-moves
                                 east-moves
                                 north-east-moves
                                 south-east-moves
                                 north-west-moves
                                 south-west-moves]))

(def create-board (vec (repeat 64 nil)))

(defn filter-valid [possible-moves board]
  (mapcat #(take-while (partial empty? board) %) possible-moves))
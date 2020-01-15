(ns chess.movement)

(defn- lies-in-range [pos [min-threshold max-threshold]]
  (every?
    #(and (< min-threshold %) (> max-threshold %))
    pos))

(defn possible-moves [pos [vel_x vel_y]]
  (iterate
    (fn [[pos_x pos_y]]
      [(+ pos_x vel_x) (+ pos_y vel_y)])
    pos))

(defn continuous-possible-moves [pos velocities threshold]
  (mapcat
    (comp
      rest
      (partial
        take-while
        (fn [possible-move] (lies-in-range possible-move threshold)))
      #(possible-moves pos %))
    velocities))

(defn one-possible-moves [pos velocities threshold]
  (filter
    #(lies-in-range % threshold)
    (map
      #(second (possible-moves pos %))
      velocities)))
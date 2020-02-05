(ns chess.utils)

(defn take-until [pred coll]
  (let [s-coll (split-with pred coll)]
    (concat (first s-coll) (take 1 (second s-coll)))))
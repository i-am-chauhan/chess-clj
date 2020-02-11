(ns chess.utils)

(defn take-until [pred coll]
  (let [s-coll (split-with pred coll)]
    (concat (first s-coll) (take 1 (second s-coll)))))

(defn twod-1d [[x y]]
  (-> y
      (* 8)
      (+ x)))
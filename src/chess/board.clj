(ns chess.board
  (:require [chess.utils :refer :all]))

(def white-soldiers
  [::rook
   ::knight
   ::bishop
   ::queen
   ::king
   ::bishop
   ::knight
   ::rook])

(def black-soldiers
  [::rook
   ::knight
   ::bishop
   ::king
   ::queen
   ::bishop
   ::knight
   ::rook])

(defn piece-data
  ([color pos type]
   (piece-data color pos type false))
  ([color pos type moved?]
   (assoc {} :color color :pos pos :type type :moved? moved?)))

(defn army [color]
  (let [[soldiers y pawn-y] (if (= color :white)
                              [white-soldiers 0 1]
                              [black-soldiers 7 6])]
    (as-> soldiers x
          (keep-indexed #(piece-data color [y %1] %2) x)
          (concat x (map #(piece-data color [pawn-y %] ::pawn) (range 8))))))

(def empty-board (vec (repeat 64 nil)))

(defn place-piece [board piece pos]
  (assoc board (twod-1d pos) piece))

(def create-board
  (reduce #(place-piece %1 %2 (:pos %2))
          empty-board
          (concat (army :white) (army :black))))

(defn symbol [piece]
  (let [type (:type piece)
        white-symbols {::pawn "P" ::king "K" ::queen "Q" ::bishop "B" ::knight "H" ::rook "R"}
        black-symbols {::pawn "p" ::king "k" ::queen "q" ::bishop "b" ::knight "h" ::rook "r"}]
    (if (= :white (:color piece))
      (type white-symbols)
      (type black-symbols))))

(defn print-board [board]
  (->> board
       (map #(if (nil? %) " " (symbol %)))
       (partition 8)
       (map (partial clojure.string/join " | "))
       (clojure.string/join "\n------------------------------\n")
       println))
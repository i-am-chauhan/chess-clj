(ns chess.movement-test
  (:require [clojure.test :refer :all]
            [chess.movement :refer :all]))

(deftest one-possible-moves-test
  (testing "one-possible-moves"
    (testing "should return all valid possible moves given position and velocities within the threshold"
      (are
        (= [[5 5] [3 5] [4 5] [5 4] [4 3] [3 4]]
           (one-possible-moves
             [4 4]
             [[1 1] [-1 1] [-1 -1] [1 -1] [0 1] [1 0] [0 -1] [-1 0]]
             [-1 8]))
        ))))

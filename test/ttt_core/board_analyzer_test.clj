(ns ttt-core.board-analyzer-test
  (:require [clojure.test :refer :all]
            [ttt-core.board-analyzer :refer :all]))

(deftest winning-lines-test
  (testing "it returns winning lines for 3x3 board")
    (is (= [[0 1 2] [3 4 5] [6 7 8] [0 3 6] 
            [1 4 7] [2 5 8] [0 4 8] [2 4 6]]
      (winning-lines 3)))
  (testing "it returns winning lines for 4x4 board")
    (is (= [[0 1 2 3] [4 5 6 7] [8 9 10 11] [12 13 14 15]
            [0 4 8 12] [1 5 9 13] [2 6 10 14] [3 7 11 15]
            [0 5 10 15] [3 6 9 12]]
      (winning-lines 4)))
  (testing "it returns winning lines for 5x5 board")
    (is (= [[0 1 2 3 4] [5 6 7 8 9] [10 11 12 13 14]
            [15 16 17 18 19] [20 21 22 23 24] [0 5 10 15 20]
            [1 6 11 16 21] [2 7 12 17 22] [3 8 13 18 23]
            [4 9 14 19 24] [0 6 12 18 24] [4 8 12 16 20]]
      (winning-lines 5))))

(deftest empty-locations-test
  (testing "it returns a vector of empty locations on the board")
    (is (= [2 5 6 7 8]
      (empty-locations [:x :x nil :o :o nil nil nil nil]))))
(ns artificial-intelligence.random-test
  (:require [clojure.test :refer :all]
            [artificial-intelligence.random :refer :all]))

(deftest choose-move-test
  (testing "it returns a random selection from a provided list of moves"
    (is (not= -1 (.indexOf [2 5 6 7 8] (choose-move [2 5 6 7 8]))))))

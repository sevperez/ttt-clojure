(ns tic-tac-toe-pair.game-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe-pair.game :refer :all]))

(deftest get-winning-token-test
  (testing "returns nil if there is no winner")
    (is (nil? (get-winning-token [nil nil nil :x :o nil nil nil nil])))
  (testing "returns the winning token if there is a winner")
    (is (= :x (get-winning-token [:x :o nil :x :o nil :x nil nil]))))
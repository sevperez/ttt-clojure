(ns tic-tac-toe-pair.board-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe-pair.board :refer :all]))

(deftest is-full?-test
  (testing "returns false if board is not full")
    (is (= false (is-full? [:x :x nil :o :o nil nil nil nil])))
  (testing "returns true if board is full")
    (is (= true (is-full? [:x :x :o :o :o :x :x :o :x]))))

(deftest is-available?-test
  (testing "returns false if space on board is taken")
    (is (= false (is-available? [:x :x nil :o :o nil nil nil nil] 0)))
  (testing "returns true if space on board is available")
    (is (= true (is-available? [:x :x nil :o :o nil nil nil nil] 2))))

(deftest fill-location-test
  (testing "return a board with the provided location filled")
    (is (= [:x :x nil :o :o nil nil nil :x]
      (fill-location [:x :x nil :o :o nil nil nil nil] 8 :x)))
  (testing "return a board with the same values if provided location
            is taken")
    (is (= [:x :x nil :o :o nil nil nil nil] 
      (fill-location [:x :x nil :o :o nil nil nil nil] 0 :o))))

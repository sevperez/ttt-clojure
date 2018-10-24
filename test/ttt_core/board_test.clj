(ns ttt-core.board-test
  (:require [clojure.test :refer :all]
            [ttt-core.board :refer :all]))

(deftest is-full?-test
  (testing "returns false if board is not full"
    (is (= false (is-full? [:x :x nil :o :o nil nil nil nil]))))
  (testing "returns true if board is full"
    (is (= true (is-full? [:x :x :o :o :o :x :x :o :x])))))

(deftest is-location-available?-test
  (testing "returns false if space on board is taken"
    (is (= false
      (is-location-available? [:x :x nil :o :o nil nil nil nil] 0))))
  (testing "returns true if space on board is available"
    (is (= true
      (is-location-available? [:x :x nil :o :o nil nil nil nil] 2)))))

(deftest is-location-valid?-test
  (testing "returns true if the move value is in the range of the board"
    (is (= true
      (is-location-valid? [:x :x nil :o :o nil nil nil nil] 8))))
  (testing "returns false if the move value is not in the range of the board"
    (is (= false
      (is-location-valid? [:x :x nil :o :o nil nil nil nil] 9))))
  (testing "returns false if the move value is not an integer"
    (is (= false
      (is-location-valid? [:x :x nil :o :o nil nil nil nil] "X")))))

(deftest fill-location-test
  (testing "return a board with the provided location filled"
    (is (= [:x :x nil :o :o nil nil nil :x]
      (fill-location [:x :x nil :o :o nil nil nil nil] :x 8)))))
      
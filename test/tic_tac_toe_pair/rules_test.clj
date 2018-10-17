(ns tic-tac-toe-pair.rules-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe-pair.rules :refer :all]
            [tic-tac-toe-pair.game :refer [initialize-game]]))

(deftest is-move-valid?-test
  (testing "returns false if space on board is taken")
    (is (= false
      (is-move-valid? [:x :x nil :o :o nil nil nil nil] 0)))
  (testing "returns true if space on board is available")
    (is (= true
      (is-move-valid? [:x :x nil :o :o nil nil nil nil] 2)))
  (testing "returns true if the move value is in the range of the board")
    (is (= true
      (is-move-valid? [:x :x nil :o :o nil nil nil nil] 8)))
  (testing "returns false if the move value is not in the range of the board")
    (is (= false
      (is-move-valid? [:x :x nil :o :o nil nil nil nil] 9)))
  (testing "returns false if the move value is not an integer")
    (is (= false
      (is-move-valid? [:x :x nil :o :o nil nil nil nil] "X"))))

(deftest get-winning-token-test
  (testing "returns nil if there is no winner")
    (is (nil? (get-winning-token [nil nil nil :x :o nil nil nil nil])))
  (testing "returns the winning token if there is a vertical winner")
    (is (= :x (get-winning-token [:x :o nil :x :o nil :x nil nil])))
  (testing "returns the winning token if there is a horizontal winner")
    (is (= :o (get-winning-token [:o :o :o :x :x nil :x nil nil])))
  (testing "returns the winning token if there is a diagonal winner")
    (is (= :o (get-winning-token [:o :x :o :x :o nil :x nil :o]))))
    
(deftest is-game-over?-test 
  (testing "returns false if the game is not over")
    (is (= false (is-game-over? [nil nil nil nil nil nil nil nil nil])))
  (testing "returns true if the game board is full")
    (is (= true (is-game-over? [:x :x :o :o :o :x :x :o :x])))
  (testing "returns true if the game board has a winner")
    (is (= true (is-game-over? [:x :o nil :x :o nil :x nil nil]))))

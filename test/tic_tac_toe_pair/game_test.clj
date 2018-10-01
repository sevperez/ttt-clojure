(ns tic-tac-toe-pair.game-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe-pair.game :refer :all]))

(deftest get-winning-token-test
  (testing "returns nil if there is no winner")
    (is (nil? (get-winning-token [nil nil nil :x :o nil nil nil nil])))
  (testing "returns the winning token if there is a vertical winner")
    (is (= :x (get-winning-token [:x :o nil :x :o nil :x nil nil])))
  (testing "returns the winning token if there is a horizontal winner")
    (is (= :o (get-winning-token [:o :o :o :x :x nil :x nil nil])))
  (testing "returns the winning token if there is a diagonal winner")
    (is (= :o (get-winning-token [:o :x :o :x :o nil :x nil :o]))))

(deftest initialize-game-test
  (testing "returns a default game map that has a board")
    (is (= {:current-token :player-1-token
            :player-1-token :x
            :player-2-token :o
            :board [nil nil nil nil nil nil nil  nil nil]}
      (initialize-game))))

(deftest update-game-test
  (testing "returns an updated game map if valid move input")
    (is (= {:current-token :player-2-token
            :player-1-token :x
            :player-2-token :o
            :board [nil nil nil nil :x nil nil  nil nil]}
      (update-game (initialize-game) 4)))
  (testing "returns an identical game map if move input is invalid")
    (is (= {:current-token :player-2-token
            :player-1-token :x
            :player-2-token :o
            :board [:x :o nil nil :x nil nil  nil nil]}
      (update-game
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil nil :x nil nil  nil nil]}
         4))))
    
(deftest is-game-over?-test 
  (testing "returns false if the game is not over")
    (is (= false (is-game-over? (initialize-game))))
  (testing "returns true if the game board is full")
    (is (= true (is-game-over?
      {:current-token :player-2-token
       :player-1-token :x
       :player-2-token :o
       :board [:x :x :o :o :o :x :x :o :x]})))
  (testing "returns true if the game board has a winner")
    (is (= true (is-game-over?
      {:current-token :player-2-token
       :player-1-token :x
       :player-2-token :o
       :board [:x :o nil :x :o nil :x nil nil]}))))

(deftest get-game-end-message-test
  (testing "it returns an appropriate message if player 1 wins")
    (is (= "Player 1 Wins!"
      (get-game-end-message
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]})))
  (testing "it returns an appropriate message if player 2 wins")
    (is (= "Player 2 Wins!"
      (get-game-end-message
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [nil :o nil :x :o nil :x :o nil]})))
  (testing "it returns a draw message if there is no winner")
    (is (= "Draw!"
      (get-game-end-message
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :x :o :o :o :x :x :o :x]}))))




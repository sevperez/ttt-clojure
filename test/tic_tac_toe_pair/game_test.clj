(ns tic-tac-toe-pair.game-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe-pair.game :refer :all]))

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

(deftest get-game-end-message-test
  (testing "it returns an appropriate message if player 1 wins")
    (is (= "Congratulations! X won the game!"
      (get-game-end-message
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]})))
  (testing "it returns an appropriate message if player 2 wins")
    (is (= "Congratulations! O won the game!"
      (get-game-end-message
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [nil :o nil :x :o nil :x :o nil]})))
  (testing "it returns a draw message if there is no winner")
    (is (= "This game ended in a tie!"
      (get-game-end-message
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :x :o :o :o :x :x :o :x]}))))

(deftest play-test
  (testing "it returns a game history map of length equal to number moves + 1")
    (with-out-str (is (= 8
      (count (with-in-str "X\n1\n2\n3\n4\n5\n6\n7\n" (play (initialize-game)))))))
  (testing "it returns a game history map with blank game data at the start")
    (with-out-str (is (= (initialize-game)
      (first (with-in-str "X\n1\n2\n3\n4\n5\n6\n7\n" (play (initialize-game)))))))
  (testing "it returns a game history map with the final game data at the end")
    (with-out-str (is (= [:x :o :x :o :x :o :x nil nil]
      (:board (last (with-in-str "X\n1\n2\n3\n4\n5\n6\n7\n" (play (initialize-game))))))))
  (testing "it returns a game history map where the last item is a draw")
    (with-out-str (is (= [:x :x :o :o :o :x :x :o :x]
      (:board (last (with-in-str "X\n1\n3\n2\n4\n6\n5\n7\n8\n9\n" (play (initialize-game)))))))))

(deftest set-player-tokens-test 
  (testing "it sets the game :player-1-token to :x if given :x")
    (is (= :x 
      (:player-1-token (set-player-tokens :x {:current-token :player-2-token
                                              :player-1-token :x
                                              :player-2-token :o
                                              :board [nil nil nil nil nil nil nil nil nil]}))))
  (testing "it sets the game :player-2-token to :o if given :x")
    (is (= :o 
      (:player-2-token (set-player-tokens :x {:current-token :player-2-token
                                              :player-1-token :x
                                              :player-2-token :o
                                              :board [nil nil nil nil nil nil nil nil nil]}))))
  (testing "it sets the game :player-1-token to :o if given :o")
    (is (= :o 
      (:player-1-token (set-player-tokens :o {:current-token :player-2-token
                                              :player-1-token :x
                                              :player-2-token :o
                                              :board [nil nil nil nil nil nil nil nil nil]}))))
  (testing "it sets the game :player-2-token to :x if given :o")
      (is (= :x 
        (:player-2-token (set-player-tokens :o {:current-token :player-2-token
                                                :player-1-token :x
                                                :player-2-token :o
                                                :board [nil nil nil nil nil nil nil nil nil]})))))
(ns tic-tac-toe-pair.console-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe-pair.console :refer :all]))

(deftest draw-board-test
  (testing "it draws an empty board to the console")
    (is (= 
      (str "          |     |     \n"
           "          |     |     \n"
           "          |     |     \n"
           "     -----------------\n"
           "          |     |     \n"
           "          |     |     \n"
           "          |     |     \n"
           "     -----------------\n"
           "          |     |     \n"
           "          |     |     \n"
           "          |     |     \n\n")
      (with-out-str
        (draw-board [nil nil nil nil nil nil nil nil nil]))))
  (testing "it draws a populated board to the console")
    (is (= 
      (str "          |     |     \n"
           "          |  X  |     \n"
           "          |     |     \n"
           "     -----------------\n"
           "          |     |     \n"
           "       X  |     |     \n"
           "          |     |     \n"
           "     -----------------\n"
           "          |     |     \n"
           "          |  O  |     \n"
           "          |     |     \n\n")
      (with-out-str
        (draw-board [nil :x nil :x nil nil nil :o nil])))))

(deftest get-move-location-test
  (testing "it returns an integer that is adjusted to match the zero-indexed board")
    (with-out-str (is 
      (= 4 (with-in-str "5"
        (get-move-location
          {:current-token :player-2-token
            :player-1-token :x
            :player-2-token :o
            :board [:x nil nil nil nil nil nil nil nil]})))))
  (testing "it continues requesting input until it's valid")
    (with-out-str (is 
      (= 4 (with-in-str "20\n5"
        (get-move-location
          {:current-token :player-2-token
            :player-1-token :x
            :player-2-token :o
            :board [:x nil nil nil nil nil nil nil nil]}))))))

(deftest read-move-input-test
  (testing "it returns an an integer if input is valid")
    (is (= 5 
      (with-in-str "6" 
        (get-player-move {:current-token :player-2-token
                          :player-1-token :x
                          :player-2-token :o
                          :board [:x nil nil nil nil nil nil nil nil]}))))
  (testing "it throws a NumberFormatException if input is not integer")
    (is (thrown? NumberFormatException 
      (with-in-str "a" 
        (get-player-move {:current-token :player-2-token
                          :player-1-token :x
                          :player-2-token :o
                          :board [:x nil nil nil nil nil nil nil nil]}))))
  (testing "it throws a custom exception if input is integer but invalid move")
    (is (thrown? clojure.lang.ExceptionInfo
      (with-in-str "1"
        (get-player-move {:current-token :player-2-token
                          :player-1-token :x
                          :player-2-token :o
                          :board [:x nil nil nil nil nil nil nil nil]})))))

(deftest draw-player-info-test 
  (testing "it returns a string with the player names and tokens")
    (is (= "Player 1 (X)     Player 2 (O)\n" 
      (with-out-str (draw-player-info :x :o)))))

(deftest build-current-player-string-test
  (testing "it returns a string with the current player's name and token")
    (is (= "Player 1's move!"
      (build-current-player-string
        {:current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]})))
    (is (= "Player 2's move!"
      (build-current-player-string
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]}))))

(deftest build-choose-move-string-test
  (testing "it returns a string listing all 9 spaces available on empty board")
    (is (= "Choose a move: (1, 2, 3, 4, 5, 6, 7, 8, 9)"
      (build-choose-move-string
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]})))
  (testing "it returns a string listing only available spaces")
    (is (= "Choose a move: (3, 6, 8, 9)"
      (build-choose-move-string
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :x nil :o nil nil]}))))

(deftest build-congratulations-message-test 
  (testing "it returns a string to congratulate the player with the :x token")
    (is (= "Congratulations! X won the game!"
      (build-congratulations-message :x)))
  (testing "it returns a string to congratulate the player with the :o token")
    (is (= "Congratulations! O won the game!"
      (build-congratulations-message :o))))
 
(deftest read-player-mark-input-test
  (testing "it returns :x if player enters X")
    (is (= :x 
      (with-in-str "X" 
        (read-player-mark-input))))
  (testing "it returns :x if player enters X")
    (is (= :x 
      (with-in-str "x" 
          (read-player-mark-input))))
    (testing "it returns :o if player enters O")
    (is (= :o 
      (with-in-str "O" 
        (read-player-mark-input))))
    (testing "it throws a custom exception if input is not a case insensitive X or O")
    (is (thrown? clojure.lang.ExceptionInfo
      (with-in-str "1"
        (read-player-mark-input)))))

(deftest prompt-for-player-mark-test 
  (testing "it returns a default string asking the player to choose the mark they want to use")
  (is (= "Choose your mark on the board: (X or O)" 
    (prompt-for-player-mark))))

(deftest get-player-mark-test 
  (testing "it returns :x if the player enters x")
  (with-out-str (is (= :x 
    (with-in-str "x" 
      (get-player-mark 
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]})))))
  (testing "it keeps asking the player for a mark if the player enters an invalid mark")
  (with-out-str (is (= :x 
    (with-in-str "1\nX" 
      (get-player-mark 
        {:current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}))))))
(ns ttt-core.console-test
  (:require [clojure.test :refer :all]
            [ttt-core.console :refer :all]))

(deftest draw-board-test
  (testing "it draws an empty board to the console"
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
        (draw-board [nil nil nil nil nil nil nil nil nil])))))
  (testing "it draws a populated board to the console"
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
        (draw-board [nil :x nil :x nil nil nil :o nil]))))))

(deftest handle-game-mode-selection-test
  (testing "it returns a game with :game-mode set to :human-vs-human with valid input"
    (with-out-str (is 
      (= {:game-mode :human-vs-human
          :current-token :player-1-token
          :player-1-token :x
          :player-2-token :o
          :board [nil nil nil nil nil nil nil nil nil]}
        (with-in-str "1"
          (handle-game-mode-selection
            {:game-mode nil
             :current-token :player-1-token
             :player-1-token :x
             :player-2-token :o
             :board [nil nil nil nil nil nil nil nil nil]}))))))
  (testing "it returns a game with :game-mode set to :human-vs-computer with valid input"
    (with-out-str (is 
      (= {:game-mode :human-vs-computer
          :current-token :player-1-token
          :player-1-token :x
          :player-2-token :o
          :board [nil nil nil nil nil nil nil nil nil]}
        (with-in-str "2"
          (handle-game-mode-selection
            {:game-mode nil
             :current-token :player-1-token
             :player-1-token :x
             :player-2-token :o
             :board [nil nil nil nil nil nil nil nil nil]})))))))

(deftest get-game-mode-selection-test
  (testing "it returns :human-vs-human if player selects a human vs. human game"
    (with-out-str (is (= :human-vs-human (with-in-str "1" 
      (get-game-mode-selection
        {:game-mode nil
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}))))))
  (testing "it returns :human-vs-computer if player selects a human vs. computer game"
    (with-out-str (is (= :human-vs-computer (with-in-str "2" 
      (get-game-mode-selection
        {:game-mode nil
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}))))))
  (testing "it continues requesting input until it's valid"
    (with-out-str (is (= :human-vs-human (with-in-str "3\na\nX\n1"
      (get-game-mode-selection
        {:game-mode nil
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]})))))))

(deftest build-choose-game-mode-string-test
  (testing "it returns a default choose game mode message"
    (is (= "Choose a game mode:\n1. Human-vs-Human\n2. Human-vs-Computer"
      (build-choose-game-mode-string))))
  (testing "it optionally returns a choose game mode message with a prepended argument string"
    (is (= "Invalid selection. Choose a game mode:\n1. Human-vs-Human\n2. Human-vs-Computer"
      (build-choose-game-mode-string "Invalid selection. ")))))

(deftest handle-player-move-selection-test
  (testing "it returns an integer that is adjusted to match the zero-indexed board"
    (with-out-str (is 
      (= 4 (with-in-str "5"
        (handle-player-move-selection
          {:game-mode :human-vs-human
           :current-token :player-2-token
           :player-1-token :x
           :player-2-token :o
           :board [:x nil nil nil nil nil nil nil nil]}))))))
  (testing "it continues requesting input until it's valid"
    (with-out-str (is 
      (= 4 (with-in-str "20\n5"
        (handle-player-move-selection
          {:game-mode :human-vs-human
           :current-token :player-2-token
           :player-1-token :x
           :player-2-token :o
           :board [:x nil nil nil nil nil nil nil nil]})))))))

(deftest get-player-move-selection-test
  (testing "it returns an an integer if input is valid"
    (is (= 5 
      (with-in-str "6" 
        (get-player-move-selection
          {:game-mode :human-vs-human
           :current-token :player-2-token
           :player-1-token :x
           :player-2-token :o
           :board [:x nil nil nil nil nil nil nil nil]})))))
  (testing "it throws a NumberFormatException if input is not integer"
    (is (thrown? NumberFormatException 
      (with-in-str "a" 
        (get-player-move-selection
          {:game-mode :human-vs-human
           :current-token :player-2-token
           :player-1-token :x
           :player-2-token :o
           :board [:x nil nil nil nil nil nil nil nil]})))))
  (testing "it throws a custom exception if input is integer but invalid move"
    (is (thrown? clojure.lang.ExceptionInfo
      (with-in-str "1"
        (get-player-move-selection
          {:game-mode :human-vs-human
           :current-token :player-2-token
           :player-1-token :x
           :player-2-token :o
           :board [:x nil nil nil nil nil nil nil nil]}))))))

(deftest draw-player-info-test 
  (testing "it returns a string with the human-vs-human player names and tokens"
    (is (= "Player 1 (X)     Player 2 (O)\n\n" 
      (with-out-str (draw-player-info
        {:game-mode :human-vs-human
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]})))))
  (testing "it returns a string with the human-vs-computer player names and tokens"
    (is (= "Player (X)     Computer (O)\n\n" 
      (with-out-str (draw-player-info
        {:game-mode :human-vs-computer
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]})))))
  (testing "it returns a default string if game mode has not yet been set"
    (is (= "******* (X)     ******* (O)\n\n" 
      (with-out-str (draw-player-info
        {:game-mode nil
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]}))))))

(deftest current-player-name-test
  (testing "it returns the correct current player name in a human-vs-human game"
    (is (= "Player 1" (current-player-name :player-1-token :human-vs-human)))
    (is (= "Player 2" (current-player-name :player-2-token :human-vs-human))))
  (testing "it returns the correct current player name in a human-vs-computer game"
    (is (= "Player" (current-player-name :player-1-token :human-vs-computer)))
    (is (= "Computer" (current-player-name :player-2-token :human-vs-computer)))))

(deftest build-current-player-string-test
  (testing "it returns a string with the current player info in human-vs-human"
    (is (= "Player 1's move!" (build-current-player-string "Player 1")))
    (is (= "Player 2's move!" (build-current-player-string "Player 2")))))

(deftest build-choose-move-string-test
  (testing "it returns a string listing all 9 spaces available on empty board"
    (is (= "Choose a move: (1, 2, 3, 4, 5, 6, 7, 8, 9)"
      (build-choose-move-string
        {:game-mode :human-vs-human
         :current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}))))
  (testing "it returns a string listing only available spaces"
    (is (= "Choose a move: (3, 6, 8, 9)"
      (build-choose-move-string
        {:game-mode :human-vs-human
         :current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :x nil :o nil nil]})))))

(deftest build-congratulations-message-test 
  (testing "it returns a string to congratulate the player with the :x token"
    (is (= "Congratulations! X won the game!"
      (build-congratulations-message :x))))
  (testing "it returns a string to congratulate the player with the :o token"
    (is (= "Congratulations! O won the game!"
      (build-congratulations-message :o)))))

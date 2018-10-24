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

(deftest handle-game-setup-test
  (testing "it returns a game with :game-mode set to :human-vs-human with valid input"
    (with-out-str (is 
      (= {:language :en
          :game-mode :human-vs-human
          :current-token :player-1-token
          :player-1-token :x
          :player-2-token :o
          :board [nil nil nil nil nil nil nil nil nil]}
        (with-in-str "1"
          (handle-game-setup
            {:language :en
             :game-mode nil
             :current-token :player-1-token
             :player-1-token :x
             :player-2-token :o
             :board [nil nil nil nil nil nil nil nil nil]}))))))
  (testing "it returns a game with :game-mode set to :human-vs-computer with valid input"
    (with-out-str (is 
      (= {:language :en
          :game-mode :human-vs-computer
          :current-token :player-1-token
          :player-1-token :x
          :player-2-token :o
          :board [nil nil nil nil nil nil nil nil nil]}
        (with-in-str "2"
          (handle-game-setup
            {:language :en
             :game-mode nil
             :current-token :player-1-token
             :player-1-token :x
             :player-2-token :o
             :board [nil nil nil nil nil nil nil nil nil]})))))))

(deftest get-selection-test
  (testing "it returns :human-vs-human for request-game-setup if selected"
    (with-out-str (is (= :human-vs-human (with-in-str "1" 
      (get-selection
        {:language :en
         :game-mode nil
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}
        request-game-setup))))))
  (testing "it returns :human-vs-computer for request-game-setup if selected"
    (with-out-str (is (= :human-vs-computer (with-in-str "2" 
      (get-selection
        {:language :en
         :game-mode nil
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}
        request-game-setup))))))
  (testing "it continues requesting input for request-game-setup until it's valid"
    (with-out-str (is (= :human-vs-human (with-in-str "4\na\nX\n1"
      (get-selection
        {:language :en
         :game-mode nil
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}
        request-game-setup))))))
  (testing "it returns :en for request-language-setup if player selects English"
    (with-out-str (is (= :en (with-in-str "1" 
      (get-selection
        {:language :en
         :game-mode nil
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}
        request-language-setup))))))
  (testing "it returns :pl for request-language-setup if player selects Polish"
    (with-out-str (is (= :pl (with-in-str "2" 
      (get-selection
        {:language :en
         :game-mode nil
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}
        request-language-setup))))))
  (testing "it continues requesting input for request-language-setup until it's valid"
    (with-out-str (is (= :en (with-in-str "4\na\nX\n1"
      (get-selection
        {:language :en
         :game-mode nil
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}
        request-language-setup)))))))

(deftest build-selection-string-test
  (testing "it returns a default choose game setup message"
    (is (= "Choose a game mode:\n1. Human-vs-Human\n2. Human-vs-Computer\n3. (Change game language)"
      (build-selection-string :en :choose-game-setup))))
  (testing "it returns a choose game setup message in :pl if language is set to :pl"
    (is (= "Wybierz tryb gry:\n1. Człowiek kontra człowiek\n2. Człowiek kontra komputer\n3. (Zmień język gry)"
      (build-selection-string :pl :choose-game-setup))))
  (testing "it optionally returns a choose game setup message with a prepended argument string in :en"
    (is (= "Invalid selection. Choose a game mode:\n1. Human-vs-Human\n2. Human-vs-Computer\n3. (Change game language)"
      (build-selection-string :en :choose-game-setup "Invalid selection."))))
  (testing "it optionally returns a choose game setup message with a prepended argument in :pl"
    (is (= "Nieprawidłowy wybór. Wybierz tryb gry:\n1. Człowiek kontra człowiek\n2. Człowiek kontra komputer\n3. (Zmień język gry)"
      (build-selection-string :pl :choose-game-setup "Nieprawidłowy wybór."))))
  (testing "it returns a default choose language message"
    (is (= "Choose a language:\n1. English\n2. Polish"
      (build-selection-string :en :choose-language-setup))))
  (testing "it returns a choose language message in :pl if language is set to :pl"
    (is (= "Wybierz język:\n1. angielski\n2. polski"
      (build-selection-string :pl :choose-language-setup))))
  (testing "it optionally returns a choose language message with a prepended argument string in :en"
    (is (= "Invalid selection. Choose a language:\n1. English\n2. Polish"
      (build-selection-string :en :choose-language-setup "Invalid selection."))))
  (testing "it optionally returns a choose language message with a prepended argument in :pl"
    (is (= "Nieprawidłowy wybór. Wybierz język:\n1. angielski\n2. polski"
      (build-selection-string :pl :choose-language-setup "Nieprawidłowy wybór.")))))

(deftest handle-player-move-selection-test
  (testing "it returns an integer that is adjusted to match the zero-indexed board"
    (with-out-str (is 
      (= 4 (with-in-str "5"
        (handle-player-move-selection
          {:language :en
           :game-mode :human-vs-human
           :current-token :player-2-token
           :player-1-token :x
           :player-2-token :o
           :board [:x nil nil nil nil nil nil nil nil]}))))))
  (testing "it continues requesting input until it's valid"
    (with-out-str (is 
      (= 4 (with-in-str "20\n5"
        (handle-player-move-selection
          {:language :en
           :game-mode :human-vs-human
           :current-token :player-2-token
           :player-1-token :x
           :player-2-token :o
           :board [:x nil nil nil nil nil nil nil nil]})))))))

(deftest get-player-move-selection-test
  (testing "it returns an an integer if input is valid"
    (is (= 5 
      (with-in-str "6" 
        (get-player-move-selection
          {:language :en
           :game-mode :human-vs-human
           :current-token :player-2-token
           :player-1-token :x
           :player-2-token :o
           :board [:x nil nil nil nil nil nil nil nil]})))))
  (testing "it throws a NumberFormatException if input is not integer"
    (is (thrown? NumberFormatException 
      (with-in-str "a" 
        (get-player-move-selection
          {:language :en
           :game-mode :human-vs-human
           :current-token :player-2-token
           :player-1-token :x
           :player-2-token :o
           :board [:x nil nil nil nil nil nil nil nil]})))))
  (testing "it throws a custom exception if input is integer but invalid move"
    (is (thrown? clojure.lang.ExceptionInfo
      (with-in-str "1"
        (get-player-move-selection
          {:language :en
           :game-mode :human-vs-human
           :current-token :player-2-token
           :player-1-token :x
           :player-2-token :o
           :board [:x nil nil nil nil nil nil nil nil]}))))))

(deftest draw-player-info-test 
  (testing "it returns a string with the human-vs-human player names and tokens in :en"
    (is (= "Player 1 (X)     Player 2 (O)\n\n" 
      (with-out-str (draw-player-info
        {:language :en
         :game-mode :human-vs-human
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]})))))
  (testing "it returns a string with the human-vs-human player names and tokens in :pl"
    (is (= "Gracz 1 (X)     Gracz 2 (O)\n\n" 
      (with-out-str (draw-player-info
        {:language :pl
         :game-mode :human-vs-human
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]})))))
  (testing "it returns a string with the human-vs-computer player names and tokens in :en"
    (is (= "Player (X)     Computer (O)\n\n" 
      (with-out-str (draw-player-info
        {:language :en
         :game-mode :human-vs-computer
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]})))))
  (testing "it returns a string with the human-vs-computer player names and tokens in :pl"
    (is (= "Gracz (X)     Komputer (O)\n\n" 
      (with-out-str (draw-player-info
        {:language :pl
         :game-mode :human-vs-computer
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]})))))
  (testing "it returns a default string if game mode has not yet been set"
    (is (= "******* (X)     ******* (O)\n\n" 
      (with-out-str (draw-player-info
        {:language :en
         :game-mode nil
         :current-token :player-1-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]}))))))

(deftest current-player-name-test
  (testing "it returns the correct current player name in a human-vs-human game in :en"
    (is (= "Player 1" (current-player-name :player-1-token :human-vs-human :en)))
    (is (= "Player 2" (current-player-name :player-2-token :human-vs-human :en))))
  (testing "it returns the correct current player name in a human-vs-human game in :pl"
    (is (= "Gracz 1" (current-player-name :player-1-token :human-vs-human :pl)))
    (is (= "Gracz 2" (current-player-name :player-2-token :human-vs-human :pl))))
  (testing "it returns the correct current player name in a human-vs-computer game"
    (is (= "Player" (current-player-name :player-1-token :human-vs-computer :en)))
    (is (= "Computer" (current-player-name :player-2-token :human-vs-computer :en))))
  (testing "it returns the correct current player name in a human-vs-computer game"
    (is (= "Gracz" (current-player-name :player-1-token :human-vs-computer :pl)))
    (is (= "Komputer" (current-player-name :player-2-token :human-vs-computer :pl)))))

(deftest build-current-player-string-test
  (testing "it returns a string with the current player info in human-vs-human in :en"
    (is (= "Player 1's move!" (build-current-player-string "Player 1" :en)))
    (is (= "Player 2's move!" (build-current-player-string "Player 2" :en))))
  (testing "it returns a string with the current player info in human-vs-human in :pl"
    (is (= "Ruch Gracza 1!" (build-current-player-string "Gracz 1" :pl)))
    (is (= "Ruch Gracza 2!" (build-current-player-string "Gracz 2" :pl)))))

(deftest build-choose-move-string-test
  (testing "it returns a string listing all 9 spaces available on empty board in :en"
    (is (= "Choose a move: (1, 2, 3, 4, 5, 6, 7, 8, 9)"
      (build-choose-move-string
        {:language :en
         :game-mode :human-vs-human
         :current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}))))
  (testing "it returns a string listing all 9 spaces available on empty board in :pl"
    (is (= "Wybierz ruch: (1, 2, 3, 4, 5, 6, 7, 8, 9)"
      (build-choose-move-string
        {:language :pl
         :game-mode :human-vs-human
         :current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [nil nil nil nil nil nil nil nil nil]}))))
  (testing "it returns a string listing only available spaces in :en"
    (is (= "Choose a move: (3, 6, 8, 9)"
      (build-choose-move-string
        {:language :en
         :game-mode :human-vs-human
         :current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :x nil :o nil nil]}))))
  (testing "it returns a string listing only available spaces in :pl"
    (is (= "Wybierz ruch: (3, 6, 8, 9)"
      (build-choose-move-string
        {:language :pl
         :game-mode :human-vs-human
         :current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :x nil :o nil nil]})))))

(deftest build-congratulations-message-test 
  (testing "it returns a string to congratulate the player with the :x token in :en"
    (is (= "Congratulations! X won the game!"
      (build-congratulations-message :x :en))))
  (testing "it returns a string to congratulate the player with the :x token in :pl"
    (is (= "Gratulacje! X wygrał!"
      (build-congratulations-message :x :pl))))
  (testing "it returns a string to congratulate the player with the :o token in :en"
    (is (= "Congratulations! O won the game!"
      (build-congratulations-message :o :en))))
  (testing "it returns a string to congratulate the player with the :o token in :pl"
    (is (= "Gratulacje! O wygrał!"
      (build-congratulations-message :o :pl))))
  (testing "it returns a tie string if winner is nil in :en"
    (is (= "This game ended in a tie!"
      (build-congratulations-message nil :en))))
  (testing "it returns a tie string if winner is nil in :pl"
    (is (= "Ta gra zakończyła się remisem!"
      (build-congratulations-message nil :pl)))))

(deftest handle-language-selection-test
  (testing "it returns a game with :language set to :en with valid input"
    (with-out-str (is 
      (= {:language :en
          :game-mode :human-vs-human
          :current-token :player-1-token
          :player-1-token :x
          :player-2-token :o
          :board [nil nil nil nil nil nil nil nil nil]}
        (with-in-str "1"
          (handle-language-selection
            {:language :en
             :game-mode :human-vs-human
             :current-token :player-1-token
             :player-1-token :x
             :player-2-token :o
             :board [nil nil nil nil nil nil nil nil nil]}))))))
  (testing "it returns a game with :language set to :pl with valid input"
    (with-out-str (is 
      (= {:language :pl
          :game-mode :human-vs-human
          :current-token :player-1-token
          :player-1-token :x
          :player-2-token :o
          :board [nil nil nil nil nil nil nil nil nil]}
        (with-in-str "2"
          (handle-language-selection
            {:language :en
             :game-mode :human-vs-human
             :current-token :player-1-token
             :player-1-token :x
             :player-2-token :o
             :board [nil nil nil nil nil nil nil nil nil]})))))))

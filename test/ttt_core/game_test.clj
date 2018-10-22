(ns ttt-core.game-test
  (:require [clojure.test :refer :all]
            [ttt-core.game :refer :all]))

(deftest initialize-game-test
  (testing "returns a default game map that has a board"
    (is (= {:language :en
            :game-mode nil
            :current-token :player-1-token
            :player-1-token :x
            :player-2-token :o
            :board [nil nil nil nil nil nil nil  nil nil]}
      (initialize-game)))))

(deftest update-game-test
  (testing "returns an updated game map if valid move input"
    (is (= {:language :en
            :game-mode :human-vs-human
            :current-token :player-2-token
            :player-1-token :x
            :player-2-token :o
            :board [nil nil nil nil :x nil nil  nil nil]}
      (update-game (assoc (initialize-game) :game-mode :human-vs-human) 4))))
  (testing "returns an identical game map if move input is invalid"
    (is (= {:language :en
            :game-mode :human-vs-human
            :current-token :player-2-token
            :player-1-token :x
            :player-2-token :o
            :board [:x :o nil nil :x nil nil  nil nil]}
      (update-game
        {:language :en
         :game-mode :human-vs-human
         :current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil nil :x nil nil  nil nil]}
         4)))))

(deftest get-game-end-message-test
  (testing "it returns an appropriate message if player 1 wins"
    (is (= "Congratulations! X won the game!"
      (get-game-end-message
        {:language :en
         :game-mode :human-vs-human
         :current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :o nil :x :o nil :x nil nil]}))))
  (testing "it returns an appropriate message if player 2 wins"
    (is (= "Congratulations! O won the game!"
      (get-game-end-message
        {:language :en
         :game-mode :human-vs-human
         :current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [nil :o nil :x :o nil :x :o nil]}))))
  (testing "it returns a draw message if there is no winner"
    (is (= "This game ended in a tie!"
      (get-game-end-message
        {:language :en
         :game-mode :human-vs-human
         :current-token :player-2-token
         :player-1-token :x
         :player-2-token :o
         :board [:x :x :o :o :o :x :x :o :x]})))))

(deftest play-test
  (testing "it returns a game history map of length equal to number moves + 1"
    (with-out-str (is (= 8
      (count (with-in-str "1\n1\n2\n3\n4\n5\n6\n7\n" (play)))))))
  (testing "it returns a game history map with blank game data at the start"
    (with-out-str (is (= (assoc (initialize-game) :game-mode :human-vs-human)
      (first (with-in-str "1\n1\n2\n3\n4\n5\n6\n7\n" (play)))))))
  (testing "it returns a game history map with the final game data at the end"
    (with-out-str (is (= [:x :o :x :o :x :o :x nil nil]
      (:board (last (with-in-str "1\n1\n2\n3\n4\n5\n6\n7\n" (play))))))))
  (testing "it returns a game history map where the last item is a draw"
    (with-out-str (is (= [:x :x :o :o :o :x :x :o :x]
      (:board (last (with-in-str "1\n1\n3\n2\n4\n6\n5\n7\n8\n9\n" (play))))))))
  (testing "it returns a game history map where the last item is a draw in Polish"
    (with-out-str (is (= [:x :x :o :o :o :x :x :o :x]
      (:board (last (with-in-str "3\n2\n1\n1\n3\n2\n4\n6\n5\n7\n8\n9\n" (play)))))))))


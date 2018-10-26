(ns ttt-core.game-test
  (:require [clojure.test :refer :all]
            [ttt-core.game :refer :all]
            [persistence.db :refer [save]]
            [persistence.db-test :refer [save-mock]]
            [clj-time.local :as lt]))

(def default-time "2018-10-24T12:00:00.0Z")

(deftest initialize-game-test
  (testing "returns a default game map that has a board"
    (is (= {:_id "123abc"
            :created-at default-time
            :updated-at default-time
            :language :en
            :game-mode nil
            :player-1-token :x
            :player-2-token :o
            :turns [{:board [nil nil nil nil nil nil nil  nil nil]
                     :current-token :player-1-token}]}
      (initialize-game "123abc" default-time)))))

(deftest update-game-test
  (testing "returns an updated game map if valid move input"
    (is (= {:_id "123abc"
            :created-at default-time
            :updated-at default-time
            :language :en
            :game-mode :human-vs-human
            :player-1-token :x
            :player-2-token :o
            :turns [{:board [nil nil nil nil nil nil nil  nil nil]
                     :current-token :player-1-token}
                    {:board [nil nil nil nil :x nil nil  nil nil]
                     :current-token :player-2-token}]}
      (update-game
        (assoc (initialize-game "123abc" default-time) :game-mode :human-vs-human) 4))))
  (testing "returns an identical game map if move input is invalid"
    (is (= {:_id "123abc"
            :created-at default-time
            :updated-at default-time
            :language :en
            :game-mode :human-vs-human
            :player-1-token :x
            :player-2-token :o
            :turns [{:board [nil nil nil nil nil nil nil  nil nil]
                     :current-token :player-1-token}
                    {:board [:x nil nil nil nil nil nil  nil nil]
                     :current-token :player-2-token}
                    {:board [:x :o nil nil nil nil nil  nil nil]
                     :current-token :player-1-token}
                    {:board [:x :o nil nil :x nil nil  nil nil]
                     :current-token :player-2-token}]}
      (update-game
        {:_id "123abc"
         :created-at default-time
         :updated-at default-time
         :language :en
         :game-mode :human-vs-human
         :player-1-token :x
         :player-2-token :o
         :turns [{:board [nil nil nil nil nil nil nil  nil nil]
                  :current-token :player-1-token}
                 {:board [:x nil nil nil nil nil nil  nil nil]
                  :current-token :player-2-token}
                 {:board [:x :o nil nil nil nil nil  nil nil]
                  :current-token :player-1-token}
                 {:board [:x :o nil nil :x nil nil  nil nil]
                  :current-token :player-2-token}]}
        4)))))

(deftest get-game-end-message-test
  (testing "it returns an appropriate message if player 1 wins"
    (is (= "Congratulations! X won the game!"
      (get-game-end-message
        {:_id "123abc"
         :created-at default-time
         :updated-at default-time
         :language :en
         :game-mode :human-vs-human
         :player-1-token :x
         :player-2-token :o
         :turns [{:board [nil nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [:x nil nil nil nil nil nil nil nil]
                  :current-token :player-2-token}
                 {:board [:x :o nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [:x :o nil :x nil nil nil nil nil]
                  :current-token :player-2-token}
                 {:board [:x :o nil :x :o nil nil nil nil]
                  :current-token :player-2-token}
                 {:board [:x :o nil :x :o nil :x nil nil]
                  :current-token :player-2-token}]}))))
  (testing "it returns an appropriate message if player 2 wins"
    (is (= "Congratulations! O won the game!"
      (get-game-end-message
        {:_id "123abc"
         :created-at default-time
         :updated-at default-time
         :language :en
         :game-mode :human-vs-human
         :player-1-token :x
         :player-2-token :o
         :turns [{:board [nil nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [nil nil nil :x nil nil nil nil nil]
                  :current-token :player-2-token}
                 {:board [nil :o nil :x nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [nil :o nil :x nil nil :x nil nil]
                  :current-token :player-2-token}
                 {:board [nil :o nil :x :o nil :x nil nil]
                  :current-token :player-1-token}
                 {:board [nil :o nil :x :o nil :x nil :x]
                  :current-token :player-2-token}
                 {:board [nil :o nil :x :o nil :x :o :x]
                  :current-token :player-1-token}]}))))
  (testing "it returns a draw message if there is no winner"
    (is (= "This game ended in a tie!"
      (get-game-end-message
        {:_id "123abc"
         :created-at default-time
         :updated-at default-time
         :language :en
         :game-mode :human-vs-human
         :player-1-token :x
         :player-2-token :o
         :board [:x :x :o :o :o :x :x :o :x]
         :turns [{:board [:x nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [:x nil nil nil nil nil nil nil nil]
                  :current-token :player-2-token}
                 {:board [:x nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [:x nil nil nil nil nil nil nil nil]
                  :current-token :player-2-token}
                 {:board [:x nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [:x nil nil nil nil nil nil nil nil]
                  :current-token :player-2-token}
                 {:board [:x nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [:x nil nil nil nil nil nil nil nil]
                  :current-token :player-2-token}
                 {:board [:x nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}]})))))

(deftest play-test
  (with-redefs [save save-mock]
    (testing "it returns a game history with moves vector of length equal to number moves + 1"
      (with-out-str (is (= 8
        (count (:turns (with-in-str "1\n1\n2\n3\n4\n5\n6\n7\n" (play))))))))
    (testing "it returns a game history map with the first move record containing a blank board"
      (with-out-str (is (= [nil nil nil nil nil nil nil nil nil]
        (:board (first (:turns (with-in-str "1\n1\n2\n3\n4\n5\n6\n7\n" (play)))))))))
    (testing "it returns a game history map with the last move record containing a completed game"
      (with-out-str (is (= [:x :o :x :o :x :o :x nil nil]
        (:board (last (:turns (with-in-str "1\n1\n2\n3\n4\n5\n6\n7\n" (play)))))))))
    (testing "it returns a game history map where the last move record is a draw"
      (with-out-str (is (= [:x :x :o :o :o :x :x :o :x]
        (:board (last (:turns (with-in-str "1\n1\n3\n2\n4\n6\n5\n7\n8\n9\n" (play)))))))))
    (testing "it returns a game history map where the last move record is a draw and game is in Polish"
      (with-out-str (is (= [:x :x :o :o :o :x :x :o :x]
        (:board (last (:turns (with-in-str "3\n2\n1\n1\n3\n2\n4\n6\n5\n7\n8\n9\n" (play)))))))))))

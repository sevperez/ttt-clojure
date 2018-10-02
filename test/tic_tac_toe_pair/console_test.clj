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
  (testing "it returns an integer value")
    (with-out-str (is 
      (= 4 (with-in-str "5"
        (get-move-location
          {:current-token :player-2-token
            :player-1-token :x
            :player-2-token :o
            :board [:x :o nil :x :o nil :x nil nil]}))))))

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

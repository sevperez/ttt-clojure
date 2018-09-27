(ns tic-tac-toe-pair.console-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe-pair.console :refer :all]))

(deftest draw-board-test
  (testing "it draws an empty board to the console")
    (is (= 
      (str "     |     |     \n"
           "     |     |     \n"
           "     |     |     \n"
           "-----------------\n"
           "     |     |     \n"
           "     |     |     \n"
           "     |     |     \n"
           "-----------------\n"
           "     |     |     \n"
           "     |     |     \n"
           "     |     |     \n")
      (with-out-str
        (draw-board [nil nil nil nil nil nil nil nil nil]))))
  (testing "it draws a populated board to the console")
    (is (= 
      (str "     |     |     \n"
           "     |  X  |     \n"
           "     |     |     \n"
           "-----------------\n"
           "     |     |     \n"
           "  X  |     |     \n"
           "     |     |     \n"
           "-----------------\n"
           "     |     |     \n"
           "     |  O  |     \n"
           "     |     |     \n")
      (with-out-str
        (draw-board [nil :x nil :x nil nil nil :o nil])))))

(deftest get-move-location-test
  (testing "it returns an integer value")
    (is (= 4 (with-in-str "4" (get-move-location)))))
(ns tic-tac-toe-pair.ai-test
    (:require [clojure.test :refer :all]
              [tic-tac-toe-pair.ai :refer :all]))
  
(deftest evaluate-board-test
  (testing "it returns 100 if given-token wins the game")
  (is (= 100 (evaluate-board [:x :x :x 
                              :o :o nil 
                              nil nil nil] :x)))
  (testing "it returns -100 if a different token than given wins the game")
  (is (= -100 (evaluate-board [:x :o :x 
                               :o :o :x 
                               nil :o nil] :x)))
  (testing "it returns 0 if the board is a draw game")
    (is (= 0 (evaluate-board [:x :o :o
                              :o :x :x 
                              :x :x :o] :x))))

(deftest get-empty-locations-test 
  (testing "it returns an array with the indexes of the nil values on the board")
    (is (= [7 8] (get-empty-locations [:x :o :x :o :o :x :x nil nil]))))


(deftest get-scores-for-empty-board-locations-test 
  (testing "it returns a vector with one map with :location 7 :score of 0")
    (is (= [{:location 7 :score 0}] (get-scores-for-empty-board-locations 
      {:current-token :player-1-token
       :player-1-token :x
       :player-2-token :o
       :board [:x :o :x 
               :o :o :x 
               :x nil :o]} 0 minimax)))
  (testing "it returns a vector with two maps one with :location 7 :score 100 the other one with :location 8 :score 0")
    (is (= [{:location 7 :score 100}, {:location 8 :score 1}] (get-scores-for-empty-board-locations 
      {:current-token :player-2-token
       :player-1-token :x
       :player-2-token :o
       :board [:x :o :x 
               :o :o :x 
               :x nil nil]} 0 minimax)))
  (testing "it returns a vector with two maps one with :location 7 :score 100 the other one with :location 8 :score 0")
  (is (= [{:location 6 :score -99}, {:location 7 :score 100}, {:location 8 :score -2}] (get-scores-for-empty-board-locations 
    {:current-token :player-2-token
    :player-1-token :x
    :player-2-token :o
    :board [:x :o :x 
            :o :o :x 
            nil nil nil]} 0 minimax))))

(deftest get-best-play-location-test
  (testing "it returns the location number on the board with the highest score")(is (= 7 (get-best-play-location {:current-token :player-2-token
    :player-1-token :x
    :player-2-token :o
    :board [:x :o :x 
            :o :o :x 
            nil nil nil]}))))
 
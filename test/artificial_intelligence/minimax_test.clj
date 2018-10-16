(ns artificial-intelligence.minimax-test
  (:require [clojure.test :refer :all]
            [artificial-intelligence.minimax :refer :all]
            [ttt-core.board-evaluator :refer [eval-functions] :as ttt-eval]))

(deftest get-other-player-test
  (testing "it returns the opposite player from the player passed in")
    (is (= :player-2-token
      (get-other-player
        {:game-mode :human-vs-computer
          :current-token :player-2-token
          :player-1-token :o
          :player-2-token :x
          :board [:x :x :x :o :o nil nil nil nil]}
        :player-1-token)))
    (is (= :player-1-token
      (get-other-player
        {:game-mode :human-vs-computer
          :current-token :player-2-token
          :player-1-token :o
          :player-2-token :x
          :board [:x :x :x :o :o nil nil nil nil]}
        :player-2-token))))

(deftest minimax-test
  (testing "it returns max-score if board is leaf and player is winner")
    (is (= max-score 
      (minimax ttt-eval/eval-functions
        {:game-mode :human-vs-computer
         :current-token :player-2-token
         :player-1-token :o
         :player-2-token :x
         :board [:x :x :x :o :o nil nil nil nil]}
        3 :player-2-token min-score max-score)))
  (testing "it returns min-score if board is leaf and opponent is winner")
    (is (= min-score
      (minimax ttt-eval/eval-functions
        {:game-mode :human-vs-computer
         :current-token :player-2-token
         :player-1-token :o
         :player-2-token :x
         :board [:x :x nil :o :o :o nil nil nil]}
        3 :player-2-token min-score max-score)))
  (testing "it returns heuristic score if board is not leaf and depth is 0")
    (is (= 40
      (minimax ttt-eval/eval-functions
        {:game-mode :human-vs-computer
         :current-token :player-2-token
         :player-1-token :o
         :player-2-token :x
         :board [:x nil :x :o nil nil :x :o nil]}
         0 :player-2-token min-score max-score)))
  (testing "it returns max-score if guaranteed win")
    (is (= max-score
      (minimax ttt-eval/eval-functions
        {:game-mode :human-vs-computer
         :current-token :player-2-token
         :player-1-token :o
         :player-2-token :x
         :board [:x :x nil :o :o nil nil nil nil]}
        3 :player-2-token min-score max-score)))
  (testing "it returns min-score if guaranteed loss")
    (is (= min-score
      (minimax ttt-eval/eval-functions
        {:game-mode :human-vs-computer
         :current-token :player-1-token
         :player-1-token :o
         :player-2-token :x
         :board [:x :x nil :o :o nil nil nil nil]}
        3 :player-2-token min-score max-score)))
  (testing "it returns zero if guaranteed draw")
    (is (= 0
      (minimax ttt-eval/eval-functions
        {:game-mode :human-vs-computer
         :current-token :player-2-token
         :player-1-token :o
         :player-2-token :x
         :board [:o :o nil :x :x :o :o :x :x]}
        3 :player-2-token min-score max-score))))

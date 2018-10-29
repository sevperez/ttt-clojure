(ns artificial-intelligence.minimax-test
  (:require [clojure.test :refer :all]
            [artificial-intelligence.minimax :refer :all]
            [ttt-core.board-evaluator :refer [eval-functions] :as ttt-eval]))

(deftest minimax-test
  (testing "it returns max-score if board is leaf and player is winner"
    (is (= max-score 
      (minimax ttt-eval/eval-functions
        {:language :en
         :game-mode :human-vs-computer
         :player-1-token :o
         :player-2-token :x
         :turns [{:board [nil nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [:x :x :x :o :o nil nil nil nil]
                  :current-token :player-2-token}]}
        :player-2-token))))
  (testing "it returns min-score if board is leaf and opponent is winner"
    (is (= min-score
      (minimax ttt-eval/eval-functions
        {:language :en
         :game-mode :human-vs-computer
         :player-1-token :o
         :player-2-token :x
         :turns [{:board [nil nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [:x :x nil :o :o :o nil nil nil]
                  :current-token :player-2-token}]}
        :player-2-token))))
  (testing "it returns heuristic score if board is not leaf and depth is 0"
    (is (= 40
      (minimax ttt-eval/eval-functions
        {:language :en
         :game-mode :human-vs-computer
         :player-1-token :o
         :player-2-token :x
         :turns [{:board [nil nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [:x nil :x :o nil nil :x :o nil]
                  :current-token :player-2-token}]}
        :player-2-token 0))))
  (testing "it returns max-score if guaranteed win"
    (is (= max-score
      (minimax ttt-eval/eval-functions
        {:language :en
         :game-mode :human-vs-computer
         :player-1-token :o
         :player-2-token :x
         :turns [{:board [nil nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [:x :x nil 
                          :o :o nil 
                          nil nil nil]
                  :current-token :player-2-token}]}
        :player-2-token))))
  (testing "it returns min-score if guaranteed loss"
    (is (= min-score
      (minimax ttt-eval/eval-functions
        {:language :en
         :game-mode :human-vs-computer
         :player-1-token :o
         :player-2-token :x
         :turns [{:board [nil nil nil nil nil nil nil nil nil]
                  :current-token :player-2-token}
                 {:board [:x :x nil :o :o nil nil nil nil]
                  :current-token :player-1-token}]}
        :player-2-token))))
  (testing "it returns zero if guaranteed draw"
    (is (= 0
      (minimax ttt-eval/eval-functions
        {:language :en
         :game-mode :human-vs-computer
         :player-1-token :o
         :player-2-token :x
         :turns [{:board [nil nil nil nil nil nil nil nil nil]
                  :current-token :player-1-token}
                 {:board [:o :o nil :x :x :o :o :x :x]
                  :current-token :player-2-token}]}
        :player-2-token)))))

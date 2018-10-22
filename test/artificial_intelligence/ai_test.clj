(ns artificial-intelligence.ai-test
  (:require [clojure.test :refer :all]
            [artificial-intelligence.ai :refer :all]
            [artificial-intelligence.minimax :refer [minimax] :as mm]
            [ttt-core.board-evaluator :refer [eval-functions] :as ttt-eval]))

(deftest get-other-player-test
  (testing "it returns the opposite player from the player passed in"
    (is (= :player-2-token (get-other-player :player-1-token)))
    (is (= :player-1-token (get-other-player :player-2-token)))))

(deftest all-move-options-test
  (testing "it returns a list of available move options with locations and scores"
    (is (= [{:location 2 :score 1000}
            {:location 5 :score -1000}
            {:location 8 :score -1000}]
      (all-move-options ttt-eval/eval-functions mm/minimax
        {:language :en
         :game-mode :human-vs-computer
         :current-token :player-2-token
         :player-1-token :o
         :player-2-token :x
         :board [:x :x nil :x :o nil :o :o nil]}
        :player-2-token)))))

(deftest top-move-options-test
  (testing "it returns a list of the best available move locations"
    (is (= [2 3 8]
      (top-move-options
        [{:location 2 :score 1000}
         {:location 3 :score 1000}
         {:location 4 :score 40}
         {:location 5 :score 0}
         {:location 6 :score -1000}
         {:location 8 :score 1000}])))))

(deftest choose-move-test
  (testing "it returns index of a winning move if available"
    (is (= 2
      (choose-move ttt-eval/eval-functions mm/minimax
        {:language :en
         :game-mode :human-vs-computer
         :current-token :player-2-token
         :player-1-token :o
         :player-2-token :x
         :board [:x :x nil :o :o nil :o :x :o]}
        :player-2-token))))
  (testing "it returns index of a blocking move if loss imminent"
    (is (= 8
      (choose-move ttt-eval/eval-functions mm/minimax
        {:language :en
         :game-mode :human-vs-computer
         :current-token :player-2-token
         :player-1-token :o
         :player-2-token :x
         :board [nil nil :o nil :x :o :x nil nil]}
        :player-2-token))))
  (testing "it preferentially returns a winning move over a blocking move"
    (is (= 2
      (choose-move ttt-eval/eval-functions mm/minimax
        {:language :en
         :game-mode :human-vs-computer
         :current-token :player-2-token
         :player-1-token :o
         :player-2-token :x
         :board [:x :x nil :o :o nil nil nil nil]}
        :player-2-token)))))
(ns ttt-core.board-evaluator-test
  (:require [clojure.test :refer :all]
            [ttt-core.board-evaluator :refer :all]))

(deftest terminal-score-test
  (testing "returns the provided max-score if test-token is the winner"
    (is (= 1000
      (terminal-score [:x :x :x :o :o nil nil nil nil] :x :o -1000 1000))))
  (testing "returns the provided min-score if other-token is the winner"
    (is (= -1000
      (terminal-score [:x :x nil :o :o :o nil nil nil] :x :o -1000 1000))))
  (testing "returns zero if neither test-token nor other-token is the winner"
    (is (= 0
      (terminal-score [:x :x nil :o :o :nil nil nil nil] :x :o -1000 1000)))))

(deftest heuristic-score-test
  (testing "returns a heuristic score based on a superior test-token board"
    (is (= 40 (heuristic-score [:x nil :x :o nil nil :x :o nil] :x :o))))
  (testing "returns a heuristic score based on a superior other-token board"
    (is (= -30 (heuristic-score [:x :o :x :o :o nil nil nil nil] :x :o))))
  (testing "returns a heuristic score based on a neutral board"
    (is (= 0 (heuristic-score [:x :x nil :o :o nil :x :o nil] :x :o))))
  (testing "returns a heuristic score based on an empty board"
    (is (= 0 (heuristic-score [nil nil nil nil nil nil nil nil nil] :x :o)))))
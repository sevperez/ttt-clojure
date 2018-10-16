(ns ttt-core.board-evaluator
  (:require [ttt-core.board-analyzer :refer
              [winning-lines calculate-board-size possible-board-states empty-locations]]
            [ttt-core.rules :refer [is-game-over? get-winning-token]]))

(defn terminal-score [board test-token other-token min max]
  (let [winning-token (get-winning-token board)]
    (cond
      (= winning-token test-token) max
      (= winning-token other-token) min
      :else 0)))

(defn- can-win-line? [line token]
  (every? (fn [item] (or (= item token) (nil? item))) line))

(defn- tokens-in-line [board winning-line]
  (map (fn [idx] (get board idx)) winning-line))

(defn- num-tokens-in-line [line token]
  (if-let [count (get (frequencies line) token)] count 0))

(defn- line-score [board winning-line test-token other-token]
  (let [current-line (tokens-in-line board winning-line)]
    (cond
      (can-win-line? current-line test-token)
        (* 10 (num-tokens-in-line current-line test-token))
      (can-win-line? current-line other-token)
        (* -10 (num-tokens-in-line current-line other-token))
      :else 0)))

(defn heuristic-score [board test-token other-token]
  (let [board-size  (calculate-board-size board)
        wlines      (winning-lines board-size)
        num-lines   (count wlines)]
    (loop [idx    0
           score  0]
      (if (>= idx num-lines)
        score
        (recur
          (inc idx)
          (+ score
            (line-score board (get wlines idx) test-token other-token)))))))

(def eval-functions
  {:terminal-score  terminal-score
   :heuristic-score heuristic-score
   :is-leaf?  is-game-over?
   :possible-board-states possible-board-states
   :empty-locations empty-locations})

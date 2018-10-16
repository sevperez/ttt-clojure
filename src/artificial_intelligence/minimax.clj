(ns artificial-intelligence.minimax
  (:require [artificial-intelligence.ai :refer [get-other-player switch-current-token]]))

(declare minimax)

;; constants
(def max-depth 3)
(def max-score 1000)
(def min-score (- 0 max-score))

(defn- player-move? [player-token next-token] (= player-token next-token))

(defn- minimax-end? [leaf-fn board depth] (or (leaf-fn board) (= 0 depth)))

(defn- minimax-end-score [eval-fns board depth test-token other-token]
  (if ((:is-leaf? eval-fns) board)
    ((:terminal-score eval-fns) board test-token other-token min-score max-score)
    (if (= 0 depth)
      ((:heuristic-score eval-fns) board test-token other-token)
      0)))

(defn- maximize [eval-fns game player depth alpha beta]
  (let [board      (:board game)
        next-token ((:current-token game) game)
        boards     ((:possible-board-states eval-fns) board next-token)]
    (loop [idx    0
           best   min-score
           alpha  alpha]
      (if (or (>= idx (count boards)) (<= beta alpha))
        best
        (let [next-board    (get boards idx)
              next-player   (switch-current-token game)
              updated-game  (assoc game :board next-board :current-token next-player)
              score         (minimax eval-fns updated-game player (dec depth) alpha beta)]
          (recur
            (inc idx)
            (max best score)
            (max alpha best score)))))))

(defn- minimize [eval-fns game player depth alpha beta]
  (let [board      (:board game)
        next-token ((:current-token game) game)
        boards     ((:possible-board-states eval-fns) board next-token)]
    (loop [idx  0
           best max-score
           beta beta]
      (if (or (>= idx (count boards)) (<= beta alpha))
        best
        (let [next-board    (get boards idx)
              next-player   (switch-current-token game)
              updated-game  (assoc game :board next-board :current-token next-player)
              score         (minimax eval-fns updated-game player (dec depth) alpha beta)]
          (recur
            (inc idx)
            (min best score)
            (min beta best score)))))))

(defn minimize-maximize [eval-fns game player depth alpha beta]
  (let [next-token  ((:current-token game) game)]
    (if (player-move? (player game) next-token)
      (maximize eval-fns game player depth alpha beta)
      (minimize eval-fns game player depth alpha beta))))

(defn minimax
  ([eval-fns game player]
    (minimax eval-fns game player max-depth min-score max-score))
  ([eval-fns game player depth]
    (minimax eval-fns game player depth min-score max-score))
  ([eval-fns game player depth alpha beta]
    (let [board         (:board game)
          test-token    (player game)
          other-player  (get-other-player player)
          other-token   (other-player game)]
      (if (minimax-end? (:is-leaf? eval-fns) board depth)
        (minimax-end-score eval-fns board depth test-token other-token)
        (minimize-maximize eval-fns game player depth alpha beta)))))

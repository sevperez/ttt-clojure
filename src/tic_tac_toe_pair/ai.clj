(ns tic-tac-toe-pair.ai
  (:require [tic-tac-toe-pair.rules :refer [get-winning-token is-game-over?]]))

(def win-score 100)
(def loose-score -100)
(def draw-score 0)

(defn evaluate-board [board token]
    (cond
      (nil? (get-winning-token board)) draw-score
      (= token (get-winning-token board)) win-score
      (not (token (get-winning-token board)))loose-score))

(defn get-board-score [board depth token is-maximizing] 
(if is-maximizing
    (- (evaluate-board board token) depth)
    (+ (evaluate-board board token) depth)))

(defn get-empty-locations [board]
  (loop [index 0
         result []]
    (if (>= index (count board))
       result
       (recur (inc index) (if (nil? (board index)) (conj result index) result)))))

(defn low-score [score1 score2] 
  (if (< (:score score1) (:score score2))
    score1
    score2))

(defn high-score [score1 score2] 
  (if (> (:score score1) (:score score2))
    score1
    score2))

(defn get-high-score [scores] 
  (reduce high-score scores))

(defn get-low-score [scores]
  (reduce low-score scores))

(defn get-best-score [scores maximizing] 
  (if maximizing (get-high-score scores) (get-low-score scores)))

(defn make-score-map [score location]
    {:score score :location location})

(defn- is-maximizing [game] 
    (= :player-2-token (:current-token game)))

(defn get-scores-for-empty-board-locations [game depth scoring-function] 
    (let [ board (:board game)
           empty-locations (get-empty-locations (:board game))
           maximizing (is-maximizing game)
           player-token (:player-1-token game)
           computer-token (:player-2-token game)]
        (map (fn [location] 
            (make-score-map (scoring-function board location player-token computer-token depth maximizing) location)) empty-locations)))

(defn- set-new-board [board location token] 
    (assoc board location token))

(defn minimax [board location player-token computer-token depth maximizing]
  (let  [board board
        new-board (set-new-board board location (if maximizing computer-token player-token))
        empty-locations (get-empty-locations new-board)]
    (if (is-game-over? new-board)
      (get-board-score new-board depth computer-token maximizing)
      (let [
        scores (map
        (fn [location] (minimax new-board location player-token computer-token    (inc depth) (not maximizing))) empty-locations)
        ]
        (if (not maximizing)
          (apply max scores) 
          (apply min scores)))
        )))

(defn get-best-play-location [game] 
  (:location (get-best-score (get-scores-for-empty-board-locations game 0 minimax) true))) 
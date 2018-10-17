(ns tic-tac-toe-pair.game
  (:require [tic-tac-toe-pair.rules :refer [get-winning-token is-game-over? is-move-valid?]]
            [tic-tac-toe-pair.board :refer [fill-location]]
            [tic-tac-toe-pair.console :refer
              [draw-intro draw-main get-move-location get-player-mark keyword-to-token build-congratulations-message]]))

(defn initialize-game [] 
  {:current-token :player-1-token
   :player-1-token :x
   :player-2-token :o
   :board [nil nil nil nil nil nil nil  nil nil]})

(defn- update-current-player [game]
  (assoc game :current-token
    (if (= (:current-token game) :player-1-token)
      :player-2-token
      :player-1-token)))

(defn- get-current-token [game] ((:current-token game) game))

(defn- update-board [game location]
  (assoc game :board (fill-location (:board game) location (get-current-token game))))

(defn- update-board-and-player [game location]
  ((comp update-current-player update-board) game location))

(defn update-game [game location]
  (if (is-move-valid? (:board game) location)
    (update-board-and-player game location)
    game))

(defn get-game-end-message [game]
  (let [winner (get-winning-token (:board game))]
    (if winner
      (build-congratulations-message winner)
      "This game ended in a tie!")))

(defn set-player-tokens [player-1-token game]
  (assoc game 
    :player-1-token player-1-token
    :player-2-token (if (= :x player-1-token) :o :x)))
  
(defn play
  ([game] (loop [history  [(set-player-tokens (get-player-mark game) game)]]
  (let [game (last history)]
    (if (is-game-over? game)
      (do 
        (draw-main game (get-game-end-message game))
        history)
        (let [new-game (update-game game (get-move-location game))]
          (recur (conj history new-game))))))))
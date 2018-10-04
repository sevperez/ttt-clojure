(ns tic-tac-toe-pair.game
  (:require [tic-tac-toe-pair.rules :refer :all]
            [tic-tac-toe-pair.board :refer :all]
            [tic-tac-toe-pair.console :refer :all])
  (:gen-class))

(defn initialize-game [] 
  {:current-token :player-1-token
   :player-1-token :x
   :player-2-token :o
   :board [nil nil nil nil nil nil nil  nil nil]})

(defn update-current-player [game]
  (assoc game :current-token
    (if (= (:current-token game) :player-1-token)
      :player-2-token
      :player-1-token)))

(defn get-current-token [game] ((:current-token game) game))

(defn update-board [game location]
  (assoc game :board (fill-location (:board game) location (get-current-token game))))

(defn update-board-and-player [game location]
  ((comp update-current-player update-board) game location))

(defn update-game [game location]
  (if (is-move-valid? (:board game) location)
    (update-board-and-player game location)
    game))

(defn build-congratulations-message [game token]
  (str "Congratulations! " (keyword-to-token (token game)) " won the game!"))

(defn get-game-end-message [game]
  (let [winner (get-winning-token (:board game))]
    (cond 
      (= (:player-1-token game) winner)
        (build-congratulations-message game :player-1-token)
      (= (:player-2-token game) winner)
        (build-congratulations-message game :player-2-token)
      :else
        "This game ended in a tie!")))

(defn play [game]
  (loop [game     game
         history  [game]]
    (if (is-game-over? game)
      (do 
        (draw-main game (get-game-end-message game))
        history)
      (let [new-game (update-game game (get-move-location game))]
        (recur new-game (conj history new-game))))))

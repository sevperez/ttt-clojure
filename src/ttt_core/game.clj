(ns ttt-core.game
  (:require [ttt-core.rules :refer [get-winning-token is-game-over? is-move-valid?]]
            [ttt-core.board :refer [fill-location]]
            [ttt-core.board-analyzer :refer [empty-locations]]
            [ttt-core.board-evaluator :refer [eval-functions]]
            [ttt-core.console :refer
              [draw-main handle-player-move-selection keyword-to-token
               build-congratulations-message handle-game-setup]]
            [artificial-intelligence.ai :refer [choose-move] :as ai]
            [artificial-intelligence.minimax :refer [minimax-memo] :as mm]))

(defn initialize-game [] 
  {:language :en
   :game-mode nil
   :current-token :player-1-token
   :player-1-token :x
   :player-2-token :o
   :board [nil nil nil nil nil nil nil  nil nil]})

(defn- update-current-player [game]
  (assoc game :current-token
    (if (= (:current-token game) :player-1-token) :player-2-token :player-1-token)))

(defn- get-current-token [game] ((:current-token game) game))

(defn- update-board [game location]
  (assoc game :board (fill-location (:board game) (get-current-token game) location)))

(defn- update-board-and-player [game location]
  ((comp update-current-player update-board) game location))

(defn update-game [game location]
  (if (is-move-valid? (:board game) location)
    (update-board-and-player game location)
    game))

(defn get-game-end-message [game]
  (let [winner (get-winning-token (:board game))]
    (build-congratulations-message winner (:language game))))

(defn- ai-move [game]
  (ai/choose-move eval-functions mm/minimax-memo game :player-2-token))

(defn get-next-move [game]
  (let [mode          (:game-mode game)
        current-token (:current-token game)]
    (if (or (= :player-1-token current-token) (= :human-vs-human (:game-mode game)))
      (handle-player-move-selection game)
      (ai-move game))))

(defn play []
  (loop [game     (handle-game-setup (initialize-game))
         history  [game]]
    (if (is-game-over? (:board game))
      (do 
        (draw-main game (get-game-end-message game))
        history)
      (let [new-game (update-game game (get-next-move game))]
        (recur new-game (conj history new-game))))))

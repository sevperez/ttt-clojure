(ns ttt-core.game
  (:require [ttt-core.rules :refer [get-winning-token is-game-over? is-move-valid?]]
            [ttt-core.board :refer [fill-location]]
            [ttt-core.board-analyzer :refer [empty-locations]]
            [ttt-core.board-evaluator :refer [eval-functions]]
            [ttt-core.console :refer
              [draw-main handle-player-move-selection keyword-to-token
               build-congratulations-message handle-game-setup]]
            [artificial-intelligence.ai :refer [choose-move] :as ai]
            [artificial-intelligence.minimax :refer [minimax-memo] :as mm]
            [persistence.db :refer [save generate-uuid] :as db]
            [clj-time.local :as lt]
            [clj-time.format :as f]))

(defn initialize-game
  ([] (initialize-game (generate-uuid) (f/unparse (f/formatters :date-time) (lt/local-now))))
  ([id timestamp] 
    {:_id id
     :created-at timestamp
     :updated-at timestamp
     :language :en
     :game-mode nil
     :player-1-token :x
     :player-2-token :o
     :turns [{:board [nil nil nil nil nil nil nil  nil nil]
              :current-token :player-1-token}]}))

(defn- update-current-player [game]
  (if (= (:current-token (last (:turns game))) :player-1-token) :player-2-token :player-1-token))

(defn- get-current-token [game] ((:current-token (last (:turns game))) game))

(defn- update-board [board token location] (fill-location board token location))

(defn- add-turn [game location]
  (let [last-turn (last (:turns game))
        new-board (update-board (:board last-turn) (get-current-token game) location)
        new-token (update-current-player game)
        new-turns (conj (:turns game) {:board new-board :current-token new-token})]
    (assoc game :turns new-turns)))

(defn update-game [game location]
  (if (is-move-valid? (:board (last (:turns game))) location)
    (add-turn game location)
    game))

(defn get-game-end-message [game]
  (let [winner (get-winning-token (:board (last (:turns game))))]
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
  (loop [game (handle-game-setup (initialize-game))]
    (do
      (save game (or (:_id game) (generate-uuid)))
      (if (is-game-over? (:board game))
        (do 
          (draw-main game (get-game-end-message game))
          game)
        (recur (update-game game (get-next-move game)))))))

(ns ttt-core.console 
  (:require [clojure.string :as s]
            [ttt-core.rules :refer [is-move-valid?]]))

(def board-shape 
  (str "          |     |     \n"
       "       0  |  1  |  2  \n"
       "          |     |     \n"
       "     -----------------\n"
       "          |     |     \n"
       "       3  |  4  |  5  \n"
       "          |     |     \n"
       "     -----------------\n"
       "          |     |     \n"
       "       6  |  7  |  8  \n"
       "          |     |     \n"))

(defn keyword-to-token [keyword] (s/upper-case (name keyword)))

(defn- set-square-content [board square]
  (let [token (get board square)]
    (if (nil? token)
      " "
      (keyword-to-token token))))

(defn- replace-square-position [board-string board square-pos]
  (s/replace board-string (str square-pos) (set-square-content board square-pos)))

(defn- build-board [board]
  (loop [square-pos     0
         filled-board   board-shape]
    (if (>= square-pos (count board))
      filled-board
      (recur (inc square-pos) (replace-square-position filled-board board square-pos)))))

(defn draw-board [board] 
  (println (build-board board)))

(defn- clear-terminal []
  (do (print "\u001b[2J") (print "\u001B[0;0f")))

(defn- draw-header []
  (println "---------------------------\nTic Tac Toe\n---------------------------"))

(defn draw-player-info [game]
  (let [mode    (:game-mode game)
        token-1 (keyword-to-token (:player-1-token game))
        token-2 (keyword-to-token (:player-2-token game))]
    (cond
      (= :human-vs-human mode)
        (println (str "Player 1 (" token-1 ")     Player 2 (" token-2 ")\n"))
      (= :human-vs-computer mode)
        (println (str "Player (" token-1 ")     Computer (" token-2 ")\n"))
      :else (println "******* (X)     ******* (O)\n"))))

(defn- draw-footer []
  (println "---------------------------\n"))

(defn draw-main [game message]
  (do
    (clear-terminal)
    (draw-header)
    (draw-player-info game)
    (draw-board (:board game))
    (println message)
    (draw-footer)))

(defn build-choose-game-mode-string 
  ([] (build-choose-game-mode-string ""))
  ([message]
    (str message "Choose a game mode:\n1. Human-vs-Human\n2. Human-vs-Computer")))

(defn request-game-mode
  ([game] (request-game-mode game ""))
  ([game message]
    (let [modes {:1 :human-vs-human :2 :human-vs-computer}]
      (do
        (draw-main game "--- Game Mode Selection ---")
        (println (build-choose-game-mode-string message))
        (get modes (keyword (read-line)))))))

(defn get-game-mode-selection [game] 
  (loop [selection (request-game-mode game)]
    (if (not (nil? selection))
      selection
      (recur (request-game-mode game "Invalid selection. ")))))

(defn handle-game-mode-selection [game]
  (assoc game :game-mode (get-game-mode-selection game)))

(defn build-current-player-string [game]
  (let [mode          (:game-mode game)
        player-1-name (if (= :human-vs-human mode) "Player 1" "Player")
        player-2-name (if (= :human-vs-human mode) "Player 2" "Computer")]
    (if (= :player-1-token (:current-token game))
      (str player-1-name "'s move!")
      (str player-2-name "'s move!"))))

(defn- get-index-adjusted-input [] (dec (Integer/parseInt (read-line))))

(defn get-player-move-selection [game] 
  (let [input (get-index-adjusted-input)]
    (if (is-move-valid? (:board game) input) 
      input
      (throw (ex-info "You've entered an invalid move." {})))))

(defn- get-available-indices [board]
  (loop [index  0
         result []]
    (if (>= index (count board))
      result
      (if (nil? (get board index))
        (recur (inc index) (conj result (inc index)))
        (recur (inc index) result)))))

(defn build-choose-move-string [game]
  (str "Choose a move: (" (s/join ", " (get-available-indices (:board game))) ")"))

(defn handle-player-move-selection
  ([game]
    (handle-player-move-selection game (build-choose-move-string game)))
  ([game message]
    (do
      (draw-main game (build-current-player-string game))
      (println message))
    (try 
      (get-player-move-selection game)
      (catch NumberFormatException e 
        (handle-player-move-selection game (str "Invalid entry. " (build-choose-move-string game))))
      (catch clojure.lang.ExceptionInfo e
        (handle-player-move-selection game (str "Unavailable entry. " (build-choose-move-string game)))))))

(defn build-congratulations-message [token]
  (str "Congratulations! " (keyword-to-token token) " won the game!"))
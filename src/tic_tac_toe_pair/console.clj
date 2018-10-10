(ns tic-tac-toe-pair.console 
  (:require [clojure.string :as s]
            [tic-tac-toe-pair.rules :refer [is-move-valid?]]))

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

(defn draw-player-info [player-1-token player-2-token]
  (println (str
    "Player 1 (" (keyword-to-token player-1-token) ")"
    "     "
    "Player 2 (" (keyword-to-token player-2-token) ")")))

(defn- draw-footer []
  (println "---------------------------\n"))

(defn draw-intro [game]
  (do 
    (clear-terminal)
    (draw-header)
    (println "\n")
    (draw-board (:board game))
    (draw-footer)))

(defn draw-main [game message]
  (do
    (clear-terminal)
    (draw-header)
    (draw-player-info (:player-1-token game) (:player-2-token game))
    (draw-board (:board game))
    (println message)
    (draw-footer)))

(defn build-current-player-string [game] 
  (if (= :player-1-token (:current-token game))
    "Player 1's move!"
    "Player 2's move!"))

(defn- get-index-adjusted-input [] (dec (Integer/parseInt (read-line))))

(defn get-player-move [game] 
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

(defn get-move-location
  ([game]
    (get-move-location game (build-choose-move-string game)))
  ([game message]
    (do
      (draw-main game (build-current-player-string game))
      (println message))
    (try 
      (get-player-move game)
      (catch NumberFormatException e 
        (get-move-location game (str "Invalid entry. " (build-choose-move-string game))))
      (catch clojure.lang.ExceptionInfo e
        (get-move-location game (str "Unavailable entry. " (build-choose-move-string game)))))))

(defn build-congratulations-message [token]
  (str "Congratulations! " (keyword-to-token token) " won the game!"))

(defn prompt-for-player-mark [] 
    (str "Choose your mark on the board: (X or O)"))

(defn- is-mark-input-valid? [input] 
  (cond
    (= "X" input) true
    (= "x" input) true
    (= "O" input) true
    (= "o" input) true
    :else false))

(defn- mark-input-to-keyword [input]
  (cond
    (or (= "X" input) (= "x" input)) :x
    (or (= "O" input) (= "o" input)) :o))

(defn read-player-mark-input [] 
  (let [input (read-line)]
    (if (is-mark-input-valid? input)
      (mark-input-to-keyword input)
      (throw (ex-info "You've entered an invalid mark." {})))))

(defn get-player-mark 
  ([game] (get-player-mark game (str "")))
  ([game message] 
   (do
     (draw-intro game)
     (println (str message (prompt-for-player-mark)))
     (try 
       (read-player-mark-input)
       (catch clojure.lang.ExceptionInfo e
         (get-player-mark game (str "Invalid mark. ")))))))
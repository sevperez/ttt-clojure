(ns tic-tac-toe-pair.console 
  (:require [clojure.string :as s])
  (:gen-class))

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

(defn build-square [board square]
  (let [token (get board square)]
    (if (nil? token)
      " "
      (keyword-to-token token))))

(defn build-board [board]
  (loop [square         0
         filled-board   board-shape]
    (if (>= square (count board))
      filled-board
      (recur
        (inc square)
        (s/replace
          filled-board
          (str square)
          (build-square board square))))))

(defn draw-board [board] 
  (println (build-board board)))

(defn clear-terminal []
  (do 
    (print "\u001b[2J")
    (print "\u001B[0;0f")))

(defn draw-header []
  (println "---------------------------\nTic Tac Toe\n---------------------------"))

(defn draw-player-info []
  (println "Player 1: X     Player 2: O\n"))

(defn draw-footer []
  (println "---------------------------\n"))

(defn draw-main
  ([game]
    (do
      (clear-terminal)
      (draw-header)
      (draw-player-info)
      (draw-board (:board game))
      (draw-footer)))
  ([game message]
    (do
      (clear-terminal)
      (draw-header)
      (draw-player-info)
      (draw-board (:board game))
      (println message)
      (draw-footer))))

(defn build-current-player-string [game] 
  (if (= :player-1-token (:current-token game))
    "Player 1's move!"
    "Player 2's move!"))

(defn get-move-location [game] 
  (do
    (draw-main game (build-current-player-string game))
    (println "Choose a move: "))
  (dec (try 
    (Integer/parseInt (read-line))
    (catch NumberFormatException e (get-move-location game)))))


; defn game msg="choose a move"
;   try
;     printline msg
;     parseint (readline)
;     if notvalidint
;       throw custom error
;   catch
;     call self (game "error message")
(ns tic-tac-toe-pair.console 
  (:require [clojure.string :as s])
  (:gen-class))

(def board-shape 
  (str "     |     |     \n"
       "  0  |  1  |  2  \n"
       "     |     |     \n"
       "-----------------\n"
       "     |     |     \n"
       "  3  |  4  |  5  \n"
       "     |     |     \n"
       "-----------------\n"
       "     |     |     \n"
       "  6  |  7  |  8  \n"
       "     |     |     "))

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

(defn get-move-location [] 
  (println "Enter a move:")
  (Integer/parseInt (read-line)))

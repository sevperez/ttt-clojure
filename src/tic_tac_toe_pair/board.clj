(ns tic-tac-toe-pair.board 
  (:gen-class))

(defn is-full? [board]
  (not (some nil? board)))

(defn is-available? [board location] 
  (nil? (get board location)))

(defn fill-location [board location token]
  (if (is-available? board location)
    (assoc board location token)
    board))

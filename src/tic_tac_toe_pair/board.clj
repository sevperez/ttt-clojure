(ns tic-tac-toe-pair.board 
  (:gen-class))

(defn is-full? [board]
  (not (some nil? board)))

(defn is-available? [board location] 
  (nil? (get board location)))

(defn is-move-value-valid? [board location]
  (let [board-size      (count board)
        valid-locations (vec (range board-size))]
    (contains? valid-locations location)))

(defn is-move-valid? [board location]
  (and (is-available? board location) (is-move-value-valid? board location)))

(defn fill-location [board location token]
  (if (is-available? board location)
    (assoc board location token)
    board))

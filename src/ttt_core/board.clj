(ns ttt-core.board)

(def default-board-size 3)

(defn is-full? [board]
  (not (some nil? board)))

(defn is-location-available? [board location] 
  (nil? (get board location)))

(defn is-location-valid? [board location]
  (let [board-size      (count board)
        valid-locations (vec (range board-size))]
    (contains? valid-locations location)))

(defn fill-location [board token location]
  (assoc board location token))

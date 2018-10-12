(ns tic-tac-toe-pair.rules
  (:require [tic-tac-toe-pair.board :refer [is-full? is-location-available? is-location-valid?]]))

(defn is-move-valid? [board location]
  (and (is-location-valid? board location) (is-location-available? board location)))

(def winning-combinations [[0 1 2] [3 4 5] [6 7 8] 
                           [0 3 6] [1 4 7] [2 5 8] 
                           [0 4 8] [2 4 6]])
                          
(defn- same-tokens? [line]
  (every? (fn [token] (= token (first line))) line))

(defn- build-line [board line-number]
  (map (fn [square] (get board square)) (get winning-combinations line-number)))

(defn- get-token-if-matching [board line-number]
  (let [line (build-line board line-number)]
    (if (same-tokens? line)
      (first line)
      nil)))

(defn get-winning-token [board]
  (loop [index  0
          token nil]
    (if (or (not (nil? token)) (>= index (count winning-combinations)))
      token
      (recur (inc index) (get-token-if-matching board index)))))

(defn- is-game-won? [board] (not= nil (get-winning-token board)))

(defn is-game-over? [game]
  (if (or (is-full? (:board game)) (is-game-won? (:board game)))
    true
    false))

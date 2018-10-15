(ns ttt-core.rules
  (:require [ttt-core.board :refer [is-full? is-location-available? is-location-valid?]]
            [ttt-core.board-analyzer :refer [winning-lines]]))

(defn is-move-valid? [board location]
  (and (is-location-valid? board location) (is-location-available? board location)))

(def default-board-size 3)

(defn- same-tokens? [line]
  (every? (fn [token] (= token (first line))) line))

(defn- build-line [board line-number]
  (map (fn [square] (get board square)) (get (winning-lines default-board-size) line-number)))

(defn- get-token-if-matching [board line-number]
  (let [line (build-line board line-number)]
    (if (same-tokens? line)
      (first line)
      nil)))

(defn get-winning-token [board]
  (let [win-lines  (winning-lines default-board-size)]
    (loop [index  0
           token  nil]
    (if (or (not (nil? token)) (>= index (count win-lines)))
      token
      (recur (inc index) (get-token-if-matching board index))))))

(defn- is-game-won? [board] (not= nil (get-winning-token board)))

(defn is-game-over? [game]
  (if (or (is-full? (:board game)) (is-game-won? (:board game)))
    true
    false))

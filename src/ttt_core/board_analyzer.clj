(ns ttt-core.board-analyzer
  (:require [ttt-core.board :refer [is-location-available?, fill-location, default-board-size]]))

(defn- square-position [first second size] (+ first (* second size)))

(defn- straight-lines [horizontal? size]
  (partition size
    (for [row (range size)
          col (range size)]
      (if horizontal?
        (square-position col row size)
        (square-position row col size)))))

(def horizontal-lines (partial straight-lines true))

(def vertical-lines (partial straight-lines false))

(defn- on-diagonal? [row col size start-on-left?]
  (if start-on-left?
    (= row col)
    (= (+ row col) (- size 1))))

(defn- diagonal-line [start-on-left? size]
  (list (remove nil?
    (for [row (range size)
          col (range size)]
      (when (on-diagonal? row col size start-on-left?)
        (square-position col row size))))))

(def left-diagonal-line (partial diagonal-line true))

(def right-diagonal-line (partial diagonal-line false))

(defn winning-lines
  ([]
    (winning-lines default-board-size))
  ([size]
    (vec (apply concat
      ((juxt horizontal-lines vertical-lines left-diagonal-line right-diagonal-line) size)))))

(defn empty-locations [board]
  (vec (filter (partial is-location-available? board) (range (count board)))))

(defn calculate-board-size [board] (int (Math/sqrt (count board))))

(defn possible-board-states [board next-token]
  (vec (map (partial fill-location board next-token) (empty-locations board))))

(ns ttt-core.board-analyzer
  (:require [ttt-core.board :refer [is-location-available?]]))

(defn- square-position [first second size] (+ first (* second size)))

(defn- straight-lines [size horizontal?]
  (partition size
    (for [row (range size)
          col (range size)]
      (if horizontal?
        (square-position col row size)
        (square-position row col size)))))

(defn- horizontal-lines [size] (straight-lines size true))

(defn- vertical-lines [size] (straight-lines size false))

(defn- on-diagonal? [row col size start-on-left?]
  (if start-on-left?
    (= row col)
    (= (+ row col) (- size 1))))

(defn- diagonal-line [size start-on-left?]
  (list (remove nil?
    (for [row (range size)
          col (range size)]
      (when (on-diagonal? row col size start-on-left?)
        (square-position col row size))))))

(defn- left-diagonal-line [size] (diagonal-line size true))

(defn- right-diagonal-line [size] (diagonal-line size false))

(defn winning-lines [size]
  (vec (map (fn [line] (vec line))
    (reduce (fn [lines func] (concat lines (func size))) []
      [horizontal-lines vertical-lines left-diagonal-line right-diagonal-line]))))

(defn empty-locations [board]
  (vec (filter (fn [location] (is-location-available? board location))
    (range (count board)))))

(defn calculate-board-size [board] (int (Math/sqrt (count board))))

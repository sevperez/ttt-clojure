(ns tic-tac-toe-pair.game
  (:gen-class))

(def winning-combinations [[0 1 2] [3 4 5] [6 7 8] 
                           [0 3 6] [1 4 7] [2 5 8] 
                           [0 4 8] [2 4 6]])
                          
(defn same-tokens? [line]
  (every? (fn [token] (= token (first line))) line))

(defn build-line [board line-number]
  (map
    (fn [square] (get board square))
    (get winning-combinations line-number)))

(defn get-line-winner [board line-number]
  (let [line (build-line board line-number)]
    (if (same-tokens? line)
      (first line)
      nil)))

(defn winner-found? [token] (not (nil? token)))

(defn get-winning-token [board]
  (let [num-combinations (count winning-combinations)]
    (loop [line-number  0
           token        nil]
      (if (or (winner-found? token) (>= line-number num-combinations))
        token
        (recur (inc line-number) (get-line-winner board line-number))))))

(ns tic-tac-toe-pair.game
  (:require [tic-tac-toe-pair.board :refer :all]
            [tic-tac-toe-pair.console :refer :all])
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

(defn initialize-game [] 
  { :current-token :player-1-token
    :player-1-token :x
    :player-2-token :o
    :board [nil nil nil nil nil nil nil  nil nil] })

(defn update-player [game]
  (assoc game
    :current-token
    (if (= (:current-token game) :player-1-token)
      :player-2-token
      :player-1-token)))
  
(defn update-board [game location]
  (assoc game
    :board
    (fill-location
      (:board game)
      location
      ((:current-token game) game))))

(defn update-board-and-player [game location]
  ((comp update-player update-board) game location))

(defn update-game [game location]
  (if (is-move-valid? (:board game) location)
    (update-board-and-player game location)
    game))

(defn is-game-over? [game]
  (if (or (is-full? (:board game))
    (not= nil (get-winning-token (:board game))))
    true
    false))

(defn build-congratulations-message [game token]
  (str "Congratulations! "
    (keyword-to-token (token game))
    " won the game!"))

(defn get-game-end-message [game]
  (let [winner (get-winning-token (:board game))]
    (cond 
      (= (:player-1-token game) winner)
        (build-congratulations-message game :player-1-token)
      (= (:player-2-token game) winner)
        (build-congratulations-message game :player-2-token)
      :else
        "This game ended in a tie!")))

(defn play [game]
  (loop [game     game
         history  [game]]
    (if (is-game-over? game)
      (do 
        (draw-main game (get-game-end-message game))
        history)
      (let [new-game (update-game game (get-move-location game))]
        (do
          (draw-main game)
          (recur new-game (conj history new-game)))))))


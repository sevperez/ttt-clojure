(ns artificial-intelligence.ai)

(defn get-other-player [player]
  (if (= :player-1-token player) :player-2-token :player-1-token))

(defn switch-current-token [game]
  (if (= (:current-token (last (:turns game))) :player-1-token) :player-2-token :player-1-token))

(defn- get-current-token [game] ((:current-token (last (:turns game))) game))

(defn- simulate-move [game location]
  (let [last-turn (last (:turns game))
        new-board (assoc (:board last-turn) location (get-current-token game))
        new-token (switch-current-token game)
        new-turns (conj (:turns game) {:board new-board :current-token new-token})]
    (assoc game :turns new-turns)))

(defn- generate-move-option [eval-fns algorithm-fn game player location]
  (let [next-game   (simulate-move game location)]
    (assoc {} :location location :score (algorithm-fn eval-fns next-game player))))

(defn all-move-options [eval-fns algorithm-fn game player]
  (let [available-locations ((:empty-locations eval-fns) (:board (last (:turns game))))]
    (loop [idx          0
           move-options []]
      (if (>= idx (count available-locations))
        move-options
        (recur
          (inc idx)
          (conj move-options
            (generate-move-option eval-fns algorithm-fn game player
              (get available-locations idx))))))))

(defn top-move-options [move-option-list]
  (let [best (apply max (map :score move-option-list))]
    (vec (map :location
      (filter (fn [move-option] (= best (:score move-option))) move-option-list)))))

(defn choose-move [eval-fns algorithm-fn game player]
  (let [all-moves (all-move-options eval-fns algorithm-fn game player)
        top-moves (top-move-options all-moves)
        rand-idx  (rand-int (count top-moves))]
    (get top-moves rand-idx)))

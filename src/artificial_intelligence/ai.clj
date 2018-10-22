(ns artificial-intelligence.ai)

(defn get-other-player [player]
  (if (= :player-1-token player) :player-2-token :player-1-token))

(defn switch-current-token [game]
  (if (= (:current-token game) :player-1-token) :player-2-token :player-1-token))

(defn- simulate-move [board location token] (assoc board location token))

(defn- generate-move-option [eval-fns algorithm-fn game player location]
  (let [board       (:board game)
        next-token  ((:current-token game) game)
        next-board  (simulate-move board location next-token)
        next-player (get-other-player player)
        next-game   (assoc game :board next-board :current-token next-player)]
    (assoc {} :location location :score (algorithm-fn eval-fns next-game player))))

(defn all-move-options [eval-fns algorithm-fn game player]
  (let [available-locations ((:empty-locations eval-fns) (:board game))]
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

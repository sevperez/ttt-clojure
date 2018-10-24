(ns persistence.db
  (:require [monger.core :as mg]
            [monger.collection :as mc])
  (:import org.bson.types.ObjectId))

(defn generate-uuid [] (ObjectId.))

(defn- board-and-token [item] (select-keys item [:board :current-token]))

(defn build-game-doc [history]
  (let [last-item       (last history)
        id              (if (:_id last-item) (:_id last-item) (generate-uuid))
        mode            (:game-mode last-item)
        lang            (:language last-item)
        player-1        (:player-1-token last-item)
        player-2        (:player-2-token last-item)
        history-entries (vec (map board-and-token history))]
    {:_id id 
     :language lang
     :game-mode mode
     :player-1-token player-1
     :player-2-token player-2
     :history history-entries}))

(defn save [game-history]
  (let [conn      (mg/connect)
        db        (mg/get-db conn "ttt-clojure")
        game-doc  (build-game-doc game-history)]
    (mc/update db "games" {:_id (:_id game-doc)} game-doc {:upsert true})))

(ns persistence.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [clj-time.local :as lt]
            [clj-time.format :as f])
  (:import org.bson.types.ObjectId))

(defn generate-uuid [] (ObjectId.))

(defn- board-and-token [item] (select-keys item [:board :current-token]))

(defn build-game-doc [history]
  (let [last-item (last history)]
    {:_id (if (:_id last-item) (:_id last-item) (generate-uuid))
     :created-at (:created-at last-item)
     :updated-at (:updated-at last-item)
     :language (:language last-item)
     :game-mode (:game-mode last-item)
     :player-1-token (:player-1-token last-item)
     :player-2-token (:player-2-token last-item)
     :history (vec (map board-and-token history))}))

(defn save [game-history]
  (let [conn              (mg/connect)
        db                (mg/get-db conn "ttt-clojure")
        game-doc          (build-game-doc game-history)
        update-timestamp  (f/unparse (f/formatters :date-time) (lt/local-now))]
    (mc/update db "games" 
      {:_id (:_id game-doc)} 
      (dissoc (assoc game-doc :updated-at update-timestamp) :_id) 
      {:upsert true})))

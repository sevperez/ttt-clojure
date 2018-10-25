(ns persistence.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [clj-time.local :as lt]
            [clj-time.format :as f])
  (:import org.bson.types.ObjectId))

(defn generate-uuid [] (ObjectId.))

(defn- board-and-token [item] (select-keys item [:board :current-token]))

(defn- get-local-time-string [] (f/unparse (f/formatters :date-time) (lt/local-now)))

; (defn build-game-doc [history]
;   (let [last-item (last history)]
;     {:created-at (:created-at last-item)
;      :updated-at (:updated-at last-item)
;      :language (:language last-item)
;      :game-mode (:game-mode last-item)
;      :player-1-token (:player-1-token last-item)
;      :player-2-token (:player-2-token last-item)
;      :history (vec (map board-and-token history))}))

(defn save [game-history game-id]
  (let [conn              (mg/connect)
        db                (mg/get-db conn "ttt-clojure")
        ; game-doc          (build-game-doc game-history)
        update-timestamp  (get-local-time-string)]
    (mc/update db "games" 
      {:_id game-id} 
      (assoc game-history :updated-at update-timestamp)
      {:upsert true})))

(defn retrieve-last [] {})

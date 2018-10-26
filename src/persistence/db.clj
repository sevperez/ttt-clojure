(ns persistence.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [clj-time.local :as lt]
            [clj-time.format :as f])
  (:import org.bson.types.ObjectId))

(defn generate-uuid [] (ObjectId.))

(defn- board-and-token [item] (select-keys item [:board :current-token]))

(defn- get-local-time-string [] (f/unparse (f/formatters :date-time) (lt/local-now)))

(defn save [game-history game-id]
  (let [conn              (mg/connect)
        db                (mg/get-db conn "ttt-clojure")
        update-timestamp  (get-local-time-string)]
    (mc/update db "games" 
      {:_id game-id} 
      (assoc game-history :updated-at update-timestamp)
      {:upsert true})))

(defn retrieve-last []
  (let [conn              (mg/connect)
        db                (mg/get-db conn "ttt-clojure")]
    (last (mc/find-maps db "games"))))

(defn adjust-turn-map-keywords [turn-map]
  {:board (vec (map keyword (:board turn-map)))
   :current-token (keyword (:current-token turn-map))})

(defn adjust-history-map-keywords [history-map]
  (assoc history-map
    :game-mode (keyword (:game-mode history-map))
    :language (keyword (:language history-map))
    :player-1-token (keyword (:player-1-token history-map))
    :player-2-token (keyword (:player-2-token history-map))
    :turns (vec (map adjust-turn-map-keywords (:turns history-map)))))

(defn history-to-game [history-map]
  (if history-map
    (let [adjusted-history (adjust-history-map-keywords history-map)
          last-turn        (last (:turns adjusted-history))]
      (assoc (dissoc adjusted-history :turns)
        :board (:board last-turn)
        :current-token (:current-token last-turn)))
    nil))

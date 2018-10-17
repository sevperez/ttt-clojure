(ns tic-tac-toe-pair.computer-player
    (require [tic-tac-toe-pair.ai :refer [get-best-play-location]]))

(defn get-ai-move [game] 
  (get-best-play-location game))
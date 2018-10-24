(ns ttt-core.console 
  (:require [clojure.string :as s]
            [ttt-core.rules :refer [is-move-valid?]]
            [ttt-core.translate :refer [translate]]))

(def board-shape 
  (str "          |     |     \n"
       "       0  |  1  |  2  \n"
       "          |     |     \n"
       "     -----------------\n"
       "          |     |     \n"
       "       3  |  4  |  5  \n"
       "          |     |     \n"
       "     -----------------\n"
       "          |     |     \n"
       "       6  |  7  |  8  \n"
       "          |     |     \n"))

(defn keyword-to-token [keyword] (s/upper-case (name keyword)))

(defn- set-square-content [board square]
  (let [token (get board square)]
    (if (nil? token)
      " "
      (keyword-to-token token))))

(defn- replace-square-position [board-string board square-pos]
  (s/replace board-string (str square-pos) (set-square-content board square-pos)))

(defn- build-board [board]
  (loop [square-pos     0
         filled-board   board-shape]
    (if (>= square-pos (count board))
      filled-board
      (recur (inc square-pos) (replace-square-position filled-board board square-pos)))))

(defn draw-board [board] 
  (println (build-board board)))

(defn- clear-terminal []
  (do (print "\u001b[2J") (print "\u001B[0;0f")))

(defn- draw-header [language]
  (println 
    (str "---------------------------\n" 
      (translate [language] [:title]) 
      "\n---------------------------")))

(defn draw-player-info [game]
  (let [mode            (:game-mode game)
        token-1         (keyword-to-token (:player-1-token game))
        token-2         (keyword-to-token (:player-2-token game))
        player-string   (translate [(:language game)] [:player-string])
        computer-string (translate [(:language game)] [:computer-string])]
    (cond
      (= :human-vs-human mode)
        (println (str  player-string " 1 (" token-1 ")     " player-string " 2 (" token-2 ")\n"))
      (= :human-vs-computer mode)
        (println (str player-string " (" token-1 ")     " computer-string " (" token-2 ")\n"))
      :else (println "******* (X)     ******* (O)\n"))))

(defn- draw-footer []
  (println "---------------------------\n"))

(defn draw-main [game message]
  (do
    (clear-terminal)
    (draw-header (:language game))
    (draw-player-info game)
    (draw-board (:board game))
    (println message)
    (draw-footer)))

(defn build-selection-string 
  ([language msg-keyword] (build-selection-string language msg-keyword ""))
  ([language msg-keyword prepend-msg]
    (s/trim (str prepend-msg " " (translate [language] [msg-keyword])))))
        
(defn request-game-setup
  ([game] (request-game-setup game ""))
  ([game prepend-msg]
    (let [opts {:1 :human-vs-human :2 :human-vs-computer :3 :language-setup}]
      (do
        (draw-main game (translate [(:language game)] [:game-setup-selection-header]))
        (println (build-selection-string (:language game) :choose-game-setup prepend-msg))
        (get opts (keyword (read-line)))))))

(defn request-language-setup
  ([game] (request-language-setup game ""))
  ([game prepend-msg]
    (let [opts {:1 :en :2 :pl}]
      (do
        (draw-main game (translate [(:language game)] [:game-language-selection-header]))
        (println (build-selection-string (:language game) :choose-language-setup prepend-msg))
        (get opts (keyword (read-line)))))))

(defn get-selection [game request-fn] 
  (loop [selection (request-fn game)]
    (if (not (nil? selection))
      selection
      (recur (request-fn game (translate [(:language game)] [:invalid-selection]))))))

(defn handle-language-selection [game]
  (assoc game :language (get-selection game request-language-setup)))

(defn handle-game-setup [game]
  (loop [game      game
         selection (get-selection game request-game-setup)]
    (if (= :language-setup selection)
      (let [updated-game (handle-language-selection game)]
        (recur updated-game (get-selection updated-game request-game-setup)))
      (assoc game :game-mode selection))))

(defn current-player-name [current-player-token game-mode language]
  (let [player-string   (translate [language] [:player-string])
        computer-string (translate [language] [:computer-string])]
    (if (= game-mode :human-vs-human)
      (if (= current-player-token :player-1-token) 
        (str player-string " 1") 
        (str player-string " 2"))
      (if (= current-player-token :player-1-token) player-string computer-string))))

(defn build-current-player-string [name language]
  (translate [language] [:move-string] [name]))

(defn- get-index-adjusted-input [] (dec (Integer/parseInt (read-line))))

(defn get-player-move-selection [game] 
  (let [input (get-index-adjusted-input)]
    (if (is-move-valid? (:board game) input) 
      input
      (throw (ex-info (translate [(:language game)] [:invalid-move]) {})))))

(defn- get-available-indices [board]
  (loop [index  0
         result []]
    (if (>= index (count board))
      result
      (if (nil? (get board index))
        (recur (inc index) (conj result (inc index)))
        (recur (inc index) result)))))

(defn build-choose-move-string [game]
  (str (translate [(:language game)] [:choose-move-string]) 
    " (" (s/join ", " (get-available-indices (:board game))) ")"))

(defn handle-player-move-selection
  ([game]
    (handle-player-move-selection game (build-choose-move-string game)))
  ([game message]
    (do
      (draw-main game
        (build-current-player-string
          (current-player-name (:current-token game) (:game-mode game) (:language game))
          (:language game)))
      (println message))
    (try 
      (get-player-move-selection game)
      (catch NumberFormatException e 
        (handle-player-move-selection game (str 
          (translate [(:language game)] [:invalid-entry]) " " (build-choose-move-string game))))
      (catch clojure.lang.ExceptionInfo e
        (handle-player-move-selection game (str 
          (translate [(:language game)] [:unavailable-entry]) " " (build-choose-move-string game)))))))

(defn build-congratulations-message [token language]
  (if (nil? token)
    (translate [language] [:tie-string])
    (translate [language] [:congratulations-string] [(keyword-to-token token)])))
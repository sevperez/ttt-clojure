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

(defn build-choose-game-mode-string 
  ([language] (build-choose-game-mode-string language ""))
  ([language message]
    (str message (if (= "" message) "" " ") (translate [language] [:choose-game-mode]))))

(defn request-game-mode
  ([game] (request-game-mode game ""))
  ([game message]
    (let [modes {:1 :human-vs-human :2 :human-vs-computer}]
      (do
        (draw-main game (translate [(:language game)] [:game-mode-selection-header]))
        (println (build-choose-game-mode-string (:language game) message))
        (get modes (keyword (read-line)))))))

(defn get-game-mode-selection [game] 
  (loop [selection (request-game-mode game)]
    (if (not (nil? selection))
      selection
      (recur (request-game-mode game (translate [(:language game)] [:invalid-selection]))))))

(defn handle-game-mode-selection [game]
  (assoc game :game-mode (get-game-mode-selection game)))

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
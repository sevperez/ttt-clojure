(ns ttt-core.translate
  (:require [taoensso.tempura :as tempura :refer [tr]]
            [clojure.string :as str]))

(declare genitive-with-num)

(def dictionary
  {:en
    {:missing ":en missing text"
     :choose-game-mode "Choose a game mode:\n1. Human-vs-Human\n2. Human-vs-Computer\n3. (Change game language)"
     :choose-move-string "Choose a move:"
     :computer-string "Computer"
     :congratulations-string "Congratulations! %1 won the game!"
     :game-mode-selection-header "--- Game Mode Selection ---"
     :invalid-entry "Invalid entry."
     :invalid-selection "Invalid selection."
     :invalid-move "You've entered an invalid move."
     :move-string "%1's move!"
     :player-string "Player"
     :tie-string "This game ended in a tie!"
     :title "Tic Tac Toe"
     :unavailable-entry "Unavailable entry."}
   :pl
    {:missing ":es texto faltante"
     :choose-game-mode "Wybierz tryb gry:\n1. Człowiek kontra człowiek\n2. Człowiek kontra komputer\n3. (Zmień język gry)"
     :choose-move-string "Wybierz ruch:"
     :computer-string "Komputer"
     :congratulations-string "Gratulacje! %1 wygrał!"
     :game-mode-selection-header "--- Wybór trybu gry ---"
     :invalid-entry "Niewłaściwy wpis."
     :invalid-selection "Nieprawidłowy wybór."
     :invalid-move "Wprowadziłeś nieprawidłowy ruch."
     :move-string (fn [[name]] (str "Ruch " (genitive-with-num name) "!"))
     :player-string "Gracz"
     :tie-string "Ta gra zakończyła się remis!"
     :title "Kółko i krzyżyk"
     :unavailable-entry "Niedostępny wpis."}})

(defn genitive-with-num [name]
  (if (re-find #"\d" name)
    (let [elements (str/split name #" ")
          name-str (first elements)
          num-str (last elements)]
      (str name-str "a" " " num-str))
    (str name "a")))

(def opts {:dict dictionary})

(def translate (partial tr opts))

(ns ttt-core.translate
  (:require [taoensso.tempura :as tempura :refer [tr]]
            [clojure.string :as str]))

(declare genitive-with-num)

(def dictionary
  {:en
    {:missing ":en missing text"
     :choose-game-setup "Choose a game mode:\n1. Human-vs-Human\n2. Human-vs-Computer\n3. (Change game language)\n4. (Load last game)"
     :choose-language-setup "Choose a language:\n1. English\n2. Polish"
     :choose-move-string "Choose a move:"
     :computer-string "Computer"
     :congratulations-string "Congratulations! %1 won the game!"
     :game-setup-selection-header "--- Game Mode Selection ---"
     :game-language-selection-header "--- Langauge Selection ---"
     :invalid-entry "Invalid entry."
     :invalid-selection "Invalid selection."
     :invalid-move "You've entered an invalid move."
     :move-string "%1's move!"
     :player-string "Player"
     :tie-string "This game ended in a tie!"
     :title "Tic Tac Toe"
     :unavailable-entry "Unavailable entry."}
   :pl
    {:missing ":pl brakujący tekst"
     :choose-game-setup "Wybierz tryb gry:\n1. Człowiek kontra człowiek\n2. Człowiek kontra komputer\n3. (Zmień język gry)\n4. (Załaduj ostatnią grę)"
     :choose-language-setup "Wybierz język:\n1. angielski\n2. polski"
     :choose-move-string "Wybierz ruch:"
     :computer-string "Komputer"
     :congratulations-string "Gratulacje! %1 wygrał!"
     :game-setup-selection-header "--- Wybór trybu gry ---"
     :game-language-selection-header "--- Wybór języka ---"
     :invalid-entry "Niewłaściwy wpis."
     :invalid-selection "Nieprawidłowy wybór."
     :invalid-move "Wprowadziłeś nieprawidłowy ruch."
     :move-string (fn [[name]] (str "Ruch " (genitive-with-num name) "!"))
     :player-string "Gracz"
     :tie-string "Ta gra zakończyła się remisem!"
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

(ns tic-tac-toe-pair.core
  (:require [tic-tac-toe-pair.game :refer [default-game initialize-game play]]))

(defn -main [& args]
  (play (initialize-game default-game)))

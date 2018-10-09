(ns tic-tac-toe-pair.core
  (:require [tic-tac-toe-pair.game :refer :all]))

(defn -main [& args]
  (play (initialize-game)))

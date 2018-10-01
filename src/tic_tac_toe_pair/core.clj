(ns tic-tac-toe-pair.core
  (:require [tic-tac-toe-pair.game :refer :all])
  (:gen-class))

(defn -main [& args]
  (play (initialize-game)))

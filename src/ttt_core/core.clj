(ns ttt-core.core
  (:require [ttt-core.game :refer [play]]
            [monger.core :as mg]            ;; temp
            [monger.collection :as mc]))    ;; temp

(defn -main [& args]
  (play))

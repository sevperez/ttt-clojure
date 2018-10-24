(ns ttt-core.core
  (:require [ttt-core.game :refer [play]]
            [clj-time.local :as lt]
            [clj-time.format :as f]))

(defn -main [& args]
  (play))

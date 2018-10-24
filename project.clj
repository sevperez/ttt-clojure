(defproject tic_tac_toe_pair "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.taoensso/tempura "1.2.1"]
                 [com.novemberain/monger "3.1.0"]
                 [tortue/spy "1.4.0"]]
  :main ^:skip-aot ttt-core.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

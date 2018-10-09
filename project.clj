(defproject tic_tac_toe_pair "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot tic-tac-toe-pair.core
  :target-path "target/%s"
  :profiles { :uberjar {:aot :all}
              :dev {:plugins [[venantius/ultra "0.5.2"] [com.jakemccrary/lein-test-refresh "0.23.0"]]}
  })

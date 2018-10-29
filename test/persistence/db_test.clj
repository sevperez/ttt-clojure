(ns persistence.db-test
  (:require [clojure.test :refer :all]
            [spy.core :as spy]
            [clj-time.local :as lt]
            [persistence.db :refer :all]))

(def default-time "2018-10-24T12:00:00.0Z")

(deftest generate-uuid-test
  (testing "it returns a uuid as an object of type org.bson.types.ObjectId"
    (is (= org.bson.types.ObjectId (type (generate-uuid))))))

(def save-mock
  (spy/mock (fn [game-history game-id]
    (assoc {} game-id (dissoc game-history :_id)))))

(deftest save-test
  (testing "it saves a game doc to the db"
    (let [test-history  {:_id "123abc" 
                         :created-at default-time
                         :updated-at "2018-10-24T12:00:20.0Z"
                         :language :en
                         :game-mode :human-vs-human
                         :player-1-token :x
                         :player-2-token :o
                         :history
                          [{:board [nil nil nil nil nil nil nil nil nil]
                            :current-token :player-1-token}
                           {:board [:x nil nil nil nil nil nil nil nil]
                            :current-token :player-2-token}
                           {:board [:x :o nil nil nil nil nil nil nil]
                            :current-token :player-1-token}]}
          expected-db   {"123abc" 
                          {:created-at default-time
                           :updated-at "2018-10-24T12:00:20.0Z"
                           :language :en
                           :game-mode :human-vs-human
                           :player-1-token :x
                           :player-2-token :o
                           :history
                            [{:board [nil nil nil nil nil nil nil nil nil]
                              :current-token :player-1-token}
                             {:board [:x nil nil nil nil nil nil nil nil]
                              :current-token :player-2-token}
                             {:board [:x :o nil nil nil nil nil nil nil]
                              :current-token :player-1-token}]}}]
      (with-redefs [save save-mock]
        (save test-history (:_id test-history))
        (is (= expected-db (spy/first-response save)))))))

(def retrieve-last-mock
  (spy/mock (fn [db]
    (last (vec db)))))

(deftest retrieve-last-test
  (testing "it retrieves the most recent game from the db"
    (with-redefs [retrieve-last retrieve-last-mock]
      (retrieve-last
        '({:_id "123abc"
           :created-at "2018-10-25T16:50:49.267Z"
           :game-mode "human-vs-computer"
           :turns [{:board [nil nil nil nil nil nil nil nil nil]
                    :current-token "player-1-token"}
                   {:board ["x" nil nil nil nil nil nil nil nil]
                    :current-token "player-2-token"}
                   {:board ["x" nil nil nil "o" nil nil nil nil]
                    :current-token "player-1-token"}
                   {:board ["x" "x" nil nil "o" nil nil nil nil]
                    :current-token "player-2-token"}
                   {:board ["x" "x" "o" nil "o" nil nil nil nil]
                    :current-token "player-1-token"}
                   {:board ["x" "x" "o" "x" "o" nil nil nil nil]
                    :current-token "player-2-token"}
                   {:board ["x" "x" "o" "x" "o" nil "o" nil nil]
                    :current-token "player-1-token"}]
           :language "en"
           :player-1-token "x"
           :player-2-token "o"
           :updated-at "2018-10-25T16:50:54.588Z"}
          {:_id "456def"
           :created-at "2018-10-25T18:17:36.466Z"
           :game-mode "human-vs-human"
           :turns [{:board [nil nil nil nil nil nil nil nil nil]
                    :current-token "player-1-token"}
                   {:board [nil nil nil "x" nil nil nil nil nil]
                    :current-token "player-2-token"}
                   {:board ["o" nil nil "x" nil nil nil nil nil]
                    :current-token "player-1-token"}
                   {:board ["o" nil nil "x" nil nil nil nil "x"]
                    :current-token "player-2-token"}
                   {:board ["o" "o" nil "x" nil nil nil nil "x"]
                    :current-token "player-1-token"}
                   {:board ["o" "o" "x" "x" nil nil nil nil "x"]
                    :current-token "player-2-token"}
                   {:board ["o" "o" "x" "x" nil nil nil "o" "x"]
                    :current-token "player-1-token"}
                   {:board ["o" "o" "x" "x" nil "x" nil "o" "x"]
                    :current-token "player-2-token"}]
           :language "pl"
           :player-1-token "x"
           :player-2-token "o"
           :updated-at "2018-10-25T18:18:02.007Z"}))
      (is (= {:_id "456def"
              :created-at "2018-10-25T18:17:36.466Z"
              :game-mode "human-vs-human"
              :turns [{:board [nil nil nil nil nil nil nil nil nil]
                       :current-token "player-1-token"}
                      {:board [nil nil nil "x" nil nil nil nil nil]
                       :current-token "player-2-token"}
                      {:board ["o" nil nil "x" nil nil nil nil nil]
                       :current-token "player-1-token"}
                      {:board ["o" nil nil "x" nil nil nil nil "x"]
                       :current-token "player-2-token"}
                      {:board ["o" "o" nil "x" nil nil nil nil "x"]
                       :current-token "player-1-token"}
                      {:board ["o" "o" "x" "x" nil nil nil nil "x"]
                       :current-token "player-2-token"}
                      {:board ["o" "o" "x" "x" nil nil nil "o" "x"]
                       :current-token "player-1-token"}
                      {:board ["o" "o" "x" "x" nil "x" nil "o" "x"]
                       :current-token "player-2-token"}]
              :language "pl"
              :player-1-token "x"
              :player-2-token "o"
              :updated-at "2018-10-25T18:18:02.007Z"}
        (spy/first-response retrieve-last)))))
  (testing "it returns nil if the db is empty"
    (with-redefs [retrieve-last retrieve-last-mock]
      (retrieve-last '())
      (is (= nil (spy/last-response retrieve-last))))))

(deftest adjust-history-map-keywords-test
  (testing "it returns a history map with database-provided values converted back to keywords"
    (is (= {:_id "456def"
              :created-at "2018-10-25T18:17:36.466Z"
              :game-mode :human-vs-human
              :turns [{:board [:x :o nil nil nil nil nil nil nil]
                       :current-token :player-1-token}]
              :language :pl
              :player-1-token :x
              :player-2-token :o
              :updated-at "2018-10-25T18:18:02.007Z"}
        (adjust-history-map-keywords
          {:_id "456def"
           :created-at "2018-10-25T18:17:36.466Z"
           :game-mode "human-vs-human"
           :turns [{:board ["x" "o" nil nil nil nil nil nil nil]
                    :current-token "player-1-token"}]
           :language "pl"
           :player-1-token "x"
           :player-2-token "o"
           :updated-at "2018-10-25T18:18:02.007Z"})))))

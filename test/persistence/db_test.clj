(ns persistence.db-test
  (:require [clojure.test :refer :all]
            [spy.core :as spy]
            [clj-time.local :as lt]
            [persistence.db :refer :all]))

(def save-mock
  (spy/mock (fn [game-history]
    (let [db        {}
          game-doc  (build-game-doc game-history)
          id        (:_id game-doc)]
      (assoc db id (dissoc game-doc :_id))))))

(def default-time "2018-10-24T12:00:00.0Z")

(deftest save-test
  (testing "it saves a game game doc to the db"
    (let [test-history  [{:_id "123abc"
                          :created-at default-time
                          :updated-at "2018-10-24T12:00:00.0Z"
                          :board [nil nil nil nil nil nil nil nil nil]
                          :current-token :player-1-token
                          :game-mode :human-vs-human
                          :language :en
                          :player-1-token :x
                          :player-2-token :o}
                         {:_id "123abc"
                          :created-at default-time
                          :updated-at "2018-10-24T12:00:11.0Z"
                          :board [:x nil nil nil nil nil nil nil nil]
                          :current-token :player-2-token
                          :game-mode :human-vs-human
                          :language :en
                          :player-1-token :x
                          :player-2-token :o}
                         {:_id "123abc"
                          :created-at default-time
                          :updated-at "2018-10-24T12:00:20.0Z"
                          :board [:x :o nil nil nil nil nil nil nil]
                          :current-token :player-1-token
                          :game-mode :human-vs-human
                          :language :en
                          :player-1-token :x
                          :player-2-token :o}]
          expected-db   { "123abc" 
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
        (save test-history)
        (is (spy/called-with? save test-history))
        (is (= expected-db (spy/first-response save)))))))

(deftest generate-uuid-test
  (testing "it returns a uuid as an object of type org.bson.types.ObjectId"
    (is (= org.bson.types.ObjectId (type (generate-uuid))))))

(deftest build-game-doc-test
  (testing "it builds a normalized game-doc"
    (is (=
      {:_id "123abc"
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
      (build-game-doc 
        [{:_id "123abc"
          :created-at default-time
          :updated-at "2018-10-24T12:00:00.0Z"
          :board [nil nil nil nil nil nil nil nil nil]
          :current-token :player-1-token
          :game-mode :human-vs-human
          :language :en
          :player-1-token :x
          :player-2-token :o}
         {:_id "123abc"
          :created-at default-time
          :updated-at "2018-10-24T12:00:11.0Z"
          :board [:x nil nil nil nil nil nil nil nil]
          :current-token :player-2-token
          :game-mode :human-vs-human
          :language :en
          :player-1-token :x
          :player-2-token :o}
         {:_id "123abc"
          :created-at default-time
          :updated-at "2018-10-24T12:00:20.0Z"
          :board [:x :o nil nil nil nil nil nil nil]
          :current-token :player-1-token
          :game-mode :human-vs-human
          :language :en
          :player-1-token :x
          :player-2-token :o}])))))
  (testing "it inserts a uuid of type org.bson.types.ObjectId if no id in game doc"
    (is (= org.bson.types.ObjectId
      (type (:_id
        (build-game-doc 
          [{:_id nil
            :board [nil nil nil nil nil nil nil nil nil]
            :current-token :player-1-token
            :game-mode :human-vs-human
            :language :en
            :player-1-token :x
            :player-2-token :o}]))))))

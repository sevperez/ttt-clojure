(ns persistence.db-test
  (:require [clojure.test :refer :all]
            [persistence.db :refer :all]))

(deftest generate-uuid-test
  (testing "it returns a uuid as an object of type org.bson.types.ObjectId"
    (is (= org.bson.types.ObjectId (type (generate-uuid))))))

(deftest build-game-doc-test
  (testing "it builds a normalized game-doc"
    (is (=
      {:_id "123abc"
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
          :board [nil nil nil nil nil nil nil nil nil]
          :current-token :player-1-token
          :game-mode :human-vs-human
          :language :en
          :player-1-token :x
          :player-2-token :o}
         {:_id "123abc"
          :board [:x nil nil nil nil nil nil nil nil]
          :current-token :player-2-token
          :game-mode :human-vs-human
          :language :en
          :player-1-token :x
          :player-2-token :o}
         {:_id "123abc"
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

;; TEMPORARY TEST DATA

(def test-history-1
  [{:_id "123abc" 
    :board [nil nil nil nil nil nil nil nil nil],
    :current-token :player-1-token,
    :game-mode :human-vs-human,
    :language :en,
    :player-1-token :x,
    :player-2-token :o}
   {:_id "123abc"
    :board [:x nil nil nil nil nil nil nil nil],
    :current-token :player-2-token,
    :game-mode :human-vs-human,
    :language :en,
    :player-1-token :x,
    :player-2-token :o}
   {:_id "123abc"
    :board [:x :o nil nil nil nil nil nil nil],
    :current-token :player-1-token,
    :game-mode :human-vs-human,
    :language :en,
    :player-1-token :x,
    :player-2-token :o}])

(def test-history-2
  [{:_id "123abc" 
    :board [nil nil nil nil nil nil nil nil nil],
    :current-token :player-1-token,
    :game-mode :human-vs-human,
    :language :en,
    :player-1-token :x,
    :player-2-token :o}
   {:_id "123abc"
    :board [:x nil nil nil nil nil nil nil nil],
    :current-token :player-2-token,
    :game-mode :human-vs-human,
    :language :en,
    :player-1-token :x,
    :player-2-token :o}
   {:_id "123abc"
    :board [:x :o nil nil nil nil nil nil nil],
    :current-token :player-1-token,
    :game-mode :human-vs-human,
    :language :en,
    :player-1-token :x,
    :player-2-token :o}
   {:_id "123abc"
    :board [:x :o :x nil nil nil nil nil nil],
    :current-token :player-2-token,
    :game-mode :human-vs-human,
    :language :en,
    :player-1-token :x,
    :player-2-token :o}
   {:_id "123abc"
    :board [:x :o :x :o nil nil nil nil nil],
    :current-token :player-1-token,
    :game-mode :human-vs-human,
    :language :en,
    :player-1-token :x,
    :player-2-token :o}
   {:_id "123abc"
    :board [:x :o :x :o :x nil nil nil nil],
    :current-token :player-2-token,
    :game-mode :human-vs-human,
    :language :en,
    :player-1-token :x,
    :player-2-token :o}
   {:_id "123abc"
    :board [:x :o :x :o :x :o nil nil nil],
    :current-token :player-1-token,
    :game-mode :human-vs-human,
    :language :en,
    :player-1-token :x,
    :player-2-token :o}
   {:_id "123abc"
    :board [:x :o :x :o :x :o :x nil nil],
    :current-token :player-2-token,
    :game-mode :human-vs-human,
    :language :en,
    :player-1-token :x,
    :player-2-token :o}])

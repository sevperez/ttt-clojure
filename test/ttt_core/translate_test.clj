(ns ttt-core.translate-test
  (:require [clojure.test :refer :all]
            [ttt-core.translate :refer :all]))

(deftest translate-test
  (testing "it returns the :en string for a given message when passed :en arg"
    (is (= "Tic Tac Toe"
      (translate [:en] [:title]))))
  (testing "it returns the :pl string for a given message when passed :pl arg"
    (is (= "Kółko i krzyżyk"
      (translate [:pl] [:title])))))

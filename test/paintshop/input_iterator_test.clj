(ns paintshop.input-iterator-test
  (:require [clojure.test :refer :all]
            [paintshop.input-iterator :refer :all]
            [paintshop.test-helper :refer :all]))

(deftest input-iterator-test
  (testing "Iterator returns lines."
    (is (= LINES (read-lines SUCCESS-INPUT-ITERATOR))))
  )

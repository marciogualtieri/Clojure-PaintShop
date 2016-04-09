(ns paintshop.core-test
  (:require [clojure.test :refer :all]
            [paintshop.core :refer :all]
            [paintshop.test-helper :refer :all])
  )

(deftest core-test
  (testing "Input file should be processed."
    (is (= OUTPUTS (execute SUCCESS-INPUT-FILE))))
  )

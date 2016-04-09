(ns paintshop.test-case-test
  (:require [clojure.test :refer :all]
            [paintshop.test-case :refer :all]
            [paintshop.test-helper :refer :all]))

(deftest test-case-process-test
  (testing "Test cases get processed and respective solutions returned."
    (is (= SOLUTIONS (process-test-cases TEST-CASES))))
  )
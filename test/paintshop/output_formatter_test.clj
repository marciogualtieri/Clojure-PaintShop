(ns paintshop.output-formatter-test
  (:require [clojure.test :refer :all]
            [paintshop.output-formatter :refer :all]
            [paintshop.test-helper :refer :all]))

(def OUTPUT-FORMATTER (->PlainTextOutputFormatter))

(deftest output-formatter-test
  (testing "Outputs are formatted to plain text."
    (is (= OUTPUTS (format-outputs OUTPUT-FORMATTER TEST-CASES SOLUTIONS))))
  )
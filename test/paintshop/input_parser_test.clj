(ns paintshop.input-parser-test
  (:require [clojure.test :refer :all]
            [paintshop.input-parser :refer :all]
            [paintshop.input-iterator :refer :all]
            [paintshop.error-messages :refer :all]
            [paintshop.field-names :refer :all]
            [paintshop.test-helper :refer :all])
  (:import (clojure.lang ExceptionInfo)))

(def INPUT-PARSER (->PlainTexFiletInputParser))

(deftest test-input-parser
  (testing "Plain text input is parsed into test cases."
    (is (= TEST-CASES (parse-input INPUT-PARSER SUCCESS-INPUT-ITERATOR)))
    )

  (testing "Input parser throws exception when number of test cases is not a number."
    (is (thrown-with-msg? ExceptionInfo
                          (re-pattern (not-a-number-error-message NUM-TEST-CASES-FIELD-NAME (->Line "X" 1)))
                          (realize-sequence (parse-input INPUT-PARSER NUM-TEST-CASES-NOT-NUMBER-INPUT-ITERATOR))))
    )

  (testing "Input parser throws exception when number of colors is not a number."
    (is (thrown-with-msg? ExceptionInfo
                          (re-pattern (not-a-number-error-message NUM-COLORS-FIELD-NAME (->Line "X" 2)))
                          (realize-sequence (parse-input INPUT-PARSER NUM-COLORS-NOT-NUMBER-INPUT-ITERATOR))))
    )

  (testing "Input parser throws exception when number of customers is not a number."
    (is (thrown-with-msg? ExceptionInfo
                          (re-pattern (not-a-number-error-message NUM-CUSTOMERS-FIELD-NAME (->Line "X" 3)))
                          (realize-sequence (parse-input INPUT-PARSER NUM-CUSTOMERS-NOT-NUMBER-INPUT-ITERATOR))))
    )

  (testing "Input parser throws exception when customer pairs are not numbers."
    (is (thrown-with-msg? ExceptionInfo
                          (re-pattern (non-numeric-customer-pairs-error-message (->Line "2 1 0 X 0" 5)))
                          (realize-sequence (parse-input INPUT-PARSER CUSTOMER-PAIRS-NOT-NUMBERS-INPUT-ITERATOR))))
    )

  (testing "Input parser throws exception when customer pairs has more than one matte color."
    (is (thrown-with-msg? ExceptionInfo
                          (re-pattern (more-than-one-matte-color-error-message (->Line "2 1 1 2 1" 5)))
                          (realize-sequence
                            (parse-input INPUT-PARSER CUSTOMER-WITH-MORE-THAN-ONE-MATTE-COLOR-INPUT-ITERATOR))))
    )

  (testing "Input parser throws exception when customer pairs has incorrect size."
    (is (thrown-with-msg? ExceptionInfo
                          (re-pattern (incorrect-number-customer-pairs-error-message (->Line "2 1 1" 4)))
                          (realize-sequence
                            (parse-input INPUT-PARSER INCORRECT-NUM-CUSTOMER-PAIRS-INPUT-ITERATOR))))
    )

  (testing "Input parser throws exception when customer pairs has invalid color code."
    (is (thrown-with-msg? ExceptionInfo
                          (re-pattern (invalid-color-code-error-message 5 (->Line "1 6 1" 4)))
                          (realize-sequence
                            (parse-input INPUT-PARSER INVALID-COLOR-CODE-INPUT-ITERATOR))))
    )

  (testing "Input parser throws exception when customer pairs has invalid finish code."
    (is (thrown-with-msg? ExceptionInfo
                          (re-pattern (invalid-finish-code-error-message (->Line "1 1 3" 4)))
                          (realize-sequence
                            (parse-input INPUT-PARSER INVALID-FINISH-CODE-INPUT-ITERATOR)))))
  )
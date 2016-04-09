(ns paintshop.test-helper
  (:require [clojure.test :refer :all]
            [paintshop.customer :refer :all]
            [paintshop.test-case :refer :all]
            [paintshop.input-iterator :refer :all]
            [paintshop.output-formatter :refer :all])
  (:import [org.apache.commons.io FilenameUtils]))

(def CUSTOMER-FIRST-COLOR-GLOSSY-SECOND-COLOR-MATTE (->Customer (biginteger 2r1) 1))
(def CUSTOMER-FIRST-COLOR-GLOSSY (->Customer (biginteger 2r1) nil))
(def BATCHES-SECOND-COLOR-MATTE (biginteger 2r10))
(def BATCHES-ALL-MATTE (biginteger 2r0000000000))
(def BATCHES-FIRST-COLOR-MATTE (biginteger 2r1))

(def FIRST-TEST-CASE (->TestCase 5 (seq [(->Customer (biginteger 2r0) 0) (->Customer (biginteger 2r11) nil)
                                         (->Customer (biginteger 2r10000) nil)])))
(def SECOND-TEST-CASE (->TestCase 1 (seq [(->Customer (biginteger 2r1) nil) (->Customer (biginteger 2r0) 0)])))
(def TEST-CASES [FIRST-TEST-CASE SECOND-TEST-CASE])
(def SOLUTIONS (seq [(biginteger 2r1) nil]))

(def LINES (seq [(->Line "2" 1)
                 (->Line "5" 2)
                 (->Line "3" 3)
                 (->Line "1 1 1" 4)
                 (->Line "2 1 0 2 0" 5)
                 (->Line "1 5 0" 6)
                 (->Line "1" 7)
                 (->Line "2" 8)
                 (->Line "1 1 0" 9)
                 (->Line "1 1 1" 10)])
  )

(def OUTPUTS (seq ["Case #1: 1 0 0 0 0" "Case #2: IMPOSSIBLE"])
  )

(defn- separator-to-system [path]
  (FilenameUtils/separatorsToSystem path)
  )

(def SUCCESS-INPUT-FILE (separator-to-system "resources/input/success.txt"))

(def SUCCESS-INPUT-ITERATOR (->PlainTextInputIterator SUCCESS-INPUT-FILE))

(def EOF-INPUT-ITERATOR
  (->PlainTextInputIterator (separator-to-system "resources/input/unexpected_end_of_file.txt"))
  )

(def NUM-COLORS-NOT-NUMBER-INPUT-ITERATOR
  (->PlainTextInputIterator (separator-to-system "resources/input/number_colors_not_a_number.txt"))
  )

(def NUM-TEST-CASES-NOT-NUMBER-INPUT-ITERATOR
  (->PlainTextInputIterator (separator-to-system "resources/input/number_test_cases_not_a_number.txt"))
  )

(def NUM-CUSTOMERS-NOT-NUMBER-INPUT-ITERATOR
  (->PlainTextInputIterator (separator-to-system "resources/input/number_customers_not_a_number.txt"))
  )

(def CUSTOMER-PAIRS-NOT-NUMBERS-INPUT-ITERATOR
  (->PlainTextInputIterator (separator-to-system "resources/input/customer_pairs_not_numbers.txt"))
  )

(def CUSTOMER-WITH-MORE-THAN-ONE-MATTE-COLOR-INPUT-ITERATOR
  (->PlainTextInputIterator (separator-to-system "resources/input/customer_with_more_than_one_matte_color.txt"))
  )

(def INCORRECT-NUM-CUSTOMER-PAIRS-INPUT-ITERATOR
  (->PlainTextInputIterator (separator-to-system "resources/input/incorrect_number_pairs.txt"))
  )

(def INVALID-COLOR-CODE-INPUT-ITERATOR
  (->PlainTextInputIterator (separator-to-system "resources/input/invalid_color_code.txt"))
  )

(def INVALID-FINISH-CODE-INPUT-ITERATOR
  (->PlainTextInputIterator (separator-to-system "resources/input/invalid_finish_code.txt"))
  )

(defn realize-sequence [sequence-to-realize]
  (str sequence-to-realize)
  )

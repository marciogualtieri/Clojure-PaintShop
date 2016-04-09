(ns paintshop.input-parser
  (:require [paintshop.input-iterator :refer :all]
            [paintshop.customer :refer :all]
            [paintshop.test-case :refer :all]
            [paintshop.error-messages :refer :all]
            [paintshop.field-names :refer :all]
            [clojure.string :as string]
            [slingshot.slingshot :refer [throw+]]))

(def ^{:private true} GLOSSY 0)
(def ^{:private true} MATTE 1)

(defprotocol InputParser
  "
  Parses input to test cases. Concrete records available at the moment:

  - PlainTexFiletInputParser, which parses plain text input following the syntax from the requirements.
  "
  (parse-input [this input-iterator])
  )

(defn- parse-number [name line]
  (if (nil? line)
    (throw+ {:type ::input-parser :message UNEXPECTED-END-OF-FILE-ERROR-MESSAGE})
    )
  (try
    (Integer/parseInt (:value line))
    (catch NumberFormatException e (throw+ {:type ::input-parser :message (not-a-number-error-message name line)})))
  )

(defn- parse-customer-parameters [line]
  (if (nil? line)
    (throw+ {:type ::input-parser :message UNEXPECTED-END-OF-FILE-ERROR-MESSAGE})
    )
  (try
    (doall (map #(Integer/parseInt %) (string/split (:value line) #" ")))
    (catch NumberFormatException e
      (throw+ {:type ::input-parser :message (non-numeric-customer-pairs-error-message line)})))
  )

(defn- partition-input-into-test-cases [lines num-test-cases]
  (loop [lines lines test-cases [] test-case-count 0]
    (if (= test-case-count num-test-cases)
      test-cases
      (let [num-customers (parse-number NUM-CUSTOMERS-FIELD-NAME (second lines))]
        (let [test-case (first (split-at (+ num-customers 2) lines))]
          (recur (nthnext lines (+ num-customers 2)) (conj test-cases test-case) (inc test-case-count))
          ))))
  )

(defn- validate-color [color num-colors line]
  (if (or (< color 1) (> color num-colors))
    (throw+ {:type ::input-parser :message (invalid-color-code-error-message num-colors line)}))
  )

(defn- validate-finish [finish line]
  (if (and (not= finish GLOSSY) (not= finish MATTE))
    (throw+ {:type ::input-parser :message (invalid-finish-code-error-message line)}))
  )

(defn- validate-pairs [pairs num-colors line]
  (loop [pairs pairs matte nil]
    (if (not (empty? pairs))
      (let [pair (first pairs)]
        (let [color (first pair) finish (second pair)]
          (validate-color color num-colors line)
          (validate-finish finish line)
          (if (and (not (nil? matte)) (= finish MATTE))
            (throw+ {:type ::input-parser :message (more-than-one-matte-color-error-message line)})
            )
          (if (= finish MATTE)
            (recur (next pairs) finish)
            (recur (next pairs) matte)
            )))))
  )

(defn- parse-glossies [pairs]
  (loop [pairs pairs glossies (biginteger 0)]
    (if (empty? pairs)
      glossies
      (let [pair (first pairs)]
        (let [color (first pair) finish (second pair)]
          (if (= finish GLOSSY)
            (recur (next pairs) (.setBit glossies (dec color)))
            (recur (next pairs) glossies))))))
  )

(defn- parse-matte [pairs]
  (loop [pairs pairs matte nil]
    (if (empty? pairs)
      matte
      (let [pair (first pairs)]
        (let [color (first pair) finish (second pair)]
          (if (= finish MATTE)
            (dec color)
            (recur (next pairs) matte))))))
  )

(defn- parse-customer [num-colors line]
  (let [num-colors num-colors parameters (parse-customer-parameters line)]
    (let [num-pairs (first parameters)]
      (let [pairs (partition 2 (next parameters))]
        (if (not= num-pairs (count pairs))
          (throw+ {:type ::input-parser :message (incorrect-number-customer-pairs-error-message line)})
          )
        (validate-pairs pairs num-colors line)
        (let [glossies (parse-glossies pairs) matte (parse-matte pairs)]
          (->Customer glossies matte)))))
  )

(defn- parse-test-case [lines]
  (let [num-colors (parse-number NUM-COLORS-FIELD-NAME (first lines))]
    (def customers (map #(parse-customer num-colors %) (nnext lines)))
    (->TestCase num-colors customers))
  )

(defn- parse-plain-text-input [input-iterator]
  (let [lines (read-lines input-iterator)]
    (let [num-test-cases (parse-number NUM-TEST-CASES-FIELD-NAME (first lines))]
      (let [test-cases (partition-input-into-test-cases (next lines) num-test-cases)]
        (map parse-test-case test-cases))))
  )

(defrecord PlainTexFiletInputParser []
  InputParser
  (parse-input [this input-iterator]
    (parse-plain-text-input input-iterator))
  )
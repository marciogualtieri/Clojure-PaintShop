(ns paintshop.error-messages)

(def UNEXPECTED-END-OF-FILE-ERROR-MESSAGE "Unexpected end of file.")

(defn not-a-number-error-message [name line]
  (format "<%s> is not a number on line %d: '%s'." name (:number line) (:value line))
  )

(defn non-numeric-customer-pairs-error-message [line]
  (format "Customer pairs are not a numbers on line %d: '%s'." (:number line) (:value line))
  )

(defn more-than-one-matte-color-error-message [line]
  (format "More than one matte color for customer on line %d: '%s'." (:number line) (:value line))
  )

(defn incorrect-number-customer-pairs-error-message [line]
  (format "Number of customer pairs is incorrect on line %d: '%s'." (:number line) (:value line))
  )

(defn invalid-color-code-error-message [num-colors line]
  (format "Color code should be in the range 1..%d on line %d: '%s'." num-colors (:number line) (:value line))
  )

(defn invalid-finish-code-error-message [line]
  (format "Finish code should be 0 or 1 on line %d: '%s'." (:number line) (:value line))
  )
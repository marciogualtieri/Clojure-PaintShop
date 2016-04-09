(ns paintshop.test-case
  (:require [slingshot.slingshot :refer [try+ throw+]]
            [paintshop.customer :refer :all]))

(defrecord TestCase [num-colors customers])

(defrecord Solution [batches is-final-solution])

(defn- fixed-batches [batches customer]
  (if (has-matte? customer)
    (.setBit batches (:matte customer))
    (throw+ {:type ::cannot-fix-batches}))
  )

(defn- process-batches [customers batches]
  (loop [batches batches customers customers customer-index 0 is-final-solution true]
    (if (= customer-index (count customers))
      (->Solution batches is-final-solution)
      (if (is-batches-satisfactory? (nth customers customer-index) batches)
        (recur batches customers (inc customer-index) is-final-solution)
        (recur (fixed-batches batches (nth customers customer-index)) customers (inc customer-index) false))))
  )

(defn- attempt-to-process-test-case [test-case batches]
  (let [customers (:customers test-case)]
    (loop [test-case test-case solution (process-batches customers batches)]
      (if (:is-final-solution solution)
        (:batches solution)
        (recur test-case (process-batches customers (:batches solution))))))
  )

(defn- process-test-case [test-case]
  (try+ (attempt-to-process-test-case test-case (biginteger 0))
        (catch [:type ::cannot-fix-batches] {:keys []} nil))
  )

(defn process-test-cases [test-cases]
  (map process-test-case test-cases)
  )
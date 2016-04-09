(ns paintshop.customer
  (require [clojure.pprint :refer [cl-format]]))

(defn- satisfied-glossy-colors [batches glossies]
  (.bitCount (.xor (.and batches glossies) glossies))
  )

(defn- is-matte-color-satisfied? [batches matte]
  (if matte (.testBit batches matte) false)
  )

(defrecord Customer [glossies matte])

(defmulti is-batches-satisfactory? (fn [customer batches] [(class customer) (class batches)]))
(defmethod is-batches-satisfactory? [Customer BigInteger] [customer batches]
  (or (< 0 (satisfied-glossy-colors batches (:glossies customer)))
      (is-matte-color-satisfied? batches (:matte customer)))
  )

(defmulti has-matte? class)
(defmethod has-matte? Customer [customer]
  (not (nil? (:matte customer)))
  )
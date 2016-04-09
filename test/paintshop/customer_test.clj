(ns paintshop.customer-test
  (:require [clojure.test :refer :all]
            [paintshop.customer :refer :all]
            [paintshop.test-helper :refer :all]))

(deftest customer-test
  (testing "Batches with second color matte satisfies customer."
    (is (true? (is-batches-satisfactory? CUSTOMER-FIRST-COLOR-GLOSSY-SECOND-COLOR-MATTE BATCHES-SECOND-COLOR-MATTE)))
    )
  (testing "Batches with second color matte satisfies customer."
    (is (true? (is-batches-satisfactory? CUSTOMER-FIRST-COLOR-GLOSSY-SECOND-COLOR-MATTE BATCHES-ALL-MATTE)))
    )
  (testing "Batches with first color matte does not satisfy customer."
    (is (false? (is-batches-satisfactory? CUSTOMER-FIRST-COLOR-GLOSSY-SECOND-COLOR-MATTE BATCHES-FIRST-COLOR-MATTE)))
    )
  (testing "Customer has matte."
    (is (true? (has-matte? CUSTOMER-FIRST-COLOR-GLOSSY-SECOND-COLOR-MATTE)))
    )
  (testing "Customer hasn't matte."
    (is (false? (has-matte? CUSTOMER-FIRST-COLOR-GLOSSY)))
    )
  )

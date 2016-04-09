(ns paintshop.output-formatter
  (require [clojure.pprint :refer [cl-format]])
  (require [clojure.string :as string]))

(defprotocol OutputFormatter
  "
  Formats batches test cases and batches solutions for output. Concrete records available at the moment:

  - PlainTextOutputFormatter, which formats according to the syntax from the requirements.
  "
  (format-outputs [this test-cases solutions])
  )

(defn- format-solution-to-plain-text [num-colors solution]
  (string/join " " (string/reverse (cl-format nil (str "~" num-colors "'0b") solution)))
  )

(defn- format-output-to-plain-text [index test-case solution]
  (if (nil? solution)
    (format "Case #%d: IMPOSSIBLE" (inc index))
    (format "Case #%d: %s" (inc index) (format-solution-to-plain-text (:num-colors test-case) solution)))
  )

(defrecord PlainTextOutputFormatter []
  OutputFormatter
  (format-outputs [this test-cases solutions]
    (map format-output-to-plain-text
         (range (count test-cases))
         test-cases
         solutions))
  )

(ns paintshop.input-iterator
  (require [clojure.java.io :refer [reader]]
           [slingshot.slingshot :refer [throw+]]))

(defprotocol InputIterator
  "
  Iterates though the input line by line. Concrete record implemenations available so far:

  - PlainTextInputIterator, which iterates through a plain text input file.
  "
  (read-lines [this])
  )

(defrecord Line [value number])

(defrecord PlainTextInputIterator [file-name-and-path]
  InputIterator
  (read-lines [this]
    (map-indexed (fn [index line-value] (->Line line-value (inc index)))
                 (line-seq (reader (:file-name-and-path this)))
                 ))
  )








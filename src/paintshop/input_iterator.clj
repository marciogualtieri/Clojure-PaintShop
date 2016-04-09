(ns paintshop.input-iterator
  (require [clojure.java.io :refer [reader]]
           [slingshot.slingshot :refer [throw+]]))

(defprotocol InputIterator
  (read-lines [this])
  )

(defrecord Line [value number])

(defrecord PlainTextInputIterator [file-name-and-path]
  InputIterator
  (read-lines [this]
    (try
      (map-indexed (fn [index line-value] (->Line line-value (inc index)))
                   (line-seq (reader (:file-name-and-path this)))
                   )
      (catch Exception e
        (throw+ {:type ::input-iterator :file file-name-and-path :message (str "unexpected end of file"
                                                                               (.getMessage e))}))))
  )








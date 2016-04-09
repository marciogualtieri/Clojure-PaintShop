(ns paintshop.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.java.io :refer [writer]]
            [slingshot.slingshot :refer [try+]]
            [paintshop.cli-config :refer :all]
            [paintshop.input-iterator :refer :all]
            [paintshop.input-parser :refer :all]
            [paintshop.output-formatter :refer :all]
            [paintshop.test-case :refer :all])
  (:gen-class))

(def LINE_SEPARATOR (System/getProperty "line.separator"))

(defn execute
  "Parses input file into test cases, process test cases into solutions, format solutions to output and return output."
  [input-file]
  (let [input-iterator (->PlainTextInputIterator input-file)
        input-parser (->PlainTexFiletInputParser)
        output-formatter (->PlainTextOutputFormatter)]
    (let [test-cases (parse-input input-parser input-iterator)]
      (let [solutions (process-test-cases test-cases)]
        (format-outputs output-formatter test-cases solutions)
        )
      )
    )
  )

(defn execute-with-benchmarking [input-file]
  "Same as execute, but prints total processing time."
  (let [start-time (System/currentTimeMillis)]
    (let [solutions (execute input-file)]
      (let [end-time (System/currentTimeMillis)]
        (println (format "\nTotal processing time: %d ms\n" (- end-time start-time)))
        solutions)))
  )

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      (not= (count arguments) 1) (exit 1 (usage summary))
      errors (exit 1 (error-msg errors)))

    (try+
      (let [input-file (first arguments) output-file (options :output-file)]
        (let [solutions (execute-with-benchmarking input-file)]
          (if (nil? output-file)
            (doall (map println solutions))
            (with-open [file-writer (writer output-file)]
              (doall (map #(.write file-writer (str % LINE_SEPARATOR)) solutions))))))
      (catch Object _ (println (:message (:object &throw-context))))))
  )
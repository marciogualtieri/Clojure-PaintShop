(ns paintshop.core
  (:require [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.java.io :refer [writer]]
            [slingshot.slingshot :refer [try+]]
            [paintshop.input-iterator :refer :all]
            [paintshop.input-parser :refer :all]
            [paintshop.output-formatter :refer :all]
            [paintshop.test-case :refer :all])
  (:gen-class))

(def LINE_SEPARATOR (System/getProperty "line.separator"))

(defn execute [input-file]
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

(def cli-options
  [["-o" "--output-file FILE" "Name of the output file. If not provided will print output to console."
    :id :output-file]
   ["-h" "--help"]]
  )

(defn usage [options-summary]
  (->> ["Usage: java -jar <jar name> <input file> [options]"
        ""
        "Options:"
        options-summary
        ""
        "<input-file>: Name of the input file containing test cases."
        ""]
       (string/join \newline))
  )

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors))
  )

(defn exit [status msg]
  (println msg)
  (System/exit status)
  )

(defn execute-with-benchmarking [input-file]
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
(ns paintshop.cli-config
  (:require [clojure.string :as string])
  )

;; Configuration for the command-line interface (tools.cli)

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

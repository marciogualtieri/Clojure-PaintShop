(defproject paintshop "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://github/marciogualtieri"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.3"]
                 [slingshot "0.12.2"]
                 [commons-io/commons-io "2.4"]]
  :main paintshop.core
  :aot [paintshop.core]
  :plugins [[lein-codox "0.9.4"]])
(defproject ad-muter-ml "0.1.0-SNAPSHOT"
  :description "Image Machine Learning"
  :url "https://github.com/jholkeboer/ad-muter-ml"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"project" "file:repo"}
  :resource-paths ["resources" "images"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/data.codec "0.1.0"]
                 [ring "1.4.0"]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.5.0"]
                 [net.mikera/imagez "0.12.0"]
                 [opencv/opencv "3.2.0"]
                 [opencv/opencv-native "3.2.0"]]
  :main ^:skip-aot ad-muter-ml.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :plugins [[lein-localrepo "0.5.3"]])

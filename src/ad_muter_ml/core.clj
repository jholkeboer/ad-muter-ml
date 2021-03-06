(ns ad-muter-ml.core
  (:gen-class)
  (:use hiccup.core)
  (:require
    [ring.adapter.jetty :as jetty]
    [ring.middleware.params :only [wrap-params]]
    [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
    [compojure.core :refer :all]
    [compojure.handler :as handler]
    [compojure.route :as route]
    [clojure.data.json :as json]
    [clojure.java.io :as io]
    [clojure.data.codec.base64 :as b64-codec]
    [ad-muter-ml.opencv :refer [get-match-score]]))

(defn image-file-path
  [time]
  (str "images/" time ".png"))

(defn write-image-to-file
  "Takes base64 string"
  [image-data time]
  (let [decoded-data (-> image-data
                      (clojure.string/split #"base64,")
                      (second)
                      (.getBytes)
                      (b64-codec/decode))
        path    (image-file-path time)]
    (io/copy decoded-data (java.io.File. path))))

(defroutes app-routes
  ;; Lighteight page for testing (small file size)
  (GET "/test_page" [] "Testpage.")

  ;; Receive an image from the chrome extension
  (POST "/receive_image" request
    (let [raw-body (json/read-str (ring.util.request/body-string request))
          time (get raw-body "time")
          image-data (get raw-body "image")]
      (write-image-to-file image-data time)
      (let [decision (get-match-score time)]
        (io/delete-file (image-file-path time))
        (println decision)
        {:status 200
         :body {:decision decision}})))

  (route/not-found "404 Route not found."))

(def app
  (-> app-routes
      (wrap-json-response)
      (ring.middleware.params/wrap-params)))

(defn -main
  "Start the server."
  [& args]
  (let [port (Integer. (get (System/getenv) "AD_MUTER_PORT" "5000"))]
    (jetty/run-jetty
      app
      {:port port})))

(ns ad-muter-ml.core
  (:gen-class)
  (:use hiccup.core)
  (:require
    [ring.adapter.jetty :as jetty]
    [ring.middleware.params :only [wrap-params]]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [clojure.data.json :as json]
    [clojure.java.io :as io]
    [clojure.data.codec.base64 :as b64-codec]))

(defn write-image-to-file
  "Takes base64 string"
  [image-data time]
  (let [decoded-data (-> image-data
                      (clojure.string/split #"base64,")
                      (second)
                      (.getBytes)
                      (b64-codec/decode))
        path    (str "images/" time  ".png")]
    (io/copy decoded-data (java.io.File. path))))

(defroutes app-routes
  ;; Lighteight page for testing (small file size)
  (GET "/test_page" [] "Testpage.")

  ;; Receive an image from the chrome extension
  (POST "/receive_image" request
    (let [raw-body (json/read-str (ring.util.request/body-string request))
          time (get raw-body "time")
          image-data (get raw-body "image")]
      (println (apply str (take 20 image-data)))
      (write-image-to-file image-data time)
      "Saved image."))

  (route/not-found "404 Route not found."))

(def app (ring.middleware.params/wrap-params app-routes))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [port (Integer. (get (System/getenv) "AD_MUTER_PORT" "5000"))]
    (jetty/run-jetty
      app
      {:port port})))

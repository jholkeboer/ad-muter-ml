(ns ad-muter-ml.core
  (:gen-class)
  (:use hiccup.core)
  (:require
    [ring.adapter.jetty :as jetty]
    [ring.middleware.params :only [wrap-params]]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [clojure.data.json :as json]))

(defroutes app-routes

  ;; Lighteight page for testing (small file size)
  (GET "/test_page" [] "Testpage.")

  ;; Receive an image from the chrome extension
  (POST "/receive_image" request
    (do
      (let [raw-body (json/read-str (ring.util.request/body-string request))
            image-time (get raw-body "time")
            image-data (get raw-body "image")
            path  (str "images/" image-time ".png")]
        (println (get raw-body "time"))
        (println (apply str (take 20 image-data)))
        (spit path image-data)
        (println (str "Saved " path))
        "Saved image.")))

  (route/not-found "404 Route not found."))

(def app (ring.middleware.params/wrap-params app-routes))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [port (Integer. (get (System/getenv) "AD_MUTER_PORT" "5000"))]
    (jetty/run-jetty
      app
      {:port port})))

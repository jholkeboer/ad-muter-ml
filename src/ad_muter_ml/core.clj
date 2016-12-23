(ns ad-muter-ml.core
  (:gen-class)
  (:use hiccup.core)
  (:require
    [ring.adapter.jetty :as jetty]
    [ring.middleware.params :only [wrap-params]]
    [compojure.core :refer :all]
    [compojure.route :as route]))

(defroutes app-routes
  (GET "/test_page" [] "Testpage.")
  ; (POST "/receive_image" [time] (do (println (str "Got time " time))) "Got request")
  (POST "/receive_image" request
    (do
      (println (str "Got params? " (:body request)))
      (let [body (ring.util.request/body-string request)]
        (println body))
      "Got request"))
  (route/not-found "404 Route not found."))

(def app (ring.middleware.params/wrap-params app-routes))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [port (Integer. (get (System/getenv) "AD_MUTER_PORT" "5000"))]
    (jetty/run-jetty
      app
      {:port port})))

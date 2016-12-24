(ns ad-muter-ml.ml
  (:gen-class)
  (:use hiccup.core)
  (:require
    [ring.adapter.jetty :as jetty]
    [ring.middleware.params :only [wrap-params]]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [clojure.data.json :as json]))

;; This namespace will be used for reading the images, and will have a function
  ;; which will send the classification for an image

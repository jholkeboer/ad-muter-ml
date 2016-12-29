(ns example.opencv
  (:import
    org.opencv.core.Core
    org.opencv.core.Mat
    org.opencv.core.MatOfRect
    org.opencv.core.Point
    org.opencv.core.Rect
    org.opencv.core.Scalar
    org.opencv.objdetect.CascadeClassifier
    org.opencv.imgcodecs.Imgcodecs
    org.opencv.videoio.Videoio)
  (:require
    [[clojure.pprint :only [pprint]]
     [clojure.reflect :as r]]))

#_(pprint (r/reflect Imgcodecs))

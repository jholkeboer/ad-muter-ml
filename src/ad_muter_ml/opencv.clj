(ns ad-muter-ml.opencv
  (:import
    org.opencv.core.Core
    org.opencv.core.Mat
    org.opencv.core.MatOfRect
    org.opencv.core.Point
    org.opencv.core.Rect
    org.opencv.core.Scalar
    org.opencv.objdetect.CascadeClassifier
    org.opencv.imgcodecs.Imgcodecs
    org.opencv.videoio.Videoio
    org.opencv.imgproc.Imgproc))

;; This must be run to use opencv
(clojure.lang.RT/loadLibrary org.opencv.core.Core/NATIVE_LIBRARY_NAME)

(defn load-image
  [filename]
  (Imgcodecs/imread
     (.getPath (clojure.java.io/resource filename))))

(def cnn (load-image "cnn-logo.png"))
(def threshold 0.95)

(defn match-with-method
  [img templ result method]
  (Imgproc/matchTemplate img templ result method))

(defn get-match-loc
  [result method]
  (let [min-max-result (Core/minMaxLoc result)]
    (if (contains? #{Imgproc/TM_SQDIFF Imgproc/TM_SQDIFF_NORMED} method)
      (.minVal min-max-result)
      (.maxVal min-max-result))))

(defn get-match-score
  [time]
  (let [result (Mat.)
        img-file (load-image (str time ".png"))
        method Imgproc/TM_CCORR_NORMED]
    (match-with-method img-file cnn result method)
    (let [match-loc (get-match-loc result method)]
      (println match-loc)
      (> match-loc threshold))))

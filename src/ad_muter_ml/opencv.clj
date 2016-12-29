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
    org.opencv.imgproc.Imgproc
    org.opencv.)
  (:require
    [[clojure.pprint :only [pprint]]
     [clojure.reflect :as r]]))

#_(pprint (r/reflect Imgcodecs))

;; This must be run to use opencv
#_(clojure.lang.RT/loadLibrary org.opencv.core.Core/NATIVE_LIBRARY_NAME)

(defn load-image
  [filename]
  (Imgcodecs/imread
     (.getPath (clojure.java.io/resource filename))))

#_(def not-ad (load-image "1482793956795.png"))
#_(def ad (load-image "1482794943137.png"))
#_(def cnn (load-image "cnn-logo.png"))
#_(def result (Mat.))


#_(def result-cols
    [img templ]
    (+ 1
      (- (.cols img) (.cols templ))))

#_(def result-rows
    [img templ]
    (+ 1
      (- (.rows img) (.rows templ))))

; Available match methods:
; Imgproc/TM_CCOEFF          Imgproc/TM_CCOEFF_NORMED
; Imgproc/TM_CCORR           Imgproc/TM_CCORR_NORMED
; Imgproc/TM_SQDIFF          Imgproc/TM_SQDIFF_NORMED

#_(Imgproc/matchTemplate ad cnn result Imgproc/TM_SQDIFF)

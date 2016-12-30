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
(def match-methods #{Imgproc/TM_CCOEFF          ; = 4
                     Imgproc/TM_CCORR           ; = 2
                     Imgproc/TM_SQDIFF          ; = 0
                     Imgproc/TM_CCOEFF_NORMED   ; = 5
                     Imgproc/TM_CCORR_NORMED    ; = 3
                     Imgproc/TM_SQDIFF_NORMED}) ; = 1


#_(Imgproc/matchTemplate ad cnn result Imgproc/TM_SQDIFF_NORMED)

; see http://docs.opencv.org/2.4/doc/tutorials/imgproc/histograms/template_matching/template_matching.html
#_(Core/normalize result result 0 1 Core/NORM_MINMAX -1 (Mat.))



 ; {:name minMaxLoc,
 ;  :return-type org.opencv.core.Core$MinMaxLocResult,
 ;  :declaring-class org.opencv.core.Core,
 ;  :parameter-types [org.opencv.core.Mat org.opencv.core.Mat],
 ;  :exception-types [],
 ;  :flags #{:public :static}}


(defn match-with-method
  [img templ result method]
  (Imgproc/matchTemplate img templ result method))
  ; (Core/normalize result result 0 1 Core/NORM_MINMAX -1))

(def ad-result (Mat.))
(match-with-method ad cnn ad-result Imgproc/TM_CCOEFF_NORMED)

(def cnn-result (Mat.))
(match-with-method not-ad cnn cnn-result Imgproc/TM_CCOEFF_NORMED)

(defn get-match-loc
  [result method]
  (let [min-max-result (Core/minMaxLoc result)]
    (if (contains? #{Imgproc/TM_SQDIFF Imgproc/TM_SQDIFF_NORMED} method)
      (.minLoc min-max-result)
      (.maxLoc min-max-result))))

(def ad-min-max (Core/minMaxLoc ad-result))
(def cnn-min-max (Core/minMaxLoc cnn-result))

(def min-max-result (Core/minMaxLoc result))
(get-match-loc ad-result Imgproc/TM_CCOEFF_NORMED)
(get-match-loc cnn-result Imgproc/TM_CCOEFF_NORMED)

(doseq [m methods]
  (def ad-result (Mat.))
  (match-with-method ad cnn ad-result m)
  (println "Method" m)
  (println "Ad result")
  (println (get-match-loc ad-result m))

  (def cnn-result (Mat.))
  (match-with-method not-ad cnn cnn-result m)
  (println "CNN result")
  (println (get-match-loc cnn-result m)))

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
  ; (:require
  ;   [[clojure.pprint :only [pprint]]
  ;    [clojure.reflect :as r]]))

#_(pprint (r/reflect Imgcodecs))

;; This must be run to use opencv
(clojure.lang.RT/loadLibrary org.opencv.core.Core/NATIVE_LIBRARY_NAME)

(defn load-image
  [filename]
  (Imgcodecs/imread
     (.getPath (clojure.java.io/resource filename))))

#_(def not-ad (load-image "1482793956795.png"))
#_(def ad (load-image "1482794943137.png"))
(def cnn (load-image "cnn-logo.png"))
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

; (def ad-result (Mat.))
; (match-with-method ad cnn ad-result Imgproc/TM_CCOEFF_NORMED)
;
; (def cnn-result (Mat.))
; (match-with-method not-ad cnn cnn-result Imgproc/TM_CCOEFF_NORMED)

(defn get-match-loc
  [result method]
  (let [min-max-result (Core/minMaxLoc result)]
    (if (contains? #{Imgproc/TM_SQDIFF Imgproc/TM_SQDIFF_NORMED} method)
      (.minLoc min-max-result)
      (.maxLoc min-max-result))))

; (def ad-min-max (Core/minMaxLoc ad-result))
; (def cnn-min-max (Core/minMaxLoc cnn-result))

; (def min-max-result (Core/minMaxLoc result))
; (get-match-loc ad-result Imgproc/TM_CCOEFF_NORMED)
; (get-match-loc cnn-result Imgproc/TM_CCOEFF_NORMED)

#_(doseq [m methods]
    (def ad-result (Mat.))
    (match-with-method ad cnn ad-result m)
    (println "Method" m)
    (println "Ad result")
    (println (get-match-loc ad-result m))

    (def cnn-result (Mat.))
    (match-with-method not-ad cnn cnn-result m)
    (println "CNN result")
    (println (get-match-loc cnn-result m)))

(defn get-match-score
  [time]
  (let [result (Mat.)
        img-file (load-image (str time ".png"))
        method Imgproc/TM_CCOEFF]
    (match-with-method img-file cnn result method)
    (get-match-loc result method)))

; Method 0
; Ad result
; #object[org.opencv.core.Point 0x1c36f0ee {955.0, 255.0}]
; CNN result
; #object[org.opencv.core.Point 0x384f1b9c {924.0, 513.0}]
; Method 1
; Ad result
; #object[org.opencv.core.Point 0x3791a07 {783.0, 300.0}]
; CNN result
; #object[org.opencv.core.Point 0x56e8f7c2 {924.0, 513.0}]
; Method 4
; Ad result
; #object[org.opencv.core.Point 0x7df2dc71 {1.0, 268.0}]
; CNN result
; #object[org.opencv.core.Point 0x3d21bba8 {924.0, 513.0}]
; Method 3
; Ad result
; #object[org.opencv.core.Point 0x76433729 {734.0, 577.0}]
; CNN result
; #object[org.opencv.core.Point 0x678498c4 {924.0, 513.0}]
; Method 2
; Ad result
; #object[org.opencv.core.Point 0xc7ed111 {0.0, 376.0}]
; CNN result
; #object[org.opencv.core.Point 0x3cb2c7cd {924.0, 513.0}]
; Method 5
; Ad result
; #object[org.opencv.core.Point 0x53a7550d {787.0, 470.0}]
; CNN result
; #object[org.opencv.core.Point 0x10a6a505 {924.0, 513.0}]

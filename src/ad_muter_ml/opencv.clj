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

(def match-methods #{Imgproc/TM_CCOEFF          ; = 4
                     Imgproc/TM_CCORR           ; = 2
                     Imgproc/TM_SQDIFF          ; = 0
                     Imgproc/TM_CCOEFF_NORMED   ; = 5
                     Imgproc/TM_CCORR_NORMED    ; = 3
                     Imgproc/TM_SQDIFF_NORMED}) ; = 1

(def normed-match-methods #{Imgproc/TM_CCOEFF_NORMED
                            Imgproc/TM_CCORR_NORMED
                            Imgproc/TM_SQDIFF_NORMED})

; need a map to see the names for testing since these are really just ints
(def match-method-names
  {Imgproc/TM_SQDIFF "TM_SQDIFF"
   Imgproc/TM_SQDIFF_NORMED "TM_SQDIFF_NORMED"
   Imgproc/TM_CCORR "TM_CCORR"
   Imgproc/TM_CCORR_NORMED "TM_CCORR_NORMED"
   Imgproc/TM_CCOEFF "TM_CCOEFF"
   Imgproc/TM_CCOEFF_NORMED "TM_CCOEFF_NORMED"})

(defn load-image
  [filename]
  (Imgcodecs/imread
     (.getPath (clojure.java.io/resource filename))))

(def cnn (load-image "cnn-logo.png"))
(def fox (load-image "fox-logo.png"))

(def last-three-scores (atom '()))

(defn update-scores
  [scores new]
  (take 3
    (conj scores new)))

(defn match-with-method
  [img templ result method]
  (Imgproc/matchTemplate img templ result method))

(defn get-match-loc
  [result method]
  (let [min-max-result (Core/minMaxLoc result)]
    (if (contains? #{Imgproc/TM_SQDIFF Imgproc/TM_SQDIFF_NORMED} method)
      (.minVal min-max-result)
      (.maxVal min-max-result))))

(defn test-methods
  "Print the maxVal and minVals for each method to evaluate "
  [img-file]
  (doseq [m normed-match-methods]
    (println (get match-method-names m))
    (let [result (Mat.)]
      (match-with-method img-file cnn result m)
      ; (match-with-method img-file fox result m)
      (let [min-max-result (Core/minMaxLoc result)]
        (if (contains? {Imgproc/TM_SQDIFF Imgproc/TM_SQDIFF_NORMED} m)
          (println (.minVal min-max-result))
          (println (.maxVal min-max-result)))))))

(defn get-match-score
  [time]
  (let [result (Mat.)
        img-file (load-image (str time ".png"))]
    (test-methods img-file)
    true))
    ; (match-with-method img-file cnn result method)
    ; (let [match-loc (get-match-loc result method)]
    ;   (println match-loc))))
      ; (swap! last-three-scores update-scores (.x match-loc)))))
      ; (println @last-three-scores)
      ; (apply = @last-three-scores))))





;;;;;;;;;;;;;;;;;;;;;;;;;
;; Dev notes below
;;;;;;;;;;;;;;;;;;;;;;;;;

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


#_(def not-ad (load-image "1482793956795.png"))
#_(def ad (load-image "1482794943137.png"))
#_(def result (Mat.))

#_(def result-cols
    [img templ]
    (+ 1
      (- (.cols img) (.cols templ))))

#_(def result-rows
    [img templ]
    (+ 1
      (- (.rows img) (.rows templ))))



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

; (def ad-result (Mat.))
; (match-with-method ad cnn ad-result Imgproc/TM_CCOEFF_NORMED)
;
; (def cnn-result (Mat.))
; (match-with-method not-ad cnn cnn-result Imgproc/TM_CCOEFF_NORMED)

; (def ad-min-max (Core/minMaxLoc ad-result))
; (def cnn-min-max (Core/minMaxLoc cnn-result))

; (def min-max-result (Core/minMaxLoc result))
; (get-match-loc ad-result Imgproc/TM_CCOEFF_NORMED)
; (get-match-loc cnn-result Imgproc/TM_CCOEFF_NORMED)

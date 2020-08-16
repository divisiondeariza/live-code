(ns tutorial.core
  (:require [clojure.repl :refer :all]
            [overtone.live :as overtone]
            [quil.core :as q]
            [quil.middleware :as m]
            [shadertone.tone :as t]
            [leipzig.melody :refer [all bpm is phrase tempo then times where with]]
            [leipzig.live :as live]
            [leipzig.scale :as scale]
            [leipzig.chord :as chord]
            [tutorial.playall :refer [play-all]]
       )
)

(apropos +)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; (play-all)

;; (defn setup []
;;   (let [myAnimation (gifAnimation.Gif. (quil.applet/current-applet) "tenor.gif")
;;         init-value (.play myAnimation)] ;; awful way to get side effects

;;     (hash-map :myAnimation myAnimation ))
;; )


;; (defn draw [state]
;;  (q/image (:myAnimation state) 10 10)
;; )


;; (q/defsketch golden-ratio-flowe
;;   :host "host"
;;   :size [500 500]
;;   :setup setup
;;   :draw draw
;;   :middleware [m/fun-mode])

;; (overtone/stop)

;; (def melody
;;          ; Row,  row,  row   your  boat
;;   (phrase [3/3   3/3   2/3   1/3   3/3]
;;           [  0     0     0     1     2]))

;; (overtone/definst beep [freq 440 dur 1.0]
;;   (-> freq
;;       overtone/saw
;;       (* (overtone/env-gen (overtone/perc 0.05 dur) :action overtone/FREE))))

;; (defmethod live/play-note :default [{midi :pitch seconds :duration}]
;;   (-> midi overtone/midi->hz (beep seconds)))

;; (->>
;;   melody
;;   (tempo (bpm 90))
;;   (where :pitch (comp scale/C scale/major))
;;   live/play)

;; (def reply "The second bar of the melody."
;;          ; Gent -ly  down the stream
;;   (phrase [2/3  1/3  2/3  1/3  6/3]
;;           [  2    1    2    3    4]))

;; (def bass "A bass part to accompany the melody."
;;   (->> (phrase [1  1 2]
;;                [0 -3 0])
;;        (all :part :bass)))

;; (defmethod live/play-note :bass [{midi :pitch}]
;;   ; Halving the frequency drops the note an octave.
;;   (-> midi overtone/midi->hz (/ 2) (beep 0.5)))

;; (->>
;;   bass
;;   (then (with bass melody))
;;   (then (with bass melody reply))
;;   (then (times 2 bass))
;;   (tempo (bpm 90))
;;   (where :pitch (comp scale/C scale/minor))
;;   live/play)





;; (phrase [3/3 3/3 2/3 1/3 3/3] [0 0 0 1 2])

;; (->> (phrase [3/3 3/3 2/3 1/3 3/3] [0 0 0 1 2])
;;      (where :time inc))

;; (defmethod live/play-note :melody [{midi :pitch}]
;;   (some-> midi overtone/midi->hz beep))

;; (def boring-scale
;;   (->> (phrase
;;         [4 4]
;;         [(-> chord/triad (chord/root 3))
;;          (-> chord/triad (chord/inversion 2) (chord/root 4))])
;;        (all :part :melody)
;;        (where :pitch (comp scale/C scale/minor)))

;; )

;; (live/jam (var boring-scale))

;; (live/stop)

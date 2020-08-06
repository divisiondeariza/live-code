(ns tutorial.instruments
 (:require [clojure.repl :refer :all]
            [overtone.live  :refer :all]
            [overtone.inst.sampled-piano :refer :all]
            [tutorial.utils :refer [freesound-inst]]
            )
  )

(swap! live-config assoc-in [:sc-args :max-buffers] 1024)

;;;;;;;;;;;;;;;;;; Drumkit ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(definst snare []
  (pan2 (freesound-inst 35608))
  )

(definst kick []
  (pan2 (freesound-inst 2086))
  )

(definst close-hat []
  (pan2 (freesound-inst 802))
  )

(definst open-snare []
  (pan2 (freesound-inst 16309))
  )

(definst open-hat []
  (pan2 (freesound-inst 26657))
  )

(definst clap []
  (pan2 (freesound-inst 48310))
  )

;;;;;;;;;;;;;;;;;; Others ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn piano2 [note]
  (sampled-piano note :attack 0 :level 0.5 :sustain 0.3 :decay 0.5 :curve -4))

(definst saw-wave [freq 440 attack 0.01 sustain 0.2 release 0.1 vol 0.4]
  (* (env-gen (env-lin attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))

(definst pick [note 60]
  (let [freq (midicps note)
        src (+ (pluck (* (pink-noise) (env-gen (perc 0.001 0.5) :action FREE)) 1 1 (/ 4 freq))
               (lf-tri (* freq 0.025))
               (sin-osc (/ freq 2))
               (sin-osc (/ freq 3)))
        filt (env-gen (perc 0.001 2) :action FREE)]
    (lpf (* 0.5 (* src filt)) 1250)))

(adsr)
(defn saw2 [music-note]
  (saw-wave (midi->hz (note music-note))))

(ns tutorial.core
  (:require [clojure.repl :refer :all]
            [overtone.live  :refer :all]
            [overtone.inst.sampled-piano :refer :all]
            [quil.core :as q]
            [tutorial.utils :refer [freesound-inst play-chord play-on]]
            )
)
(swap! live-config assoc-in [:sc-args :max-buffers] 1024)

(def metro (metronome 90))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; UTILS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



;;;;;;;;;;;;;;;;;;;;;;;;;;;; INSTRUMENTS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(definst snare []
  (out 0 (* 0.7 (freesound-inst 35608)))
  (out 1 (* 0.8 (freesound-inst 35608)))
  ;(play-buf 2  (load-sample (freesound-path 35608)) :action FREE )
  )

(definst kick []
  (out 0 (* 0.7 (freesound-inst 2086)))
  (out 1 (* 0.7 (freesound-inst 2086)))
  ;(play-buf 2 (load-sample (freesound-path 2086)) :action FREE )
 )

(definst close-hat []
  (out 0 (* 0.3 (freesound-inst 802)))
  (out 1 (* 0.3 (freesound-inst 802)))
)

(definst c-hat [amp 0.8 t 0.04]
  (let [env (env-gen (perc 0.001 t) 1 1 0 1 FREE)
        noise (white-noise)
        sqr (* (env-gen (perc 0.01 0.04)) (pulse 880 0.2))
        filt (bpf (+ sqr noise) 9000 0.5)]
    (* amp env filt)))

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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; MUSIK  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn dembow [metro number-of-cycle]
  (let [beat        (metro)

        kick-times  (range 8)

        snare-times (concat
                     ;; basic sequence
                     (for [bts [0 2 4 6] ts [0.75 1.5]] (+ bts ts))
                     [ (+ 2 1.75) (+ 6 1.75) ]
                     )

        c-hat-times (concat
                     ;; basic sequence
                     (for [bts [0 2 4 6] ts [0.25 0.75]] (+ bts ts))
                          [ (+ 4 1.25) (+ 4 1.75) ]
                     )

        ]

    (play-on metro beat kick-times kick)
    (play-on metro beat snare-times snare)
    (play-on metro beat c-hat-times close-hat)
    (apply-by (metro (+ 8 beat)) #'dembow metro (inc number-of-cycle) []))
  )

(defn bass-seq [metro]
  (let [beat (metro)]
    (play-on metro beat [0 2 4 6] (fn [] (pick 60)) )
    (play-on metro beat [7] (fn [] (pick (- 67 12))) )
    (apply-by (metro (+ 8 beat)) #'bass-seq metro []))
  )

(defn chords-seq [metro]
  (let [beat (metro)
        ]
    (doseq [ [ch t] (concat
                     (map list
                          (apply concat
                           (repeat 2 [[:C4 :major] [:A4 :minor] [:D4 :minor] [:G4 :major7]]))
                           [0 1.5 3 4 8 9.5 11 12])
                     [[[:G4 :dim7] 13.5]]
                     )
            ]
           (play-on metro beat [t]
                    (fn [] (play-chord (apply chord ch) piano2)))
           )
    (apply-by (metro (+ 16 beat)) #'chords-seq metro [])
    )
)


(defn play-all []
  (stop)
  (let [metro (metronome 90)]
    (chords-seq metro)
    (bass-seq metro)
    (dembow metro 0)
  )
  ;(apply-by (metro (+ 2 beat)) #'player (+ 2 beat) [])
  )

(play-all)

(stop)
(chord :C4 :major)
(play-chord (chord :C4 :major) sampled-piano)

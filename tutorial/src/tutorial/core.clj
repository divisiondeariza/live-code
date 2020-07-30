(ns tutorial.core)

(require '[clojure.repl :refer :all])
(use 'overtone.live)
(swap! live-config assoc-in [:sc-args :max-buffers] 1024)

(def metro (metronome 90))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; UTILS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn play-on [metro first-beat times-after-beat instrument]
  (at (metro
      (+ first-beat (first  times-after-beat)))
      (instrument))
  (when (> (count (rest times-after-beat)) 0)
    (play-on metro first-beat (rest times-after-beat) instrument)
    ))

(defn if-nth-times [n actual-count then else?]
  (if (= 0 (mod actual-count n)) then else?)
  )

(defn freesound-inst [id]
  (play-buf 1  (load-sample (freesound-path id)) :action FREE  ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;; INSTRUMENTS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(definst snare []
  (play-buf 2  (load-sample (freesound-path 35608)) :action FREE ))

(definst kick []
  (play-buf 2 (load-sample (freesound-path 2086)) :action FREE ))

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

(definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
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


(defn saw2 [music-note]
  (saw-wave (midi->hz (note music-note))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; MUSIK  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn play-chord [a-chord]
  (doseq [note a-chord] (saw2 note)))

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
    (play-on metro beat [7] (fn [] (pick 62)) )
    (apply-by (metro (+ 8 beat)) #'bass-seq metro []))
  )

(defn play-all []
  (stop)
  (let [metro (metronome 90)]
    (bass-seq metro)
    (dembow metro 0)
  )
  ;(apply-by (metro (+ 2 beat)) #'player (+ 2 beat) [])
  )

(play-all)

(stop)

(play-chord (chord :C4 :major))

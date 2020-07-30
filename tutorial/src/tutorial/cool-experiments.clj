(ns tutorial.core)

(require '[clojure.repl :refer :all])
(use 'overtone.live)
(swap! live-config assoc-in [:sc-args :max-buffers] 1024)


(definst kick [freq 120 dur 0.3 width 0.5]
  (let [freq-env (* freq (env-gen (perc 0 (* 0.99 dur))))
        env (env-gen (perc 0.01 dur) 1 1 0 1 FREE)
        sqr (* (env-gen (perc 0 0.01)) (pulse (* 2 freq) width))
        src (sin-osc freq-env)
        drum (+ sqr (* env src))]
    (compander drum drum 0.2 1 0.1 0.01 0.01)))

(kick)

(definst c-hat [amp 0.8 t 0.04]
  (let [env (env-gen (perc 0.001 t) 1 1 0 1 FREE)
        noise (white-noise)
        sqr (* (env-gen (perc 0.01 0.04)) (pulse 880 0.2))
        filt (bpf (+ sqr noise) 9000 0.5)]
    (* amp env filt)))


(c-hat)

(def metro (metronome 90))

(metro)
(metro 100)

(defn player [beat]
  (dembow beat)
  (apply-by (metro (+ 2 beat)) #'player (+ 2 beat) []))

(defn dembow [beat]
  (at (metro beat) (kick))
  (at (metro (+ 0.75 beat)) (snare))
  (at (metro (+ 1 beat)) (kick))
  (at (metro (+ 1.5 beat)) (snare))
  (at (metro (+ 1.75 beat))
      (when (= 0 (mod beat 8)) (c-hat 1.2)))
)

(player (metro))


(definst snare []
  (play-buf 1
            (load-sample (freesound-path 35608) )))

(definst kick []
  (play-buf 1
            (load-sample (freesound-path 344757) )))

(stop)

(snare)

(definst o-hat [amp 0.8 t 0.5]
  (let [env (env-gen (perc 0.001 t) 1 1 0 1 FREE)
        noise (white-noise)
        sqr (* (env-gen (perc 0.01 0.04)) (pulse 880 0.2))
        filt (bpf (+ sqr noise) 9000 0.5)]
    (* amp env filt)))

(defn swinger [beat]
  (at (metro beat) (o-hat))
  (at (metro (inc beat)) (c-hat))
  (at (metro (+ 1.65 beat)) (c-hat))
  (apply-at (metro (+ 2 beat)) #'swinger (+ 2 beat) []))

(swinger (metro))

(Thread/sleep)

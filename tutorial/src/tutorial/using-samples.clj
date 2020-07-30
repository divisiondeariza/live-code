(ns tutorial.core)

(require '[clojure.repl :refer :all])
(use 'overtone.live)
(swap! live-config assoc-in [:sc-args :max-buffers] 1024)

(demo (* (env-gen (lin 0.1 1 1 0.25) :action FREE) (sin-osc)))


(demo (* 0.25 (linen (impulse 0) 0.1 1 1.9 :action FREE) (sin-osc)))

(demo (let [dur 1
            env (sin-osc:kr (/ 1 (* 0.1 dur)))]
        (line:kr 0 4 dur :action FREE)
        (* env (sin-osc 220))))

(definst spooky-house [freq 440 width 0.2
                         attack 0.3 sustain 4 release 0.3
                         vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (sin-osc (+ freq (* 20 (lf-pulse:kr 0.5 0 width))))
     vol))

(spooky-house)

(definst noisey [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (pink-noise) ; also have (white-noise) and others...
     vol))

(noisey)

(defsynth sin1 [freq 440]
  (out 0 (sin-osc freq)))

(def flute (sample "/home/emmanuel/live-code/samples/homero_profesor.wav"))

(flute)

(def homero-profesor (load-sample "~/live-code/samples/homero_profesor.wav" :start (* 44100 10) :force 1))

(defsynth reverb-on-left []
  (let [dry (play-buf 1 homero-profesor)
	wet (free-verb dry 1)]
    (out 0 [wet dry])))

(reverb-on-left)

(kill reverb-on-left)

(def snare (freesound 26903))
(snare)

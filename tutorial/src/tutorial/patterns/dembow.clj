(ns tutorial.patterns.dembow
  (:require [tutorial.instruments :as instruments]
            [overtone.inst.drum :as o-drums]
            [leipzig.live :as live]
            [leipzig.melody :refer :all]
            )
  )

(def drumkit {:kick  instruments/kick
              :snare instruments/snare
              :close-hat instruments/close-hat
              :clap instruments/clap
              })

(def basic-pattern {:kick  [1 0 0 0 1 0 0 0]
                    :snare [0 0 0 1 0 0 1 0]
                    })

(def double-snare-pattern (-> basic-pattern
                              (assoc :snare [0 0 0 1 0 0 1 1]))
  )

(def combined-pattern (merge-with into
                                  basic-pattern
                                  double-snare-pattern))

(def claps-hats (-> combined-pattern
                    (assoc :close-hat [1 1 1 0 0 0 0 0 1 1 1 0 1 0 1 0])
                    (assoc :clap      [1 0 1 1 0 1 0 1 0 1 1 0 1 0 0 0])))

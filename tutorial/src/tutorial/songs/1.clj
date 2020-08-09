(ns tutorial.songs.1
  (:require [tutorial.instruments :as instruments]
            [tutorial.patterns.dembow :as dembow]
            [tutorial.utils :refer [pattern2phrase pattern-map2phrase]]
            [leipzig.live :as live]
            [leipzig.melody :refer :all]
            [leipzig.chord :as chord]
            [overtone.music.pitch :as pitch]
            [sonic-pi.atmos :as atmos]
            )
  )


(def chords-map (->>[[:A3 :minor 4]
                     [:C3 :major 4]
                     [:F3 :major 4]
                     [:G3 :major 4]]
                    ))

(def piano-progression (->> chords-map
                            (map (partial apply #(vector %3 (pitch/chord %1 %2))))
                            (map (partial apply #(repeat %1 [1 %2])))
                            (apply concat)
                            (apply map vector)
                            (apply phrase)
                            (all :part :piano)
                            (where :time #(+ 0.5 %))
                            (all :duration 0.5)
                            )
  )

(def sequential-dembow-phrase (->> (mapthen pattern-map2phrase
                                            (flatten [dembow/combined-pattern
                                                      (repeat 3 dembow/claps-hats)])
                                            )
                                   (all :part :dembow)
                                   ))


(defmethod live/play-note :dembow [{drum-key :drum}]
  (when-let [ drum (get dembow/drumkit drum-key)]
    (drum))
  )

(defmethod live/play-note :piano [{pitch :pitch duration :duration}]
  (when pitch
    (instruments/piano2 pitch)))


(def main-song (->> sequential-dembow-phrase
                    (with piano-progression)
                    (tempo (bpm 100))))



(live/jam (var main-song))


(live/stop)

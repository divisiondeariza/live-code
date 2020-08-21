(ns tutorial.songs.1
  (:require   [clojure.repl :refer :all]
              [tutorial.instruments :as instruments]
              [tutorial.patterns.dembow :as dembow]
              [tutorial.utils :refer [pattern2phrase pattern-map2phrase chordvector2arpeggio chordvector2strum chordvector2chord]]
              [leipzig.live :as live]
              [leipzig.melody :refer :all]
              [leipzig.chord :as chord]
              [overtone.music.pitch :as pitch]
              [sonic-pi.atmos :as atmos]
              [sonic-pi.basic :as basic]
              [tutorial.note-sequences :refer [phrase2noteseq coconet]]
              [overtone.osc :as osc]))


;;;;;;;;;;;;;;;;;;;;;;;;; Music Structures ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def chords-map { :intro [[:C3  :minor 2]
                          [:Eb3 :major 2]
                          [:F3  :minor 2]
                          [:Ab3 :major 2]]
                 :cliche [[:B4  :major 2]
                          [:F#4 :major 1]
                          [:G#4 :minor 3]
                          [:E3  :major 2]]})

(defn remap [m f]
  (into {} (for [[k v] m] [k (f v)])))

(def piano-progression (remap chords-map (fn [ch] (->> ch
                                                      (map #(chordvector2strum % 1 0.5))
                                                      (reduce #(then %2 %1))
                                                      (all :part :piano)))))

(def arpeggio-map (remap chords-map (fn [ch] (->> ch
                                               (map #(chordvector2arpeggio % 1/4
                                                                           (fn [chord] (concat chord (reverse chord)))))
                                               (reduce #(then %2 %1))
                                               (all :part :piano)))))

(def bass-map (remap chords-map (fn [ch] (->> ch
                                           (map #(chordvector2arpeggio % 1/4
                                                                       (fn [chord] (for [n [0 -1 1 2]] (nth chord n nil)))))
                                           (reduce #(then %2 %1))
                                           (where :pitch #(- % 12))
                                           (all :part :bass)))))

(def sequential-dembow-phrase (->> (mapthen pattern-map2phrase
                                            (flatten [(repeat 2 dembow/claps-hats)]))
                                   (all :part :dembow)))



;;;;;;;;;;;;;;;;;;;;;;;;; Instruments & Actions ;;;;;;;;;;;;;;;;;;;;;;;;;

(defmethod live/play-note :dembow [{drum-key :drum}]
  (when-let [ drum (get dembow/drumkit drum-key)]
    (drum))
  )

(defmethod live/play-note :coconet [{pitch :pitch duration :duration instrument :instrument}]
  (when pitch
    (case instrument
      ;(0)   (instruments/traditional-pluck :note pitch :amp 1)
      (1)   (instruments/traditional-blade :note pitch :amp 0.8)
      (2)   (instruments/piano pitch :level 0.5 )
      (3)   (instruments/basic-fm :note pitch :amp 0.5 :release (* duration ))
      (nil? instrument)
      )))

(defmethod live/play-note :piano [{pitch :pitch duration :duration}]
  (when pitch
    (instruments/piano pitch :level 0.5)))

(defmethod live/play-note :bass [{pitch :pitch duration :duration}]
  (when pitch
    (instruments/basic-fm :note pitch :amp 0.3 :sustain duration :release 0)))

;;;;;;;;;;;;;;;;;;;;;;  Magenta Models ;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def coconet-seq (remap (merge-with with bass-map arpeggio-map)
                        (fn [ch] (->> ch
                               coconet
                               (all :part :coconet)
                               (sort-by :time)
                               (apply vector)))))

;;;;;;;;;;;;;;;;;;;;;  Main Song ;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def main-song
  (->> (apply with (map :intro [coconet-seq piano-progression]))
       (with  sequential-dembow-phrase)
       (tempo (bpm 90))) )

(def main-song (->> coconet-seq
                    (tempo (bpm 90))))


;;;;;;;;;;;;;;;;;;;;;;; Jam & Recording ;;;;;;;;;;;;;;;;;;;;;;;;;;;

(live/jam (var main-song))


(live/stop)

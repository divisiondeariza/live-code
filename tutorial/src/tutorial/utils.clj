(ns tutorial.utils
  (:require  [overtone.live  :refer :all]
             [leipzig.melody :refer :all]
             [overtone.music.pitch :as pitch]))


(defn chordvector2arpeggio
  "Generates an arpeggio from a chord vector"
  [chordvector notes-length]
  (let [root        (first  chordvector)
        chord-name  (second chordvector)
        bars        (last chordvector)
        total-notes (/ bars notes-length)
        durations   (take total-notes (repeat notes-length))]
    (->> (pitch/chord root chord-name)
         (sort)
         (cycle)
         (take total-notes)
         (vector durations)
         (apply phrase)
         )
    )
  )

(defn pattern2phrase
  "converts a pattern into a leipzig phrase.
   e.g. (pattern2phrase 0.25 [:inst [1 0 0 1])
  should return:
  [{:time 0,   :duration 0.25, :drum :inst}
   {:time 0.75 :duration 0.25, :drum :inst}]"

  ([length [instrument pattern]]
   (pattern2phrase length [instrument pattern] 0))

  ([length [instrument pattern] offset]
   (->> pattern
        (map (fn [step]
               (if (vector? step)
                 (pattern2phrase (/ length (count step)) [instrument step] (+ offset))
                 {:duration length, :drum (if (= step 0) nil instrument)})))
        (flatten)
        (mapthen (fn [step] [(assoc step :time 0)]))
        ;; Times don't sum up exactly with non 2 power rhythms, let's hope this doesn't break anything.
        )))


(defn pattern-map2phrase
  [pattern-map]
  (->> pattern-map
       (map (partial pattern2phrase 0.25))
       (apply with)
       )
  )

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

(defn play-chord [a-chord inst]
  (doseq [note a-chord] (inst note)))

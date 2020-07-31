(ns tutorial.utils
  (:require  [overtone.live  :refer :all]))

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

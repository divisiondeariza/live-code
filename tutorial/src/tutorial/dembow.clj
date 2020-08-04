(ns tutorial.dembow
  (:require [tutorial.instruments :as instruments]
            [leipzig.live :as live]
            [leipzig.melody :refer :all]
            )
 )

(def kit {:kick  instruments/kick
          :snare instruments/snare})

(defn pattern2phrase
  ([length [instrument pattern]] (pattern2phrase length [instrument pattern] 0))
  ([length [instrument pattern] offset]
    (->> (map-indexed(fn [index note] [index note]) pattern)
         (filter (fn [[index note]] (not= note 0)))
         (map (fn
                [[index note]]
                (let [time (+ (* (- index 1) length) offset)]
                  (if (vector? note)
                    (pattern2phrase (/ length (count note)) [instrument note] time)
                    {:time time
                     :duration 1
                     :drum instrument
                     :part :dembow}
                   )
                )
              )
              )
         (flatten)
   )
  )
 )

(def dembow-pattern (->>
                     {:kick  [1 0 0 0 1 0 0 0]
                      :snare [0 0 0 1 0 0 1 0]}

                     (map (partial pattern2phrase 0.25))
                     (apply with)
                     )
  )

(defmethod live/play-note :dembow [note]
  (when-let [drum (-> (get kit (:drum note)))]
    (drum)))

(->> (times 2 dembow-pattern)
       (tempo (bpm 90))
       live/play)

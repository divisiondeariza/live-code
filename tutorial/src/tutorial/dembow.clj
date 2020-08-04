(ns tutorial.dembow
  (:require [tutorial.instruments :as instruments]
            [leipzig.live :as live]
            [leipzig.melody :refer :all]
            )
 )

(def kit {:kick  instruments/kick
          :snare instruments/snare})

(defn pattern2phrase [[instrument pattern]]
  (->> (map-indexed(fn [index note] [index note]) pattern)
       (filter (fn [[index note]] (not= note 0)))
       (map (fn [[index note]]
              {:time (* index 0.25)
               :duration 1
               :drum instrument
               :part :dembow}))
     )
  )



(def dembow-pattern (->>
                     {:kick  [1 0 0 0 1 0 0 0]
                      :snare [0 0 1 0 0 0 0 1]}

                     (map pattern2phrase)
                     (apply with)
                     )
  )

dembow-pattern

(defmethod live/play-note :dembow [note]
  (when-let [drum (-> (get kit (:drum note)))]
    (drum)))

(->> dembow-pattern
       (tempo (bpm 110))
       live/play)



( dembow-pattern)




(first dembow-pattern)





dembow-pattern

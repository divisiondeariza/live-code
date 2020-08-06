(ns tutorial.patterns.dembow
  (:require [tutorial.instruments :as instruments]
            [overtone.inst.drum :as o-drums]
            [leipzig.live :as live]
            [leipzig.melody :refer :all]
            [tutorial.utils :refer [pattern2phrase]]
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

;; (def dembow-phrase (->> dembow-claps-hats
;;                               (map (partial pattern2phrase 0.25))
;;                               (apply with)
;;                               (all :part :dembow)
;;                               ))


;; (def dembow-pattern (->>
;;                      {:kick  (take 16 (cycle [1 0 0 0]))
;;                       :snare (concat [0 0 0 1 0 0 1 0]
;;                                      [0 0 0 1 0 0 1 1])
;;                       :close-hat (take 16 (cycle (concat
;;                                                   [1 1 1 0 0 0 0 0]
;;                                                   [1 1 1 0 1 0 1 0]
;;                                                   )))
;;                       :clap [1 0 1 1 0 1 0 1 0 1 1 0 1]}

;;                      (map (partial pattern2phrase 0.25))
;;                      (apply with)
;;                      (all :part :dembow)
;;                      )
;;   )

;; (defmethod live/play-note :dembow [note]
;;   (when-let [drum (-> (get kit (:drum note)))]
;;     (drum)))
;; ()
;; (->> ()
;;        (tempo (bpm 90))
;;        live/play)

;; (def dbpt (->> basic-dembow-phrase
;;                (tempo (bpm 90))
;;                )
;;   )

;; (live/jam (var dbpt))

;; (live/stop)

;; (var dbpt)



;; dembow-double-snare-pattern

(ns tutorial.playall
  (:require [clojure.repl :refer :all]
            [overtone.live  :refer :all]
            [tutorial.utils :refer [play-chord play-on]]
            [tutorial.instruments :refer :all]
       )
  )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; MUSIK  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn dembow [metro number-of-cycle]
  (let [beat        (metro)

        kick-times  (range 8)

        snare-times (concat
                     ;; basic sequence
                     (for [bts [0 2 4 6] ts [0.75 1.5]] (+ bts ts))
                     [ (+ 2 1.75) (+ 6 1.75) ]
                     )

        c-hat-times (concat
                     ;; basic sequence
                     (for [bts [0 2 4 6] ts [0.25 0.75]] (+ bts ts))
                          [ (+ 4 1.25) (+ 4 1.75) ]
                     )

        ]

    (play-on metro beat kick-times kick)
    (play-on metro beat snare-times snare)
    (play-on metro beat c-hat-times close-hat)
    (apply-by (metro (+ 8 beat)) #'dembow metro (inc number-of-cycle) []))
  )

(defn bass-seq [metro]
  (let [beat (metro)]
    (play-on metro beat [0 2 4 6] (fn [] (pick 60)) )
    (play-on metro beat [7] (fn [] (pick (- 67 12))) )
    (apply-by (metro (+ 8 beat)) #'bass-seq metro []))
  )

(defn chords-seq [metro]
  (let [beat (metro)
        ]
    (doseq [ [ch t] (concat
                     (map list
                          (apply concat
                           (repeat 2 [[:C4 :major] [:A4 :minor] [:D4 :minor] [:G4 :major7]]))
                           [0 1.5 3 4 8 9.5 11 12])
                     [[[:G4 :dim7] 13.5]]
                     )
            ]
           (play-on metro beat [t]
                    (fn [] (play-chord (apply chord ch) piano2)))
           )
    (apply-by (metro (+ 16 beat)) #'chords-seq metro [])
    )
)

(defn play-all []
  (stop)
  (let [metro (metronome 90)]
    (chords-seq metro)
    (bass-seq metro)
    (dembow metro 0)
  )
)

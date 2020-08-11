(ns tutorial.note-sequences
  (:require [clojure.repl :refer :all]
            [leipzig.live :as live]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.walk :refer [keywordize-keys]]
   ))

(defn phrase2noteseq [phrase]
  (let [stepsPerQuarter 4]
      {:quantizationInfo {:stepsPerQuarter stepsPerQuarter}
       :notes (map (fn [note] (let [quantizedStartStep (-> note :time (* stepsPerQuarter))
                                   quantizedEndStep   (-> note :duration (* stepsPerQuarter)
                                                          (+  quantizedStartStep))]
                               {:pitch (:pitch note)
                                :quantizedStartStep quantizedStartStep
                                :quantizedEndStep quantizedEndStep}))
                   phrase)}))

(defn coconet []
  (->> (client/get "http://localhost:3000/coconet" {:accept :json})
       (:body)
       (json/read-str)
       (keywordize-keys)
       (magenta2leipzig)
       ))


(defn magenta2leipzig [note_sequence]
  (let [notes (:notes note_sequence)
        stepsPerQuarter (-> note_sequence
                            :quantizationInfo
                            :stepsPerQuarter)
        ]
    (map #(let [time     (-> % :quantizedStartStep
                             Integer/parseInt
                             (/ stepsPerQuarter ))
                duration (-> % :quantizedEndStep
                             Integer/parseInt
                             (/ stepsPerQuarter )
                             (- time))]
            (hash-map :pitch (:pitch %)
                      :time time
                      :duration duration
                      :instrument (:instrument %)))
         notes)))

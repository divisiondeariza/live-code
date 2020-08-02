(ns tutorial.core
  (:require [clojure.repl :refer :all]
            [overtone.live  :refer :all]
            [quil.core :as q]
            [quil.middleware :as m]
            [shadertone.tone :as t]

            [tutorial.playall :refer [play-all]]

       )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(play-all)

(defn setup []
  (let [myAnimation (gifAnimation.Gif. (quil.applet/current-applet) "tenor.gif")
        init-value (.play myAnimation)] ;; awful way to get side effects

    (hash-map :myAnimation myAnimation ))
)


(defn draw [state]
 (q/image (:myAnimation state) 10 10)
)


(q/defsketch golden-ratio-flowe
  :host "host"
  :size [500 500]
  :setup setup
  :draw draw
  :middleware [m/fun-mode])

(stop)

(defproject tutorial "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [overtone "0.10.6"  ]
                 [quil "3.1.0"]
                 [leipzig "0.10.0"]
                 [shadertone "0.2.5"]
                 [clj-http "2.3.0"]
                 [sonic-pi "0.1.0-SNAPSHOT"] ;checkout
                 ]
  :resource-paths [
                   "gifAnimation/library/gifAnimation.jar"
                   ]

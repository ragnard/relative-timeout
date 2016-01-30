(ns ^{:skip-wiki true} ragnard.relative-timeout
  (:require [clojure.core.async.impl.protocols :as impl]
            [clojure.core.async.impl.channels :as channels])
  (:import [java.util.concurrent Executors ScheduledExecutorService TimeUnit]))

(set! *warn-on-reflection* true)

(defonce ^:private ^ScheduledExecutorService executor
  (Executors/newSingleThreadScheduledExecutor))

(defn timeout
  "returns a channel that will close after msecs. Uses relative time and is not
  affected by clock changes."
  [^long msecs]
  (let [timeout-chan (channels/chan nil)]
    (.schedule executor ^Runnable #(impl/close! timeout-chan) msecs TimeUnit/MILLISECONDS)
    timeout-chan))

(ns kixi.paloma.enrich.events
  (:require [kixi.paloma.enrich.llpg :as llpg]))

(defn data-source [acc event]
  (keyword (:data-source event)))

(defmulti rollup-event data-source)

(defmethod rollup-event :llpg [acc event]
  (println "This is a LLPG")
  (let [uprn (:uprn event)]
    (-> acc
        (update (keyword (:uprn event))
                (fnil conj (:event acc)) event))))

(defmethod rollup-event :nndr [acc event]
  (let [uprn (:uprn event)]
    (-> acc
        (update (keyword (:uprn event))
                (fnil conj (:event acc)) event))))

(defmethod rollup-event :civica [acc event]
  (let [uprn (:uprn event)]
    (-> acc
        (update (keyword (:uprn event))
                (fnil conj (:event acc)) event))))

(defmethod rollup-event :default [acc event]
  (println "THis is the default...")
  (let [uprn (:uprn event)]
    (-> acc
        (update (keyword (:uprn event))
                (fnil conj (:event acc)) event))))

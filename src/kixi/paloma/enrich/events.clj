(ns kixi.paloma.enrich.events
  (:require [kixi.paloma.enrich.llpg :as llpg]
            [kixi.paloma.enrich.nndr :as nndr]
            [kixi.paloma.enrich.civica :as civica]))

(defn data-source [_ event]
  (keyword (:data_source event)))

(defn llpg-nndr-lookup [sm]
  (into {}
        (map (fn [m]
               [(:nndr_prop_ref m) (:uprn m)]) sm)))

(defmulti merge-record data-source)

(defmethod merge-record :llpg [acc event]
  (llpg/bx-record acc event))

(defmethod merge-record :nndr [acc event]
  (nndr/bx-record acc event))

(defmethod merge-record :civica [acc event]
  (civica/bx-record acc event))

(defmulti preferred-key data-source)

(defmethod preferred-key :llpg [_ event]
  (:uprn event))

(defmethod preferred-key :civica [_ event]
  (:uprn event))

(defmethod preferred-key :nndr [lookup event]
  (-> event
      :nndr_prop_ref
      (lookup)))

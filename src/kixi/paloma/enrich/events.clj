(ns kixi.paloma.enrich.events
  (:require [kixi.paloma.enrich.llpg :as llpg]
            [kixi.paloma.enrich.nndr :as nndr]
            [kixi.paloma.enrich.civica :as civica]
            [taoensso.timbre :as log]))

(defn data-source [_ event]
  (keyword (:data_source event)))

;; From SQL uprns are java Double values from JDBC and come out as 1.00023544877E11
;; .longValue gets the raw value and then we turn that into a string for the lookup table.
(defn llpg-nndr-lookup [sm]
  (into {}
        (map (fn [m]
               [(:nndr_prop_ref m) (:uprn m)]) sm)))

(defmulti merge-record data-source)

(defmethod merge-record :llpg [acc event]
  (log/info "Merging LLPG Record")
  (llpg/bx-record acc event))

(defmethod merge-record :nndr [acc event]
  (log/info "Merging NNDR Record")
  (nndr/bx-record acc event))

(defmethod merge-record :civica [acc event]
  (log/info "Merging Civica Record")
  (civica/bx-record acc event))


(defmulti preferred-key data-source)

(defmethod preferred-key :llpg [_ event]
  (log/info "Finding LLPG preferred key")
  (:uprn event))

(defmethod preferred-key :civica [_ event]
  (log/info "Finding Civica preferred key")
  (:uprn event))

(defmethod preferred-key :nndr [lookup event]
  (log/info "Finding NNDR preferred key")
  (-> event
      :nndr_prop_ref
      (get lookup)))

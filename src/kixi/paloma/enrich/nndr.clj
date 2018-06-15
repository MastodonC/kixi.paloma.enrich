(ns kixi.paloma.enrich.nndr
  (:require [kixi.paloma.enrich.file :as kf]
            [clojure.string :as str]
            [clojure.set :as s]
            [kixi.paloma.enrich.string :as pes]))

(defn organisation-populated? [v]
  (not (empty? (str/trim (:account_holder1 v)))))

(defn create-name [nndr]
  (-> (select-keys nndr [:business_name :data_source :last_updated])
      (s/rename-keys {:last_updated :update_date})
      (assoc :data_source "nndr"
             :uprn (get nndr :uprn)
             :premises_ref nil
             :civica_preferred_name nil
             :start_date nil
             :end_date nil)
      (pes/truncate-val :uprn 40)))

(defn bx-record [acc nndr]
  (-> acc
      (assoc :uprn (get nndr :uprn (get acc :uprn))
             :nndr_prop_ref (:nndr_prop_ref nndr)
             :names (-> (get acc :names #{})
                        (conj (create-name nndr))))
      (pes/truncate-val :uprn 40)
      (pes/truncate-val :nndr_prop_ref 40)))


(defn load-nndr-from-csv [nndr-lookup filename]
  (->> filename
       kf/load-data
       (remove #(nil? (:nndr_prop_ref %)))
       (filter #(re-matches #"\d+" (:nndr_prop_ref %)))
       (map #(assoc % :uprn (get nndr-lookup (:nndr_prop_ref %))))
       (remove #(nil? (:uprn %)))
       (map #(assoc % :data_source "nndr"))))

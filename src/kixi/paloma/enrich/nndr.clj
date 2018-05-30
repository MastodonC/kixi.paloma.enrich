(ns kixi.paloma.enrich.nndr
  (:require [kixi.paloma.enrich.file :as kf]
            [clojure.string :as str]
            [clojure.set :as s]))

(defn organisation-populated? [v]
  (not (empty? (str/trim (:account_holder1 v)))))

(defn bx-record [acc nndr]
  (assoc acc
         :uprn (get nndr :uprn (get acc :uprn))
         :nndr_prop_ref (:nndr_prop_ref nndr)
         :names (-> (get acc :names #{})
                    (conj (-> (select-keys nndr [:account_holder1 :data_source :last_updated])
                              (s/rename-keys {:account_holder1 :business_name :last_updated :update_date})
                              (assoc :data_source "nndr"))))))


(defn load-nndr-from-csv [nndr-lookup filename]
  (->> filename
       kf/load-data
       (remove #(nil? (:nndr_prop_ref %)))
       (filter #(re-matches #"\d+" (:nndr_prop_ref %)))
       (map #(assoc % :uprn (get nndr-lookup (:nndr_prop_ref %))))
       (remove #(nil? (:uprn %)))
       (map #(assoc % :data_source "nndr"))))

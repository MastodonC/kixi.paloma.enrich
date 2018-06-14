(ns kixi.paloma.enrich.llpg
  ;; (:require [kixi.palmoma.enrich.db.llpg :as llpg-db])
  (:require [kixi.paloma.enrich.file :as kf]
            [clojure.string :as str]
            [clojure.set :as s]
            [kixi.paloma.enrich.string :as pes]))

(defn organisation-populated? [v]
  (not (empty? (:organisation v))))

(defn has-address? [llpg]
  (and (:postcode_master llpg)
       (not= "NULL" (:postcode_master llpg))))

(defn has-name? [llpg]
  (and (:organisation llpg)
       (not= "NULL" (:organisation llpg))))

(defn maybe-add-address [addresses llpg]
  (if (has-address? llpg)
    (conj addresses
          (-> (select-keys llpg [:uprn :postcode_master :bs7666_address :data_source])
              (s/rename-keys {:postcode_master :postcode :bs7666_address :address_fields})
              (pes/truncate-val :uprn 40)
              (pes/truncate-val :postcode 20)
              (pes/truncate-val :address_fields 255)
              (assoc :data_source "llpg"
                     :premises_ref nil)))
    addresses))

(defn maybe-add-name [names llpg]
  (if (has-name? llpg)
    (conj names
          (-> (select-keys llpg [:uprn :data_source :organisation :start_date :end_date :last_update_date])
              (s/rename-keys {:organisation :business_name :last_update_date :update_date})
              (assoc :data_source "llpg"
                     :premises_ref nil
                     :civica_preferred_name nil
                     :start_date nil
                     :end_date nil
                     :update_date nil)
              (pes/truncate-val :uprn 40)
              (pes/truncate-val :business_name 255)))
    names))

(defn bx-record [acc llpg]
  (-> acc
      (assoc :uprn (:uprn llpg)
             :nndr_prop_ref (:nndr_prop_ref llpg)
             :start_date (:start_date llpg)
             :end_date (:end_date llpg)
             :addresses (-> (get acc :addresses #{})
                            (maybe-add-address llpg))
             :names (-> (get acc :names #{})
                        (maybe-add-name llpg)))
      (pes/truncate-val :uprn 40)
      (pes/truncate-val :nndr_prop_ref 40)))

(defn load-llpg-from-csv [filename]
  (->> filename
       kf/load-data
       (remove #(nil? (:uprn %)))
       (filter #(re-matches #"\d+" (:uprn %)))
       (map #(assoc % :data_source "llpg"))))


(comment

  (defn load-data []
    (let [llpg (llpg-db/get-llpg-records-no-address {})]
      ;; WIP -  test the connection is working and returning data.
      (count llpg)
      (first llpg)))
  )

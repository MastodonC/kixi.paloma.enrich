(ns kixi.paloma.enrich.llpg
  ;; (:require [kixi.palmoma.enrich.db.llpg :as llpg-db])
  (:require [kixi.paloma.enrich.file :as kf]
            [clojure.string :as str]
            [clojure.set :as s]))

(defn organisation-populated? [v]
  (not (empty? (:organisation v))))

(defn bx-record [acc llpg]
  (assoc acc
         :uprn (:uprn llpg)
         :nndr_prop_ref (:nndr_prop_ref llpg)
         :start_date (:start_date llpg)
         :end_date (:end_date llpg)
         :addresses (-> (get acc :addresses #{})
                        (conj (-> (select-keys llpg [:uprn :postcode_master :data_source])
                                  (s/rename-keys {:postcode_master :postcode}))))
         :names (-> (get acc :names #{})
                    (conj (-> (select-keys llpg [:uprn :data_source :organisation :start_date :end_date :last_update_date])
                              (s/rename-keys {:organisation :business_name :last_update_date :update_date}))))))

(defn load-llpg-from-csv [filename]
  (->> filename
       kf/load-data
       (filter #(not-empty (:uprn %)))
       (map #(assoc % :data_source "llpg"))))


(comment

  (defn load-data []
    (let [llpg (llpg-db/get-llpg-records-no-address {})]
      ;; WIP -  test the connection is working and returning data.
      (count llpg)
      (first llpg)))
  )

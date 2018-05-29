(ns kixi.paloma.enrich.civica
  (:require [kixi.paloma.enrich.file :as kf]
            [clojure.string :as str]
            [clojure.set :as s]))

;; Fields from Civica occassionally have white space on either side so it needs stripping out
(defn organisation-populated? [v]
  (not (empty? (str/trim (:civica_name v)))))

(defn create-address [m]
  (->> ((juxt :houseno :housename :add1 :add2 :add3 :add4 :postcode) m)
       (filter not-empty)
       (str/join "\n")))

(defn create-name [m]
  (-> m
      (select-keys [:uprn :premises_ref :premises_id :civica_name])
      (assoc :civica_preferred_name (= (:civica_name m) (:preferred_name m)))
      (s/rename-keys {:civica_name :business_name})))

(defn bx-record [acc civica]
  (assoc acc
         :uprn (:uprn civica)
         :names (-> (get acc :names #{})
                    (conj (create-name civica)))
         :addresses (-> (get acc :addresses #{})
                        (conj (-> (select-keys civica [:uprn :postcode :data_source])
                                  (assoc :address (create-address civica)))))))

(defn load-civica-from-csv [filename]
  (->> filename
       kf/load-data
       (map (fn [m]
              (reduce-kv (fn [a k v]
                           (assoc a k (str/trim v))) {} m)))
       (filter #(not-empty (:uprn %)))
       (map #(assoc % :data_source "civica"))))

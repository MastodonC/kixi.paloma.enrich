(ns kixi.paloma.enrich.civica
  (:require [kixi.paloma.enrich.file :as kf]
            [clojure.string :as str]
            [clojure.set :as s]
            [kixi.paloma.enrich.string :as pes]))

;; Fields from Civica occassionally have white space on either side so it needs stripping out
(defn organisation-populated? [v]
  (not (empty? (str/trim (:civica_name v)))))

(defn clean-records [sm]
  (->> sm
       (map (fn [m]
              (reduce-kv (fn [a k v]
                           (assoc a k (if (string? v)
                                        (str/trim v)
                                        v))) {} m)))))

(defn create-address [m]
  (->> ((juxt :houseno :housename :add1 :add2 :add3 :add4 :postcode) m)
       (filter not-empty)
       (str/join "\n")))

(defn create-name [m]
  (let [preferred? (= (-> m :civica_name str/lower-case)
                      (-> m :preferred_name str/lower-case))
        m' (if preferred?
             (assoc m :business_name (:preferred_name m))
             (assoc m :business_name (:civica_name m)))]
    (-> m'
        (select-keys [:uprn :premises_ref :premises_id :business_name :last_activity])
        (s/rename-keys {:last_activity :update_date})
        (pes/truncate-val :uprn 40)
        (pes/truncate-val :postcode 20)
        (pes/truncate-val :premises_ref 40)
        (pes/truncate-val :premises_id 40)
        (pes/truncate-val :business_name 255)
        (assoc :civica_preferred_name preferred?
               :data_source "civica"
               :start_date nil
               :end_date nil))))

(defn bx-record [acc civica]
  (-> acc
      (assoc :uprn (:uprn civica)
             :nndr_prop_ref nil
             :start_date nil
             :end_date nil
             :names (-> (get acc :names #{})
                        (conj (create-name civica)))
             :addresses (-> (get acc :addresses #{})
                            (conj (-> (select-keys civica [:uprn :postcode])
                                      (assoc :address_fields (create-address civica)
                                             :data_source "civica"
                                             :premises_ref nil)))))
      (pes/truncate-val :uprn 40)))

(defn load-civica-from-csv [filename]
  (->> filename
       kf/load-data
       (map (fn [m]
              (reduce-kv (fn [a k v]
                           (assoc a k (str/trim v))) {} m)))
       (remove #(nil? (:uprn %)))
       (filter #(re-matches #"\d+" (:uprn %)))
       (remove #(re-matches #".*\*.*" (:premises_ref %)))
       (map #(assoc % :data_source "civica"))))

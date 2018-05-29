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
  (let [preferred? (= (-> m :civica_name str/lower-case)
                      (-> m :preferred_name str/lower-case))
        m' (if preferred?
             (assoc m :business_name (:preferred_name m))
             (assoc m :business_name (:civica_name m)))]
    (-> m'
        (select-keys [:uprn :premises_ref :premises_id :business_name])
        (assoc :civica_preferred_name preferred?)
        (assoc :data_source "civica"))))

(defn bx-record [acc civica]
  (assoc acc
         :uprn (:uprn civica)
         :names (-> (get acc :names #{})
                    (conj (create-name civica)))
         :addresses (-> (get acc :addresses #{})
                        (conj (-> (select-keys civica [:uprn :postcode])
                                  (assoc :address (create-address civica))
                                  (assoc :data_source "civica"))))))

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

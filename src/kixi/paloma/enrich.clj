(ns kixi.paloma.enrich
  (:require [kixi.paloma.enrich.civica :as civica]
            [kixi.paloma.enrich.nndr :as nndr]
            [kixi.paloma.enrich.llpg :as llpg]
            [kixi.paloma.enrich.events :as events]
            [kixi.paloma.enrich.db.llpg :as llpg-db]
            [kixi.paloma.enrich.db.civica :as civica-db]
            [kixi.paloma.enrich.db.nndr :as nndr-db])
  (:gen-class))


(defn reduce-event [a x]
  (let [k (:uprn x)
        previous (get a k)
        new (events/merge-record previous x)]
    (assoc a k new)))

(defn csv->business-index [sources]
  (let [{:keys [llpg-source civica-source nndr-source]} sources
        llpg-data (llpg/load-llpg-from-csv llpg-source)
        nndr-lookup (events/llpg-nndr-lookup llpg-data)

        civica-data (civica/load-civica-from-csv civica-source)
        nndr-data (nndr/load-nndr-from-csv nndr-lookup nndr-source)]
    (reduce reduce-event {} (concat llpg-data civica-data nndr-data))))

(defn db->business-index []
  (let [llpg-data (->> (llpg-db/get-llpg-records)
                       (map #(assoc % :data_source "llpg")))
        nndr-lookup (events/llpg-nndr-lookup llpg-data)
        civica-data (->> (civica-db/get-civica-records)
                         (map #(assoc % :data_source "civica")))
        nndr-data (->> (nndr-db/get-nndr-records)
                       (remove #(nil? (:nndr_prop_ref %)))
                       (filter #(re-matches #"\d+" (:nndr_prop_ref %)))
                       (map #(assoc % :uprn (get nndr-lookup (:nndr_prop_ref %))))
                       (remove #(nil? (:uprn %)))
                       (map #(assoc % :data_source "nndr")))]
    (reduce reduce-event {} (concat llpg-data civica-data nndr-data))))

(defn -main
  [& args]
  (let [bx (db->business-index)]
    (spit "testenrich.edn" (pr-str bx))))


(comment

  ;; some predicates
  (defn from-source? [source x]
    (some #(= (:data_source %) source) (:names x)))

  (defn business_name-is-NULL? [x]
    (some #(= (:business_name %) "NULL") (:names x)))

  (clojure.pprint/pprint (take 10 (filter (fn [x]
                                            (and #_(from-source? "civica" x)
                                                 #_(from-source? "nndr" x)
                                                 (from-source? "llpg" x)
                                                 (business_name-is-NULL? x)))
                                          (vals bx))))

  (def llpg-data (llpg/load-llpg-from-csv llpg-file))
  (def nndr-lookup (events/llpg-nndr-lookup llpg-data))

  (def civica-data (civica/load-civica-from-csv civica-file))

  (def nndr-data (nndr/load-nndr-from-csv nndr-lookup nndr-file))



  (def csv-sources {:llpg-source llpg-file
                    :civica-source civica-file
                    :nndr-source nndr-file})

  (def bx (csv->business-index csv-sources))

  )

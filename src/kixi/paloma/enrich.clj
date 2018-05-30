(ns kixi.paloma.enrich
  (:require [kixi.paloma.enrich.civica :as civica]
            [kixi.paloma.enrich.nndr :as nndr]
            [kixi.paloma.enrich.llpg :as llpg]
            [kixi.paloma.enrich.events :as events])
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

(defn -main
  "Main function entry point."
  [& args]
  )


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

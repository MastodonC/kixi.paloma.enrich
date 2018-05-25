(ns kixi.paloma.enrich.llpg
  ;; (:require [kixi.palmoma.enrich.db.llpg :as llpg-db])
  (:require [kixi.paloma.enrich.file :as kf]
            [clojure.string :as str]))

(defn organisation-populated? [v]
  (not (empty? (:organisation v))))

(defn load-llpg-from-csv [filename]
  (let [llpg-data (kf/load-data filename)]
    (map (fn [v]
           (assoc v :organisation-populated (organisation-populated? v))) llpg-data)))




(comment

  (defn load-data []
    (let [llpg (llpg-db/get-llpg-records-no-address {})]
      ;; WIP -  test the connection is working and returning data.
      (count llpg)
      (first llpg)))
  )

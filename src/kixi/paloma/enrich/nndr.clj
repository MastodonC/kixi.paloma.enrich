(ns kixi.paloma.enrich.nndr
  (:require [kixi.paloma.enrich.file :as kf]
            [clojure.string :as str]))

(defn organisation-populated? [v]
  (not (empty? (str/trim (:account_holder1 v)))))

(defn load-nndr-from-csv [filename]
  (let [nndr-data (kf/load-data filename)]
    (map (fn [v]
           (assoc v :organisation-populated (organisation-populated? v))) nndr-data)))

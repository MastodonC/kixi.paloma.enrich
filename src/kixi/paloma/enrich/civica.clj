(ns kixi.paloma.enrich.civica
  (:require [kixi.paloma.enrich.file :as kf]
            [clojure.string :as str]))

;; Fields from Civica occassionally have white space on either side so it needs stripping out
(defn organisation-populated? [v]
  (not (empty? (str/trim (:civica_name v)))))

(defn load-civica-from-csv [filename]
  (let [civica-data (kf/load-data filename)]
    (map (fn [v]
           (assoc v :organisation-populated (organisation-populated? v))) civica-data)))

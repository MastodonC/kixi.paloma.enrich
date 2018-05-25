(ns kixi.paloma.enrich.file
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn remove-bom [^String line]
  (let [bom "\uFEFF"]
    (if (.startsWith line bom)
      (.substring line 1)
      line)))

;; Initial field names, will get revised.

(defn export-business-records->csv [out xs]
  (csv/write-csv out [["UPRN"
                       "NDR_Ref"
                       "Civica Premises Ref"
                       "Civica Premises ID"
                       "Address Fields"
                       "Preferred Name"
                       "Start Date"
                       "End Date"]])
  ;;(csv/write-csv out (map function xs)
  )

(defn export-alternative-names->csv [out xs]
  (csv/write-csv out [["UPRN"
                       "Source"
                       "Alternative Name"
                       "Update Date"
                       "Start Date"
                       "End Date"]])
  ;;(csv/write-csv out (map function xs)
  )


;; CSV reader functions

(defn format-key
  "Converts the CSV header to a friend Clojure keyword."
  [str-key]
  (when (string? str-key)
    (-> str-key

        clojure.string/lower-case
        (clojure.string/replace #" " "-")
        keyword)))

(defn load-data
  "Loads the CSV file from Witan and converts the data to a Clojure friendly map using the CSV header as the keywords."
  [file-id]
  (let [csv (-> file-id
                slurp
                remove-bom ;; CSV files from MS SQL Server have BOM, needs to be removed or it will break the map.
                (csv/read-csv))
        headers (map format-key (first csv))]
    (map #(zipmap headers %) (rest csv))))

(comment
  ;; placeholder csv write test.
  (with-open [writer (clojure.java.io/writer "business-records.csv")]
    (export-business-records->csv writer output-data))

  )

(ns kixi.paloma.enrich.bx
  (:require [kixi.paloma.enrich.db.bx :as bxdb]
            [taoensso.timbre :as log]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(defn persist-business-record [{:keys [uprn nndr_prop_ref start_date end_date]} tablename]
  (log/infof "Writing business record for %s" uprn)
  (bxdb/create-business-record! {:uprn uprn
                                 :nndr_prop_ref nndr_prop_ref
                                 :start_date nil
                                 :end_date nil
                                 :btname tablename}))

(defn persist-business-names [business-names tablename]
  (log/infof "Writing business names to table %s" tablename)
  (->> business-names
       (map #(assoc % :bntname tablename))
       (run! #(bxdb/create-business-name-record! %))))

(defn persist-addresses [addresses tablename]
  (log/infof "Writing addresses to table %s" tablename)
  (->> addresses
       (map #(assoc % :atname tablename))
       (run! #(bxdb/create-address-record! %))))

(defn create-tables [datestamp]
  (log/infof "Creating tables for datestamp: %s" datestamp)
  (bxdb/create-business-table! {:btname (str "business_" datestamp)})
  (bxdb/create-business-name-table! {:bntname (str "business_name_" datestamp)})
  (bxdb/create-address-table! {:atname (str "address_" datestamp)}))



(defn create-views [datestamp]
  (log/info "Updating table views.")
  (bxdb/create-business-view! {:btname (str "business_" datestamp)})
  (bxdb/create-address-view! {:atname (str "address_" datestamp)})
  (bxdb/create-business-name-view! {:bntname (str "business_name_" datestamp)}))

(defn persist-bx-to-db [bx]
  (log/info "Persisting Business Index data.")
  (let [datestamp (f/unparse (f/formatter :basic-date) (t/now))]
    (create-tables datestamp)
    (run! (fn [[_ bxr]]
            (do
              (persist-addresses (:addresses bxr) (str "address_" datestamp))
              (persist-business-names (:names bxr) (str "business_name_" datestamp))
              (persist-business-record bxr (str "business_" datestamp)))) bx)
    (create-views datestamp)))

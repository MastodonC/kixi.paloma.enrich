(ns kixi.paloma.enrich.bx
  (:require [kixi.paloma.enrich.db.bx :as bxdb]))

(defn persist-business-record [{:keys [uprn nndr_prop_ref start_date end_date]}]
  (bxdb/create-business-record! {:uprn uprn
                                 :nndr_prop_ref nndr_prop_ref
                                 :start_date nil
                                 :end_date nil}))

(defn persist-business-names [business-names]
  (run! #(bxdb/create-business-name-record! %) business-names))

(defn persist-addresses [addresses]
  (run! #(bxdb/create-address-record! %) addresses))

(defn persist-bx-to-db [bx]
  (run! (fn [[_ bxr]]
          (do
            (persist-addresses (:addresses bxr))
            (persist-business-names (:names bxr))
            (persist-business-record bxr))) bx))

(ns kixi.paloma.enrich.dates
  (:require [clj-time.core :as t]
            [clj-time.format :as f]))

(defn convert-to-date [i]
  (cond
    (= 0 i) nil
    (= nil i) nil
    (< i 19000101) nil
    :else (f/parse (f/formatter :basic-date) (.toString i))))

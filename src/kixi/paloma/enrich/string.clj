(ns kixi.paloma.enrich.string
  (:require [clojure.string :as str]))

(defn double->string [d]
  (str (.longValue d)))

(defn integer->string [i]
  (.toString i))

(defn truncate [s n]
  (if (< n (count s))
    (apply str (take n s))
    s))

(defn truncate-val [m k n]
  (update m k truncate n))

(defn capitalise-words [s]
  (when (string? s)
    (if (not= "NULL" s)
      (-> s
          str/lower-case
          (clojure.string/replace #"(?<=\b(?:mc|mac)?)[a-zA-Z](?<!'s\b)" (fn [x] (.toUpperCase x))))
      s)))

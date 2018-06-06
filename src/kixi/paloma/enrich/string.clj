(ns kixi.paloma.enrich.string)

(defn truncate [s n]
  (if (< n (count s))
    (apply str (take n s))
    s))

(defn truncate-val [m k n]
  (update m k truncate n))
(ns kixi.paloma.enrich.string)

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

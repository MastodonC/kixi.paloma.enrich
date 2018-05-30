(defproject kixi.paloma.enrich "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [clj-time "0.14.3"] ;; Used for JDBC timestamps (if requried)
                 [environ "1.1.0"]
                 [mount "0.1.12"]
                 [conman "0.7.8"]
                 [com.taoensso/timbre "4.10.0"]
                 [com.layerware/hugsql "0.4.8"]
                 [org.postgresql/postgresql "42.1.4"] ;; Driver for the Postgres BX DB.
                 [net.sourceforge.jtds/jtds "1.3.1"]] ;; Open source version of the SQL Server 2012 JDBC Drivers.
  :main ^:skip-aot kixi.paloma.enrich
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

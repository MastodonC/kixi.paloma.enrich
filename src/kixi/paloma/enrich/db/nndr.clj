(ns kixi.paloma.enrich.db.nndr
  (:require
   [clj-time.jdbc]
   [clojure.java.jdbc :as jdbc]
   [taoensso.timbre :as log]
   [conman.core :as conman]
   [environ.core :refer [env]]
   [mount.core :refer [defstate]])
  (:import [java.sql
            BatchUpdateException
            PreparedStatement]))

(defstate ^:dynamic *db*
  :start (if-let [jdbc-url (env :nndr-database-url)]
           (conman/connect! {:jdbc-url jdbc-url})
           (do
             (log/warn "database connection URL was not found, please set :nndr-database-url in your config.")
             *db*))
  :stop (conman/disconnect! *db*))

(conman/bind-connection *db* "sql/nndr.sql")
(mount.core/start #'*db*)

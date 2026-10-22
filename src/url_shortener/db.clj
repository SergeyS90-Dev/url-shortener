
(ns url-shortener.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "url_shortener.db"})

(defn init-db []
  (jdbc/execute! db-spec
    ["CREATE TABLE IF NOT EXISTS urls (
        short_url TEXT PRIMARY KEY,
        original_url TEXT NOT NULL
      )"]))

(defn save-url [short-url original-url]
  (jdbc/insert! db-spec :urls
    {:short_url short-url
     :original_url original-url}))

(defn get-url [short-url]
  (first
    (jdbc/query db-spec
      ["SELECT original_url FROM urls WHERE short_url = ?" short-url])))

(defn update-url [short-url original-url]
  (jdbc/update! db-spec :urls
    {:original_url original-url}
    ["short_url = ?" short-url]))

(defn delete-url [short-url]
  (jdbc/delete! db-spec :urls
    ["short_url = ?" short-url]))

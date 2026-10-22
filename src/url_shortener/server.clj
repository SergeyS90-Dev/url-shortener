
(ns url-shortener.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.codec :as codec]
            [clojure.string :as str]
            [url-shortener.db :as db]))

(defn response [status body]
  {:status status
   :headers {"Content-Type" "text/plain; charset=utf-8"}
   :body body})

(defn handler [request]
  (let [method (:request-method request)
        path (:uri request)
        params (codec/form-decode (or (:query-string request) ""))
        body (if-let [stream (:body request)]
               (str/trim (slurp stream))
               "")]

    (cond

      (and (= method :post) (= path "/normal-url"))
      (if (str/blank? body)
        (response 400 "Ошибка")
        (let [short-url (subs (str (java.util.UUID/randomUUID)) 0 8)]
          (db/save-url short-url body)
          (response 201 short-url)))

      (and (= method :get) (= path "/short-url"))
      (let [short-url (get params "url")
            result (when short-url
                     (db/get-url short-url))]
        (if result
          (response 200 (:original_url result))
          (response 404 "Ошибка")))

      (and (= method :put) (= path "/short-url"))
      (let [[short-url normal-url] (str/split body #"\s+" 2)]
        (if (or (str/blank? short-url)
                (str/blank? normal-url))
          (response 400 "Ошибка")
          (if (pos? (first (db/update-url short-url normal-url)))
            (response 200 "OK")
            (response 404 "Ошибка"))))

      (and (= method :delete) (= path "/short-url"))
      (if (str/blank? body)
        (response 400 "Ошибка")
        (if (pos? (first (db/delete-url body)))
          (response 200 "OK")
          (response 404 "Ошибка")))

      :else
      (response 404 "Ошибка"))))

(defn start-server [port]
  (db/init-db)
  (run-jetty handler {:port port :join? false}))
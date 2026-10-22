
(ns url-shortener.client
  (:import [java.net URI URLEncoder]
           [java.net.http HttpClient HttpRequest
            HttpRequest$BodyPublishers HttpResponse
            HttpResponse$BodyHandlers]
           [java.nio.charset StandardCharsets]))

(def base-url "http://localhost:8080")

(def ^HttpClient http-client
  (HttpClient/newHttpClient))

(defn send-request [method path body]
  (try
    (let [builder (HttpRequest/newBuilder
                    (URI/create (str base-url path)))
          publisher (HttpRequest$BodyPublishers/ofString
                      (or body "") StandardCharsets/UTF_8)
          request (case method
                    :get (.GET builder)
                    :post (.POST builder publisher)
                    :put (.PUT builder publisher)
                    :delete (.method builder "DELETE" publisher))
          _ (.header request "Content-Type"
                     "text/plain; charset=utf-8")
          ^HttpResponse result
          (.send http-client
                 (.build request)
                 (HttpResponse$BodyHandlers/ofString
                   StandardCharsets/UTF_8))]
      (if (< (.statusCode result) 400)
        (.body result)
        "Ошибка"))
    (catch Exception _
      "Ошибка")))

(defn create-url [normal-url]
  (send-request :post "/normal-url" normal-url))

(defn show-url [short-url]
  (let [encoded (URLEncoder/encode
                  ^String short-url "UTF-8")]
    (send-request :get
                  (str "/short-url?url=" encoded)
                  nil)))

(defn update-url [short-url normal-url]
  (send-request :put
                "/short-url"
                (str short-url " " normal-url)))

(defn delete-url [short-url]
  (send-request :delete "/short-url" short-url))

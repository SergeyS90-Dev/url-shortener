
(ns url-shortener.core
  (:require [url-shortener.server :as server]
            [url-shortener.cli :as cli])
  (:gen-class))

(defn -main [& args]
  (if (seq args)
    (let [port (Integer/parseInt (first args))]
      (server/start-server port)
      (println (str "Server started on port " port)))
    (cli/start-client)))

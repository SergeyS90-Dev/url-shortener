
(ns url-shortener.cli
  (:require [clojure.string :as str]
            [url-shortener.client :as client]))

(defn print-menu []
  (println)
  (println "=== URL Shortener ===")
  (println "1. Создать")
  (println "2. Показать")
  (println "3. Изменить")
  (println "4. Удалить")
  (println "5. Выйти")
  (print "Выберите действие: ")
  (flush))

(defn read-input [message]
  (println message)
  (print "> ")
  (flush)
  (str/trim (or (read-line) "")))

(defn print-answer [answer]
  (println (str "Ответ: " answer)))

(defn create-action []
  (let [url (read-input "Введите обычный URL:")]
    (print-answer (client/create-url url))))

(defn show-action []
  (let [url (read-input "Введите короткий URL:")]
    (print-answer (client/show-url url))))

(defn update-action []
  (let [input (read-input
                "Введите короткий URL и новый обычный URL через пробел:")
        [short-url normal-url] (str/split input #"\s+" 2)]
    (if (or (str/blank? short-url)
            (str/blank? normal-url))
      (print-answer "Ошибка")
      (print-answer
        (client/update-url short-url normal-url)))))

(defn delete-action []
  (let [url (read-input "Введите короткий URL:")]
    (print-answer (client/delete-url url))))

(defn start-client []
  (println "Клиент URL Shortener")
  (loop []
    (print-menu)
    (let [choice (read-line)]
      (case choice
        "1" (do (create-action) (recur))
        "2" (do (show-action) (recur))
        "3" (do (update-action) (recur))
        "4" (do (delete-action) (recur))
        "5" (println "Завершение работы.")
        (do
          (println "Неверный пункт меню.")
          (recur))))))

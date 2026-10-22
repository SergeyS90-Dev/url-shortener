
(defproject url-shortener "0.1.0-SNAPSHOT"
  :description "REST API for URL shortening"
  :url "https://github.com/"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
:dependencies [[org.clojure/clojure "1.12.2"]
               [ring/ring-core "1.13.0"]
               [ring/ring-jetty-adapter "1.13.0"]
               [org.xerial/sqlite-jdbc "3.46.1.3"]
               [org.clojure/java.jdbc "0.7.12"]]

  :main ^:skip-aot url-shortener.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})

(ns helloworld.core
  (:use                                 ;[hiccup.core]
   [hiccup.page-helpers :only (html5 include-css)]
   [clojure.contrib.string :only (split)]
   [compojure.core :only (defroutes GET)]
   [hiccup.middleware :only (wrap-base-url)]
   [ring.adapter.jetty])
  (:require [compojure.route :as route
             :only (resources not-found) ]
            [compojure.handler :as handler :only (site)]
            ;[compojure.api :as api :only (site)]
            ))

(defn index-page
  ([name]
     (html5
      [:head
       [:title (str "Hello " name)]
       (include-css "/css/style.css")]
      [:body
       [:h1 (str "Hello " name)]]))
  ([] (index-page "World")))

(def match-opperator
  { "add"      +
    "subtract" -
    "multiply" *
    "divide"   /})

(defroutes hello-routes
  (GET "/calculator/:f/*" [f & x]
       (index-page (apply (match-opperator f)
                          (map #(Integer/parseInt %)
                               (split #" " (:* x))))))
  (GET "/" [] (index-page))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
     (->  hello-routes
         (wrap-base-url)))


;(defn app [req]
;  (response "Hello World"))

(defn -main []
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))

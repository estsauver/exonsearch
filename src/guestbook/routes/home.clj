(ns guestbook.routes.home
  (:require [compojure.core :refer :all]
            [guestbook.views.layout :as layout]
            [hiccup.form :refer :all]))


(defn exon-form [search, body]  (form-to [:post "/"]
            [:p "Search String"]
            (text-field "search" search)

            [:p "Sequence Body"]
            (text-area {:rows 10, :cols 40} "body" body)

            [:br]
            (submit-button "Search")
            ))

(defn home [] 
  (layout/common
   [:h1 "Hello World!"]
   (exon-form "Input Search Here" "Sequence Here")
   )
  )

(defn regex-string [string]
   (clojure.string/join
                   (concat "[A-Z]*" (interpose
                     "[a-z]*"
                     (seq string)
                     ) "[A-Z]*")
          )
                          )

(defn regex [string] (re-pattern (regex-string string)
                       )
  )

(defn find-string [search, body] (re-find (regex search) (clojure.string/join body)))
(defn exons [search body]
  (layout/common [:h1 "Showing exons"]
                 (exon-form search body)
                 [:br]
                  (find-string search body)))

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [search body] (exons search body)))

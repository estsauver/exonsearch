(ns guestbook.routes.home
  (:require [compojure.core :refer :all]
            [guestbook.views.layout :as layout]
            [hiccup.form :refer :all]
            [hiccup.element :refer :all]))


(defn exon-form [search, body]
  
    (form-to [:post "/"]
             [:div {:class "form-group"} [:p "Search String"]
             (text-field {:placeholder search} "search" )
              ]

             [:div {:class "form-group"}
             [:p "Sequence Body"]
             (text-area {:class "form-control" :rows "10", :placeholder body} "body")
              ]
             
             (submit-button {:class "btn btn-primary"} "Search")
             ))

(defn home [] 
  (layout/common
   [:h1 "Exonsearch"]
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

(defn find-string [search, body] (re-find (regex (clojure.string/upper-case search)) (clojure.string/join
                                                                                     (clojure.string/split-lines body))
                                                 )
                                          )
(defn exons [search body]
  (layout/common  (link-to "/" [:h1  "Showing exons"])
                  [:div
                   [:strong "Search String:"]
                   search]
                  [:pre {:class "pre-scrollable" } body]
                  
                 [:br]
                 [:h2 "Results:"]
                 [:div {:class "results" :style "width: 100%; word-wrap:break-word;"} (find-string search body)]
                 (link-to "/" [:button {:class "btn btn-primary"} "Search Again"])
                 )
  )

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [search body] (exons search body)))

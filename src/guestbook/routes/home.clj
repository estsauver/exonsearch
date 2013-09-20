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

(defn strip-headers [lines] (cond
                             (= \> (first (first lines))) (rest lines)
                             :else lines)
  )

(defn join-and-strip [lines] ( clojure.string/join (strip-headers (clojure.string/split-lines  lines))))

(defn exons-string [text] (clojure.string/replace text #"[a-z]+" ","))
(def base  "AACasdfAAGAB")



(defn regex-string [string]
   (clojure.string/join
                    (interpose
                     ",*"
                     (seq string)
                     )
          )
   )

(defn regex [string] (re-pattern (regex-string string)
                       )
  )

(defn match [search original] (re-find (regex search) (exons-string original)))

(defn html-output [search original]
  (let [match (re-find (regex search) (exons-string original))]
    (clojure.string/split
     (clojure.string/replace
      (exons-string original)
      match
      (clojure.string/replace
       (str "<b>" match "</b>")
       #","
       "</b>,<b>"))
     #","
     )
    )
  )
(defn results-seq [search base] (map-indexed (fn [idx itm] [(inc idx) (nil? (re-find #"b" itm)) itm]) (html-output search (join-and-strip base))))
(html-output "CAAG" base) 



                                        ; "," "</em>,<em>"




(defn find-string [search, body] (re-find (regex (exons-string body))))
   


(defn exons [search body]
  (layout/common  (link-to "/" [:h1  "Showing exons"])
                  [:div
                   [:strong "Search String:"]
                   search]
                  [:pre {:class "pre-scrollable" } body]
                  
                 [:br]
                 [:h2 "Results:"]
                 [:table {:class "table"}                      ;
                  (map (fn [elem] [:tr (if-not (nth elem 1) {:class "success"}) [:td (nth elem 0)]  [:td (nth elem 2)]]) (results-seq search body))]
                 (link-to "/" [:button {:class "btn btn-primary"} "Search Again"])
                 )
  )

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [search body] (exons search body)))

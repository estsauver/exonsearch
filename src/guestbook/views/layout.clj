(ns guestbook.views.layout
  (:require [hiccup.page :refer [html5 include-css]]
             [hiccup.form :refer :all]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to guestbook"]
     (include-css "//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css")
     (include-css "/css/screen.css")]
    [:body  [:div {:class "row"}]
            [:div {:class "col-md-6 col-md-offset-3"} body ]]
))

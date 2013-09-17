(ns guestbook.views.layout
  (:require [hiccup.page :refer [html5 include-css]
             [hiccup.form :refer :all]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to guestbook"]
     (include-css "/css/screen.css")]
    [:body body]))

(ns com.plumbee.hamcrest.formatting
  (:import (clojure.lang Keyword)
           (java.util.regex Pattern)))


(defn indent
  ([string] (indent string 4))
  ([string n] (.replace string "\n" (apply str (cons "\n" (repeat n " "))))))

(defn indented-list [descriptions]
  (indent (apply str (interpose "\n" (cons "" (map #(indent (str %)) descriptions))))))


(defmulti describe type)
(defmethod describe String [x] (str \" x \"))
(defmethod describe Character [x] (str \' x \'))
(defmethod describe Keyword [x] (str x))
(defmethod describe Pattern [x] (str "#\"" x "\""))
(defmethod describe nil [_] "nil")
(defmethod describe :default [x] (str \< x \>))

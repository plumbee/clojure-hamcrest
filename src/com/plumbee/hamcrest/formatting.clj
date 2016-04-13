;
; Copyright © 2016 Plumbee Ltd.
;
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
; http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
; implied. See the License for the specific language governing
; permissions and limitations under the License.
;
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

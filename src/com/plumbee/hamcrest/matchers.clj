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
(ns com.plumbee.hamcrest.matchers
  (:require [com.plumbee.hamcrest.matcher :as m]
            [com.plumbee.hamcrest.formatting :as f])
  (:import (java.util Map)
           (clojure.lang ILookup)))


(defn equal-to [expected]
  (m/->matcher (str "equal to " expected)
             #(if-not (= expected %) (str "was " %))))

(defn as-matcher [maybe-matcher]
  (if (:match-fn maybe-matcher)
    maybe-matcher
    (equal-to maybe-matcher)))

(defn as-matchers [maybe-matchers]
  (map as-matcher maybe-matchers))


(defn nil-value []
  (m/->matcher "nil" #(when-not (nil? %) (f/describe %))))

(defn an-instance-of [class]
  (m/->matcher (str "an instance of " class)
             #(when-not (instance? class %)
               (if (nil? %) "nil" (str "an instance of " (type %))))))

(defn matches-re [re]
  (m/->matcher (str "a string matching " (f/describe re))
             #(when-not (re-matches re %)
               (str (f/describe %) " did not match " re))))

(defn string-contains [substring]
  (m/->matcher (str "a string containing '" substring \')
             #(when-not (.contains % substring)
               (str (f/describe %) " did not contain '" substring \'))))

(defn has-str [string]
  (let [matcher (as-matcher string)]
    (m/->matcher (str "something with string representation '" matcher "'")
               #(m/match matcher (str %)))))

(defn greater-than [expected]
  (m/->matcher (str "a number greater than " (f/describe expected))
             #(if-not (> % expected) (str "was " (f/describe %)))))

(defn less-than [expected]
  (m/->matcher (str "a number less than " (f/describe expected))
             #(if-not (< % expected) (str "was " (f/describe %)))))

(defn is-not [expected]
  (let [matcher (as-matcher expected)]
    (m/->matcher (str "not " matcher)
               (fn [actual]
                 (let [mismatch-description (m/match matcher actual)]
                   (if mismatch-description
                     nil
                     (str "was " (f/describe actual))))))))

(def does-not is-not)

(defn all-of [& expecteds]
  (let [matchers (as-matchers expecteds)]
    (m/->matcher (str "all of:" (f/indented-list matchers))
               (fn [actual]
                 (let [mismatch-descriptions (filter identity (map #(m/match % actual) matchers))]
                   (if-not (empty? mismatch-descriptions)
                     (str "all of:" (f/indented-list mismatch-descriptions))))))))

(defn any-of [& expecteds]
  (let [matchers (as-matchers expecteds)]
    (m/->matcher (str "any of:" (f/indented-list matchers))
               (fn [actual]
                 (let [mismatch-descriptions (filter identity (map #(m/match % actual) matchers))]
                   (if (= (count mismatch-descriptions) (count matchers))
                     (str "all of:" (f/indented-list mismatch-descriptions))))))))


(defn has-key [key]
  (m/->matcher (str "a collection with key " (f/describe key))
             #(when-not (contains? % key) (str (f/describe %) " did not have key " (f/describe key)))))

(defn has-entry [key expected-value]
  (let [value-matcher (as-matcher expected-value)]
    (m/->matcher (str "a collection with mapping " (f/describe key) " -> " value-matcher)
               #(if (or (instance? Map %) (instance? ILookup %)) ; could be more but for now these are the main ones
                 (if-let [value (% key)]
                   (if-let [mismatch-description (m/match value-matcher value)]
                     (str "key " (f/describe key) " -> " mismatch-description))
                   (str (f/describe %) " did not have key " (f/describe key)))
                 (str (f/describe %) " was not a map")))))

(defn has-count [expected-count]
  (let [count-matcher (as-matcher expected-count)]
    (m/->matcher (str "with count " count-matcher)
               #(if-let [mismatch (m/match count-matcher (count %))]
                 (str (f/describe %) " had count " mismatch)))))

(defn empty-sequence []
  (has-count 0))

(defn has-item [expected-item]
  (let [item-matcher (as-matcher expected-item)]
    (m/->matcher (str "contains an item " item-matcher)
               (fn [actual]
                 (try
                   (let [sequence (seq actual)
                         mismatches (filter identity (map #(m/match item-matcher %) sequence))]
                     (if (= (count mismatches) (count sequence))
                       (str "none of the items matched:" (f/indented-list mismatches))))
                   (catch IllegalArgumentException e
                     (str "trying to iterate " (f/describe actual) " threw " (.getMessage e))))))))

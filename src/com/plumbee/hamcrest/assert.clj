(ns com.plumbee.hamcrest.assert
  (:require [clojure.test :as test]
            [com.plumbee.hamcrest.matcher :as matcher]
            [com.plumbee.hamcrest.formatting :as formatting]))


(defn- file-and-line
  [^Throwable exception depth]
  (let [stacktrace (.getStackTrace exception)]
    (if (< depth (count stacktrace))
      (let [^StackTraceElement s (nth stacktrace depth)]
        {:file (.getFileName s) :line (.getLineNumber s)})
      {:file nil :line nil})))


(defmethod test/report :hamfail [m]
  (test/with-test-out
    (test/inc-report-counter :fail)
    (println "\nFAIL in" (test/testing-vars-str m))
    (when (seq test/*testing-contexts*) (println (test/testing-contexts-str)))
    (when-let [message (:message m)] (println message))
    (println "expected:" (str (:expected m)))
    (println "  actual:" (str (:actual m)))))


(defn assert-that
  ([actual matcher] (assert-that "" actual matcher 2))
  ([message actual matcher] (assert-that message actual matcher 2))
  ([message actual matcher depth]
   (if-let [mismatch-description (matcher/match matcher actual)]
     (let [location (file-and-line (new Throwable) depth)]
       (test/report (merge location {:type     :hamfail
                                     :expected (formatting/indent (str matcher) 10)
                                     :actual   (formatting/indent mismatch-description 10)
                                     :message  message})))
     (test/report {:type :pass}))))


;;;;!;;;;;;;;;;;;;;;;
;
;(assert-that {} (an-instance-of java.util.Map))
;
;(assert-that {} (all-of (isn't (an-instance-of String))
;                        (an-instance-of java.util.Map)))
;
;(assert-that [] (any-of (isn't (an-instance-of String))
;                        (an-instance-of java.util.Map)))
;
;(assert-that [] (isn't (nil-value)))
;
;(assert-that {:a 1} (has-entry :a (equal-to 1)))
;
;(assert-that "Bobby" (matches-re #"Bobby"))
;
;(assert-that {:a "hello"} (has-str (string-contains "hello")))
;(assert-that {:a "hello"} (has-str (isn't (string-contains "Hello"))))
;
;(assert-that 2 (all-of (less-than 3) (greater-than 1)))
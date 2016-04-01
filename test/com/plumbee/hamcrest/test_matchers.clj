(ns com.plumbee.hamcrest.test-matchers
  (:require [clojure.test :refer :all]
            [com.plumbee.hamcrest.assert :refer [assert-that]]
            [com.plumbee.hamcrest.matchers :refer :all])
  (:import (java.util Map)))



(deftest Matchers
  (testing "matchers behave as expected"

    (assert-that {} (an-instance-of Map))

    (assert-that {} (all-of (is-not (an-instance-of String))
                            (an-instance-of Map)))

    (assert-that [] (any-of (is-not (an-instance-of String))
                            (an-instance-of Map)))

    (assert-that nil (nil-value))
    (assert-that [] (is-not (nil-value)))

    (assert-that {:a 1} (has-key :a))
    (assert-that {:a 1} (has-entry :a 1))
    (assert-that {:a 1} (has-entry :a (equal-to 1)))

    (assert-that "Bobby" (matches-re #"Bo[a-b]+y"))
    (assert-that "Bobby" (is-not (matches-re #"Bo[c-d]*y")))

    (assert-that {:a "hello"} (has-str (string-contains "hello")))
    (assert-that {:a "hello"} (has-str (does-not (string-contains "Hello"))))

    (assert-that 2 (all-of (less-than 3) (greater-than 1)))
    (assert-that 3 (is-not (less-than 3)))

    (assert-that [] (empty-sequence))
    (assert-that [1] (is-not (empty-sequence)))

    (assert-that [1] (has-item 1))
    (assert-that [1 2 3] (has-item 3))
    (assert-that [1 2 3] (does-not (has-item 4)))))

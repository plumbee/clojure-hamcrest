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
(ns com.plumbee.hamcrest.test-formatting
  (:require [clojure.test :refer :all]
            [com.plumbee.hamcrest.formatting :as test-subject]))


(deftest Formatting
  (testing "indent a single line"
    (is (= (test-subject/indent "\nHello") "\n    Hello"))
    (is (= (test-subject/indent "\nHello" 2) "\n  Hello"))
    (is (= (test-subject/indent "\nHello" 10) "\n          Hello")))
  (testing "does not indent a single fragment"
    (is (= (test-subject/indent "Hello") "Hello"))
    (is (= (test-subject/indent "Hello" 2) "Hello")))
  (testing "indent multiple lines"
    (is (= (test-subject/indented-list ["Hello"]) "\n    Hello"))
    (is (= (test-subject/indented-list ["Hello" "World" "With Bananas"]) "\n    Hello\n    World\n    With Bananas"))))

(deftest Description
  (testing "describe an Object"
    (is (= (test-subject/describe true) "<true>"))
    (is (= (test-subject/describe [1]) "<[1]>")))
  (testing "describe specific things"
    (is (= (test-subject/describe "Henry King") "\"Henry King\""))
    (is (= (test-subject/describe \c) "'c'"))
    (is (= (test-subject/describe :keyword) ":keyword"))
    (is (= (test-subject/describe nil) "nil"))))

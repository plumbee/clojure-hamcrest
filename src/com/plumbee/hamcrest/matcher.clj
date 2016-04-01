(ns com.plumbee.hamcrest.matcher)


(defprotocol matchable
  (match [matcher actual] ""))

(defrecord matcher [description match-fn]
  Object
  (toString [_] description)
  matchable
  (match [matcher actual] ((:match-fn matcher) actual)))

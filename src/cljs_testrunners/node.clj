(ns cljs-testrunners.node
  (:require [cljs.test :as test]))

(defmacro run-tests
  [& namespaces]
  `(test/run-tests (test/empty-env :cljs-testrunners.core/nodejs) ~@namespaces))

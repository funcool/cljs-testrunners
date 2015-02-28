(ns cljs-testrunners.core
  (:require [cljs.test :as test]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Simple (console based) test reporting.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(derive ::nodejs ::simple)

(defmethod test/report [::simple :begin-test-ns]
  [m]
  (println "[test]" (name (:ns m))))

(defmethod test/report [::simple :pass] [m]
  (test/inc-report-counter! :pass))

(defmethod test/report [::simple :error] [m]
  (test/inc-report-counter! :error)
  (println "\n  ERROR in" (test/testing-vars-str m))
  (if-let [message (:message m)]
    (println "    message:" message)
    (println "    message: Unexpected exception."))
  (when-let [value (:actual m)]
    (println "        exc:" value)))

(defmethod test/report [::simple :fail] [m]
  (test/inc-report-counter! :fail)
  (println "\n  FAIL in" (test/testing-vars-str m))
  (when (seq (:testing-contexts (test/get-current-env)))
    (println (test/testing-contexts-str)))
  (when-let [message (:message m)]
    (println message))
  (println "    expected:" (pr-str (:expected m)))
  (println "      actual:" (pr-str (:actual m)))
  (println))

(defmethod test/report [::simple :summary]
  [m]
  (println "\nRan" (:test m) "tests containing"
    (+ (:pass m) (:fail m) (:error m)) "assertions.")
  (println (:fail m) "failures," (:error m) "errors."))

(defmethod test/report [::nodejs :summary]
  [m]
  (println "\nRan" (:test m) "tests containing"
    (+ (:pass m) (:fail m) (:error m)) "assertions.")
  (println (:fail m) "failures," (:error m) "errors.")
  (let [success? (and (zero? (:fail m 0))
                      (zero? (:error m 0)))]
    (if success?
      (set! (.-exitCode js/process) 0)
      (set! (.-exitCode js/process) 1))))

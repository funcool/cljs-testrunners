# cljs-testrunners

ClojureScript test runners facilities/helpers.

Provides a easy way to run tests in nodejs/iojs and return proper exitcode on process
is finished. This is mandatory if you want run tests in some integration tests service like
travis, circleci or any other.

This package exists because clojurescript does not provides something like this out of the
box. And printing results in console is not really useful.

Additionally, this package provides slightly more compact console output.


## Quick Start

Add the `cljs-testrunners` to your dependencies list:

```clojure
[funcool/cljs-testrunners "0.1.0-SNAPSHOT"]
```

Create a testrunner ns on your test directory:

```clojure
;; file: yourappdir/test/yourapp/testrunner.cljs
(ns yourapp.testrunner
  (:require [cljs-testrunners.node :as node]
            [yourapp.fooo-tests]
            [yourapp.baar-tests]))

(defn main []
  (node/run-tests 'yourapp.fooo-tests
                  'yourapp.baar-tests))

(set! *main-cli-fn* main)
```

And configure your cljsbuild with something like this:

```clojure
{:test-commands {"test" ["node" "tests.js"]}
 :builds [{:id "dev"
           :source-paths ["test"]
           :notify-command ["node" "tests.js"]
           :compiler {:output-to "tests.js"
                      :output-dir "out"
                      :source-map true
                      :static-fns true
                      :cache-analysis false
                      :main yourapp.testrunner
                      :optimizations :none
                      :target :nodejs
                      :pretty-print true}}]}
```

Maybe in a future we will add other environments for run tests.

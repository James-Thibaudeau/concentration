(ns awesome.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [awesome.core-test]))

(doo-tests 'awesome.core-test)

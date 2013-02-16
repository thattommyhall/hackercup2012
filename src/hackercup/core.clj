(ns hackercup.core
  (:require [clojure.java.io :as io])
  )

(defn process-file [f input output]
  (with-open [output-file (io/writer output)
              input-file (io/reader input)]
    (doseq [output-line (map (partial format "Case #%d: %s\n" )
                             (iterate inc 1)
                             (map f (drop 1 (line-seq input-file))))]
        (do (.write output-file output-line)
            (print output-line)
  ))))


(ns hackercup.squished
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.math.combinatorics :as combinatorics]
            ))

(def input "/Users/tomh/Dropbox/Projects/hackercup/src/hackercup/squished-status-in.txt")
(def output "/Users/tomh/Dropbox/Projects/hackercup/src/hackercup/squished-status-out.txt")

(defn process-file [f input output]
  (with-open [output-file (io/writer output)
              input-file (io/reader input)]
    (doseq [output-line (map (partial format "Case #%d: %s\n" )
                             (iterate inc 1)
                             (map #(apply f %) (partition 2 (drop 1 (string/split (slurp input-file)  #"\s+" )))))]
        (do (.write output-file output-line)
            (print output-line)
            ))))

(defn bigparse [somestring]
  (new BigInteger somestring))

(defn to-int [str-partitions]
  (map (fn [l] (map #(bigparse (apply str %)) l)) str-partitions))

(defn remove-zeros [str-partitions]
  (remove (fn [l]
            (some #(= \0 %)
                  (map first l)))
          str-partitions))
  
(defn partitions [someseq]
  (map (partial partition-by-seq someseq)
       (lazy-cat ()))))

(defn bigparse [somestring]
  (new BigInteger somestring))

(defn squished [M message]
  (let [valid-nums (set (range (inc (bigparse M))))
        str-partitions (partitions message)
        possible-messages (count
                           (filter
                            #(every? valid-nums %)
                            (to-int
                            (remove-zeros
                             str-partitions)))
        )
    ]
    (mod possible-messages 4207849484)))

(defn -main []
  (process-file squished input output))

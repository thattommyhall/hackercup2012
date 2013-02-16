(ns hackercup.soup
    (:require [clojure.java.io :as io]))

(def input "/Users/tomh/Dropbox/Projects/hackercup/src/hackercup/soup-in.txt")
(def output "/Users/tomh/Dropbox/Projects/hackercup/src/hackercup/soup-out.txt")

(defn process-file [f input output]
  (with-open [output-file (io/writer output)
              input-file (io/reader input)]
    (doseq [output-line (map (partial format "Case #%d: %s\n" )
                             (iterate inc 1)
                             (map f (drop 1 (line-seq input-file))))]
        (do (.write output-file output-line)
            (print output-line)
            ))))

(defn count-occurances [line]
  (let [counts (map (frequencies line) (seq "HACKERCUP"))]
    (if (some nil? counts)
      0
      (apply min counts))))

(defn -main []
  (process-file count-occurances input output))

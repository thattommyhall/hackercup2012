(ns hackercup.billboards
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def input "/Users/tomh/Dropbox/Projects/hackercup/src/hackercup/billboards-in.txt")
(def output "/Users/tomh/Dropbox/Projects/hackercup/src/hackercup/billboards-out.txt")

(defn process-file [f input output]
  (with-open [output-file (io/writer output)
              input-file (io/reader input)]
    (doseq [output-line (map (partial format "Case #%d: %s\n" )
                             (iterate inc 1)
                             (map f (drop 1 (line-seq input-file))))]
        (do (.write output-file output-line)
            (print output-line)
            ))))

(defn take-row [row-width word-lengths]
  (if (empty? word-lengths)
    word-lengths
    (let [first-word (first word-lengths)]
      (if (> first-word row-width)
        word-lengths
        (recur (- row-width (inc first-word))
                  (rest word-lengths))))))

(defn fits? [w h word-size word-lengths]
  (let [columns (Math/floor (/ w word-size))
        rows (Math/floor (/ h word-size))]
    (empty? (first (drop rows
                         (iterate (partial take-row columns) word-lengths))))))
  
(defn max-size [w h word-lengths]
  (reduce #(max %1 %2)
          (for [word-size (range 1
                                 (inc (min w h)))
                :when (fits? w h word-size word-lengths)
                ]
            word-size)))
  
(defn billboards [line]
  (time (let [[w h & words] (string/split line #" ")
              wordlengths (map count words)]
          (max-size (Integer/parseInt w)
                    (Integer/parseInt h)
                    wordlengths)
          )))

(defn -main []
  (process-file billboards input output))

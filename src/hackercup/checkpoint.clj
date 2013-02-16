(ns hackercup.checkpoint
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.math.combinatorics :as combinatorics]
            [clojure.core.memoize :as memoize]
            ))

(def input "/Users/tomh/Dropbox/Projects/hackercup/src/hackercup/checkpoint-in.txt")
(def output "/Users/tomh/Dropbox/Projects/hackercup/src/hackercup/checkpoint-out.txt")

(defn process-file [f input output]
  (with-open [output-file (io/writer output)
              input-file (io/reader input)]
    (doseq [output-line (map (partial format "Case #%d: %s\n" )
                             (iterate inc 1)
                             (map f (drop 1 (line-seq input-file))))]
        (do (.write output-file output-line)
            (print output-line)
            ))))

(def factorial
  (memoize/memo (fn [n] (reduce *' (range 1 (inc n))))))

;; (def factorial
;;   (fn [n] (reduce *' (range 1 (inc n)))))


(defn num-paths [m n]
  (/ (factorial (+ m n))
     (*' (factorial m)
         (factorial n))))

(defn distance [m n]
  (+ m n))

;; (defn closest [number-of-paths]
;;   (if (= 1 number-of-paths)
;;     1
;;     (apply min (for [i (range 1 (inc number-of-paths))
;;                      j (range 1 (inc i))
;;                      :when (= (num-paths i j)
;;                               number-of-paths)]
;;                  (distance i j)))))

(defn closest [number-of-paths]
  
  (first 
   (mapcat #(concat (for [i (range %)
                          :when (= (num-paths i %)
                                   number-of-paths)]
                      (distance i %))
                    (for [j (range (inc %))
                          :when (= (num-paths % j)
                                   number-of-paths)]
                      (distance j %)))
           (iterate inc 1)))
  )
;; )


(defn factor-pairs [n]
  (for [i (range 1 (inc (Math/sqrt n)))
        :when (= 0 (mod n i))
        ]
    [i (/ n i)]))

(defn checkpoint [line]
  (let [n (bigint line)]
    (apply min (for [[m n] (factor-pairs n)]
                 (+ (closest m)
                    (closest n))))))

(defn -main []
  (process-file checkpoint input output)
  ;; (println (take 3 (closest 5)))
  )

  


(ns hackercup.auction
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def input "/Users/tomh/Dropbox/Projects/hackercup/src/hackercup/auction-in.txt")
(def output "/Users/tomh/Dropbox/Projects/hackercup/src/hackercup/auction-out.txt")

(defn process-file [f input output]
  (with-open [output-file (io/writer output)
              input-file (io/reader input)]
    (doseq [output-line (map (partial format "Case #%d: %s\n" )
                             (iterate inc 1)
                             (map f (drop 1 (line-seq input-file))))]
        (do (.write output-file output-line)
            (print output-line)
            ))))

(defn items [P1, W1, M, K, A, B, C, D]
  (defn next-p [old-p]
    (+ 1
       (mod (+ B
               (* A old-p))
            M)))
  
  (defn next-w [old-w]
    (+ 1
       (mod (+ D
               (* C old-w))
            K)))
  (map vector
       (iterate next-p P1)
       (iterate next-w W1)))

(defn pref-p? [[p1 w1] [p2 w2]]
  (and (< p1 p2)
       (<= w1 w2)))

(defn pref-w? [[p1 w1] [p2 w2]]
  (and (< w1 w2)
       (<= p1 p2)))


(defn prefered? [a b]
  (or (pref-p? a b)
      (pref-w? a b)))

;; (defn prefered? [[p1 w1] [p2 w2]]
;;   (and (<= w1 w2)
;;        (<= p1 p2)))



;; (defn best [a b]
;;   (if (prefered? a b)
;;     a
;;     b))

;; (defn worst [a b]
;;   (if (prefered? a b)
;;     b
;;     a))

(defn pref-compare [a b]
  (cond (prefered? a b)
        -1

        (prefered? b a)
        1

        :else 0))

(defn get-result [N P1 W1 M K A B C D]
  (let [products (take N (items P1 W1 M K A B C D))
        ;; bargin (reduce best products)
        ;; terrible (reduce worst products)
        ;; terrible-deals (count (remove #(prefered? % terrible) products))
        ;; bargins (count (remove #(prefered? bargin %)  products))
        ordered (sort pref-compare products)
        best (first ordered)
        worst (do (println products)
                  (println ordered)
                  (last ordered))
        bargins (count (remove #(= 0
                                   (prefered? % best))
                               ordered))
        terrible-deals (count (filter #(= 0
                                          (prefered? % worst ))
                                      ordered))
        ]
    [terrible-deals bargins]
    ))

(defn auction [line]
  (time (let [[N P1 W1 M K A B C D] (map #(Integer/parseInt %) (string/split line #" "))
              [terrible-deals bargins] (do ; (println line)
                                           (get-result N P1 W1 M K A B C D)) ]
          (str terrible-deals " " bargins))))
          


(defn -main []
  ;; (println (prices 5 1 4 5 7 1 0 1 2))
  (process-file auction input output)
  )

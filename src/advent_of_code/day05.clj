(ns advent-of-code.day05
  (:require [advent-of-code.utils :as u]))

(defn- parse-ranges
  "Parse the ranges and return a set"
  [ranges]
  (->> (u/to-lines ranges) u/parse-ranges))

(defn- between
  "Predicate to determine if `v` is within the range of `rng`"
  [v rng]
  (<= (first rng) v (last rng)))

(defn- filter-it
  "Convert the ranges and the values, then generate the answer"
  [[ranges values]]
  (let [ranges (parse-ranges ranges)
        values (->> (u/to-lines values)
                    (map parse-long))
        fresh  (fn [x]
                 (reduce (fn [_ rng]
                           (when (between x rng)
                             (reduced true)))
                         false ranges))]
    (count (filter fresh values))))

(defn part-1
  "Day 05 Part 1"
  [input]
  (->> (u/to-blocks input)
       filter-it))

(defn- overlap?
  "Test to see if the two ranges overlap to any degree"
  [[_ rng1-hi] [rng2-lo _]]
  (<= rng2-lo rng1-hi))

(defn- merge-ranges
  "Merge the two ranges into a single one"
  [[lo1 hi1] [lo2 hi2]]
  [(min lo1 lo2) (max hi1 hi2)])

(defn- count-it
  "Count all the valid members of all ranges"
  [ranges]
  (let [ranges  (sort (map vec (parse-ranges ranges)))
        rng-val (fn [[lo hi]] (inc (- hi lo)))]
    (loop [[r & rs] ranges, coll ()]
      (cond
        (nil? r)      (reduce + (map rng-val coll))
        (empty? coll) (recur rs (cons r coll))
        :else         (if (overlap? (first coll) r)
                        (recur rs
                               (cons (merge-ranges (first coll) r) (rest coll)))
                        (recur rs (cons r coll)))))))

(defn part-2
  "Day 05 Part 2"
  [input]
  (->> (u/to-blocks input)
       first
       count-it))

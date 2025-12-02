(ns advent-of-code.day02
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(defn- find-invalid
  "Find any invalid IDs in the range (inclusive)"
  [pattern [from to]]
  (for [x (range from (inc to))
        :when (re-find pattern (str x))]
    x))

(defn part-1
  "Day 02 Part 1"
  [input]
  (->> (str/split input #",")
       u/parse-ranges
       (map #(find-invalid #"^(\d+)(\1)$" %))
       flatten
       (reduce +)))

(defn part-2
  "Day 02 Part 2"
  [input]
  (->> (str/split input #",")
       u/parse-ranges
       (map #(find-invalid #"^(\d+)(?:\1)+$" %))
       flatten
       (reduce +)))

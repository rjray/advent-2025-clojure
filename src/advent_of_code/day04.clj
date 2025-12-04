(ns advent-of-code.day04
  (:require [advent-of-code.utils :as u]))

(def ^:private dirs
  [[-1 -1] [-1 0] [-1 1]
   [0 -1]         [0 1]
   [1 -1]  [1 0]  [1 1]])

(defn- accessible
  "Determine if point `pos` ([y x]) in `matrix` has <4 adjacent paper rolls"
  [matrix pos]
  (let [around (count (filter #(= \@ (get-in matrix % \.))
                              (map #(mapv + pos %) dirs)))]
    (< around 4)))

(defn- find-accessible
  "Find all the accessible squares in `matrix`"
  [matrix]
  (let [max-y (count matrix)
        max-x (count (first matrix))]
    (for [y (range max-y), x (range max-x)
          :when (and (= (get-in matrix [y x]) \@)
                     (accessible matrix [y x]))]
      [y x])))

(defn part-1
  "Day 04 Part 1"
  [input]
  (->> (u/to-matrix input)
       find-accessible
       count))

(defn- clear-out
  "Clear out accessible rolls until none are accessible. Return the count."
  [matrix]
  (loop [mat matrix, last-mat nil, cleared 0]
    (if (= mat last-mat)
      cleared
      (let [out     (find-accessible mat)
            new-mat (reduce (fn [m pos]
                              (assoc-in m pos \.)) mat out)]
        (recur new-mat mat (+ cleared (count out)))))))

(defn part-2
  "Day 04 Part 2"
  [input]
  (->> (u/to-matrix input)
       clear-out))

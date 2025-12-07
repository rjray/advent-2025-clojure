(ns advent-of-code.day07
  (:require [advent-of-code.utils :as u]))

(defn- count-splits
  "Count the number of beam splits"
  [field]
  (let [start (.indexOf (first field) \S)]
    (loop [[row & rows] (rest field), current #{start}, count 0]
      (if (nil? row)
        count
        (let [[nxt count'] (loop [[c & cs] current, nxt #{}, cnt count]
                             (cond
                               (nil? c)       (list nxt cnt)
                               (= (row c) \^) (recur cs
                                                     (conj (conj nxt (dec c))
                                                           (inc c))
                                                     (inc cnt))
                               :else          (recur cs (conj nxt c) cnt)))]
          (recur rows nxt count'))))))

(defn part-1
  "Day 07 Part 1"
  [input]
  (->> input
       u/to-matrix
       count-splits))

(defn- count-timelines
  "Count the number of 'timelines' that the particle would end up on"
  [field]
  (let [start   (.indexOf (first field) \S)
        updater (fn [a b] (+ (or a 0) b))]
    (loop [[row & rows] (rest field), current {start 1}]
      (if (nil? row)
        (reduce + (vals current))
        (let [nxt (loop [[[c cnt] & es] current, nxt {}]
                    (if (nil? c)
                      nxt
                      (if (= (row c) \^)
                        (recur es (-> nxt
                                      (update (dec c) updater cnt)
                                      (update (inc c) updater cnt)))
                        (recur es (update nxt c updater cnt)))))]
          (recur rows nxt))))))

(defn part-2
  "Day 07 Part 2"
  [input]
  (->> input
       u/to-matrix
       count-timelines))

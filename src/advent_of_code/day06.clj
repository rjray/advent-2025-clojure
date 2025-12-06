(ns advent-of-code.day06
  (:require [advent-of-code.utils :as u]
            [clojure.string :as str]))

(def ^:private op-map { "+" +, "*" *})

(defn- to-assignment
  "Convert the list-of-lists into a matrix of numbers and ops"
  [lol]
  (let [[ops & nums] (reverse lol)
        ops          (mapv op-map (u/tokenize (str/trim ops)))
        nums         (mapv u/parse-out-longs (reverse nums))]
    (u/transpose (conj nums ops))))

(defn part-1
  "Day 06 Part 1"
  [input]
  (->> (u/to-lines input)
       to-assignment
       (map reverse)
       (map (fn [[op & nums]] (apply op (reverse nums))))
       (reduce +)))

(defn- to-assignment2
  "Re-organize the content based on part 2 rules"
  [matrix]
  (let [matrix' (map #(apply str %)
                     (last (take 4 (iterate u/rotate-cw matrix))))]
    (loop [[line & lines] matrix', nums (), vals ()]
      (cond
        (nil? line)             vals
        (re-find #"^\s+$" line) (recur lines () vals)
        :else
        (let [[_ num op] (re-find #"\s*(\d+)\s*([+*])?" line)]
          (if (nil? op)
            (recur lines (cons (parse-long num) nums) vals)
            (recur lines
                   ()
                   (cons (apply (op-map op)
                                (cons (parse-long num) nums)) vals))))))))

(defn part-2
  "Day 06 Part 2"
  [input]
  (->> (u/to-matrix input)
       to-assignment2
       (reduce +)))

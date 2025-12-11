(ns advent-of-code.day11
  (:require [advent-of-code.utils :as u]))

(defn- build-graph
  "Build the graph from the sets of tokens in each line"
  [lines]
  (reduce (fn [graph tokens]
            (let [tokens (map keyword tokens)]
              (assoc graph (first tokens) (rest tokens))))
          {} lines))

(defn- memo-wrap
  "Wrap a memoized path-finder around the `graph` map"
  [graph]
  (def gp-memo
    (memoize
     (fn [node dac-seen fft-seen]
       (if (= node :out)
         (if (and dac-seen fft-seen) 1 0)
         (let [dac-seen (or dac-seen (= node :dac))
               fft-seen (or fft-seen (= node :fft))]
           (apply + (map #(gp-memo % dac-seen fft-seen) (graph node)))))))))

(defn- get-paths
  "A general DFS, using memoization."
  [dac-seen fft-seen start graph]
  (let [gp-memoized (memo-wrap graph)]
    (gp-memoized start dac-seen fft-seen)))

(defn part-1
  "Day 11 Part 1"
  [input]
  (->> (u/to-lines input)
       (map u/alphanum-tokenize)
       build-graph
       (get-paths true true :you)))

(defn part-2
  "Day 11 Part 2"
  [input]
  (->> (u/to-lines input)
       (map u/alphanum-tokenize)
       build-graph
       (get-paths false false :svr)))

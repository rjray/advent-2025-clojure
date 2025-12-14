(ns advent-of-code.day11bis
  (:require [advent-of-code.utils :as u]))

(defn- build-graph
  "Build the graph from the sets of tokens in each line"
  [lines]
  (reduce (fn [graph tokens]
            (let [tokens (map keyword tokens)]
              (assoc graph (first tokens) (rest tokens))))
          {} lines))

(def gp-memo
  (memoize
   (fn [graph start end]
     (if (= start end)
       1
       (apply + (map #(gp-memo graph % end) (graph start)))))))

(defn- get-paths
  "A general DFS, using memoization."
  [start end graph]
  (gp-memo graph start end))

(defn part-1
  "Day 11 Part 1"
  [input]
  (->> (u/to-lines input)
       (map u/alphanum-tokenize)
       build-graph
       (get-paths :you :out)))

(defn- get-order
  "Determine order of :dac and :fft and the number of paths between"
  [graph]
  (let [dac->fft (get-paths :dac :fft graph)
        fft->dac (get-paths :fft :dac graph)]
    (if (pos? dac->fft)
      (list dac->fft :dac :fft)
      (list fft->dac :fft :dac))))

(defn part-2
  "Day 11 Part 2"
  [input]
  (->> (u/to-lines input)
       (map u/alphanum-tokenize)
       build-graph
       ((fn [graph]
          (let [[segment2 B C] (get-order graph)
                segment1       (get-paths :svr B graph)
                segment3       (get-paths C :out graph)]
            (* segment1 segment2 segment3))))))

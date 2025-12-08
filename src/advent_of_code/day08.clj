(ns advent-of-code.day08
  (:require [advent-of-code.utils :as u]
            [clojure.set :as set]
            [clojure.math.combinatorics :as comb]))

(defn- dist
  "Compute the straight-line distance between the two points in 3D space"
  [[[x1 y1 z1] [x2 y2 z2]]]
  (let [x' (abs (- x2 x1))
        y' (abs (- y2 y1))
        z' (abs (- z2 z1))]
    (Math/sqrt (+ (* x' x') (* y' y') (* z' z')))))

(defn- create-distance-map
  "Create a map of all pairs of points, with their straight-line distance"
  [points]
  (list points
        (into {} (map #(vector % (dist %)) (comb/combinations points 2)))))

(defn- find-circuit
  "Find the circuit that `p` is currently in"
  [p circuits]
  (some #(when (% p) %) circuits))

(defn- join-up
  "Join the two points into a circuit, based on puzzle rules"
  [p1 p2 circuits]
  (let [c1 (find-circuit p1 circuits)
        c2 (find-circuit p2 circuits)]
    (if (= c1 c2)
      circuits
      (conj (disj circuits c1 c2) (set/union c1 c2)))))

(defn- find-circuits
  "Join the `n` pairs of closest points and return a system of circuits"
  [n [points pairs]]
  (let [closest-n (take n (sort #(compare (val %1) (val %2)) pairs))
        circuits  (set (map #(set [%]) points))]
    (loop [[[[p1 p2]] & pairs] closest-n, circuits circuits]
      (if (nil? p1)
        circuits
        (recur pairs (join-up p1 p2 circuits))))))

(defn- calc-answer
  "Calculate the part 1 answer from the resulting set of circuits"
  [circuits]
  (let [sizes (reverse (sort (map count circuits)))]
    (apply * (take 3 sizes))))

(defn part-1
  "Day 08 Part 1"
  [input & [cnt]]
  (let [cnt (or cnt 1000)]
    (->> input
         u/to-lines
         (map u/parse-out-longs)
         create-distance-map
         (find-circuits cnt)
         calc-answer)))

(defn- find-circuits2
  "Find the circuits based on part 2 rules"
  [[points pairs]]
  (let [closest  (sort #(compare (val %1) (val %2)) pairs)
        circuits (set (map #(set [%]) points))]
    (loop [[pair & pairs] closest, circuits circuits]
      (if (nil? pair)
        circuits
        (let [[[p1 p2]] pair
              circuits' (join-up p1 p2 circuits)]
          (if (= 1 (count circuits'))
            (* (first p1) (first p2))
            (recur pairs circuits')))))))

(defn part-2
  "Day 08 Part 2"
  [input]
  (->> input
       u/to-lines
       (map u/parse-out-longs)
       create-distance-map
       find-circuits2))

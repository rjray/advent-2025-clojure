(ns advent-of-code.day09
  (:require [advent-of-code.utils :as u]
            [clojure.math.combinatorics :as comb]))

(defn- get-pairs-with-area
  "Take the list of points and create all pairs. Calculate the area as well."
  [points]
  (letfn [(area [[[^long x1 ^long y1] [^long x2 ^long y2]]]
            (* (inc (abs (- x1 x2))) (inc (abs (- y1 y2)))))]
    (map #(list (area %) %) (comb/combinations points 2))))

(defn part-1
  "Day 09 Part 1"
  [input]
  (->> input
       u/parse-out-longs
       (partition 2)
       get-pairs-with-area
       (sort-by first)
       reverse
       ffirst))

(defn- get-pairs-with-area-indexed
  "Take the list of points and create all pairs. Calculate the area as well."
  [points]
  (letfn [(area [[[_ [^long x1 ^long y1]] [_ [^long x2 ^long y2]]]]
            (* (inc (abs (- x1 x2))) (inc (abs (- y1 y2)))))]
    (map #(list (area %) (last (first %)) (last (last %)) (ffirst %))
         (comb/combinations (map-indexed list points) 2))))

(defn- check-for-area
  "Check to see if the given tuple satisfies the problem constraints"
  [n points [area point1 point2 index]]
  (let [[left right] (sort (map first (list point1 point2)))
        [lo hi]      (sort (map last (list point1 point2)))
        [x2 y2]      point1]
    (loop [[i & is] (range n), x2 x2, y2 y2, break false]
      (cond
        break    nil
        (nil? i) area
        :else    (let [[x1 y1]     [x2 y2]
                       [x2 y2]     (points (mod (+ index i) n))
                       [xmin xmax] (if (< x1 x2) [x1 x2] [x2 x1])
                       [ymin ymax] (if (< y1 y2) [y1 y2] [y2 y1])
                       bool1       (< left x1 right)
                       bool2       (< lo y1 hi)]
                   (if (or (and bool1 bool2)
                           (and bool2
                                (<= xmin left)
                                (< left xmax)
                                (< xmin right)
                                (<= right xmax))
                           (and bool1
                                (<= ymin lo)
                                (< lo ymax)
                                (< ymin hi)
                                (<= hi ymax)))
                     (recur is x2 y2 true)
                     (recur is x2 y2 false)))))))

(defn- find-max-area
  "Find the area value that is the largest while being within the polygon"
  [points indexed-coords]
  (let [n (count points)]
    (loop [[tuple & indexed] indexed-coords, max-area nil]
      (cond
        (not (nil? max-area)) max-area
        (nil? tuple)          "No answer found!"
        :else                 (recur indexed
                                     (check-for-area n points tuple))))))

(defn part-2
  "Day 09 Part 2"
  [input]
  (let [points  (->> input u/parse-out-longs (partition 2))
        pointsv (vec points)
        pairs   (reverse (sort-by first (get-pairs-with-area-indexed points)))]
    (find-max-area pointsv pairs)))

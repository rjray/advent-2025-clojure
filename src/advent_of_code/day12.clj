(ns advent-of-code.day12
  (:require [advent-of-code.utils :as u]))

(defn- setup
  "Parse the input blocks into the presents and the trees"
  [blocks]
  (letfn [(parse-tree [tree] (let [[x y & counts] (u/parse-out-longs tree)]
                               {:x x, :y y, :counts (vec counts)}))
          (parse-present [present] (count (re-seq #"#" present)))]
    (let [[tree-data & present-data] (reverse blocks)]
      {:trees    (map parse-tree (u/to-lines tree-data))
       :presents (map parse-present (reverse present-data))})))

(defn- test-fit
  "Try a simple math-based approach to seeing if they fit"
  [pixels {:keys [x y counts]}]
  (let [area       (* x y)
        min-needed (apply + (map * counts pixels))
        total-cnt  (reduce + counts)]
    (cond
      (> min-needed area)                      false
      (<= total-cnt (* (quot x 3) (quot y 3))) true
      ;; If both of the above conditions were false, assume it wouldn't fit
      :else                                    false)))

(defn- simple-solve
  "Try the yes/no/??? approach based on pixel counts"
  [{:keys [trees presents]}]
  (count (filter (partial test-fit presents) trees)))

(defn part-1
  "Day 12 Part 1"
  [input]
  (->> (u/to-blocks input)
       setup
       simple-solve))

(defn part-2
  "Day 12 Part 2"
  [input]
  "Implement this part")

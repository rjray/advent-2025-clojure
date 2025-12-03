(ns advent-of-code.day03
  (:require [advent-of-code.utils :as u]))

(defn- find-big
  "Find the first occurrence of the largest number in the vector `bank`"
  [bank first-digit]
  (let [size (count bank)
        size (if first-digit (- size 2) size)]
    (reduce (fn [_ n]
              (let [pos (.indexOf bank n)]
                (when (<= 0 pos size)
                  (reduced (list n pos))))) nil (range 9 0 -1))))

(defn- max-joltage
  "Find the maximum 'joltage' that can be had by activating two batteries"
  [bank]
  (let [values      (vec (u/->digits bank))
        [dig-1 pos] (find-big values true)
        dig-2       (first (let [values' (subvec values (inc pos))]
                             (find-big values' false)))]
    (+ (* 10 dig-1) dig-2)))

(defn part-1
  "Day 03 Part 1"
  [input]
  (->> (u/to-lines input)
       (map max-joltage)
       (reduce +)))

(defn- fill-inner
  "Fill an inner row of the dynamic-programming table"
  [dp i bank picks]
  (loop [[j & js] (range 1 (inc picks)), dp dp]
    (if (nil? j)
      dp
      (let [skip (get-in dp [(inc i) j])
            pick (+' (bigint (*' (bank i) (Math/pow 10 (dec j))))
                     (get-in dp [(inc i) (dec j)]))]
        (recur js (assoc-in dp [i j] (max skip pick)))))))

(defn- max-joltage2
  "A different approach for 12 batteries instead of 2"
  [picks index bank]
  (let [bank    (vec (u/->digits bank))
        n       (count bank)
        neg-inf (- (bigint (Math/pow 10 18)))
        dp      (vec (repeat (inc n) (vec (repeat (inc picks) neg-inf))))
        dp      (reduce (fn [dp' i] (assoc-in dp' [i 0] 0)) dp (range (inc n)))]
    (loop [[i & is] (range (dec n) (dec index) -1), dp dp]
      (if (nil? i)
        (get-in dp [index picks])
        (recur is (fill-inner dp i bank picks))))))

(defn part-2
  "Day 03 Part 2"
  [input]
  (->> (u/to-lines input)
       (map #(max-joltage2 12 0 %))
       (reduce +')))

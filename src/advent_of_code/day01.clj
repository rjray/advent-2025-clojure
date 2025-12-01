(ns advent-of-code.day01
  (:require [advent-of-code.utils :as u]))

(defn- line2pair
  "Turn one line into a direction+number"
  [line]
  (let [[ch num] (rest (re-find #"([LR])(\d+)" line))]
    (list (keyword ch) (parse-long num))))

(defn- spin-stuff
  "Spin the dial, count the number of times it lands on zero"
  [start lines]
  (let [lines (map line2pair lines)
        ops   {:R +, :L -}]
    (loop [[pair & pairs] lines, current start, zeros 0]
      (if (nil? pair)
        zeros
        (let [[dir cnt] pair
              current'  (mod ((ops dir) current cnt) 100)]
          (if (zero? current')
            (recur pairs current' (inc zeros))
            (recur pairs current' zeros)))))))

(defn part-1
  "Day 01 Part 1"
  [input]
  (->> (u/to-lines input)
       (spin-stuff 50)))

(defn- spin
  "Spin the dial"
  [current step times]
  (loop [current current, n times, zs 0]
    (if (zero? n)
      (list current zs)
      (let [current' (mod (+ current step) 100)
            zs'      (if (zero? current') (inc zs) zs)]
        (recur current' (dec n) zs')))))

(defn- spin-stuff2
  "Spin the dial, counting every time the dial lands on or passes zero"
  [lines]
  (let [lines (map line2pair lines)
        ops   {:R 1, :L -1}]
    (loop [[pair & pairs] lines, current 50, zeros 0]
      (if (nil? pair)
        zeros
        (let [dir (ops (first pair))
              len (last pair)
              [cur' zs] (spin current dir len)]
          (recur pairs cur' (+ zeros zs)))))))

(defn part-2
"Day 01 Part 2"
[input]
(->> (u/to-lines input)
     spin-stuff2))

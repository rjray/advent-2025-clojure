(ns advent-of-code.day10
  (:require [advent-of-code.utils :as u]
            [clojure.math.combinatorics :as comb]
            [fastmath.optimization :as opt]
            [fastmath.core :as m]))

(def ^:private bits {"." 0, "#" 1})

(defn- to-machine
  "Turn list of tokens into a machine description"
  [tokens]
  (let [lights  (mapv bits (re-seq #"[.#]" (first tokens)))
        joltage (vec (u/parse-out-longs (last tokens)))
        buttons (mapv #(vec (u/parse-out-longs %)) (drop-last (rest tokens)))]
    {:lights lights
     :joltage joltage
     :buttons buttons}))

(defn- setup
  "Set up a button as a bit-mask vector"
  [template button]
  (reduce (fn [bits idx] (assoc bits idx 1)) template button))

(defn- solves?
  "Does the given series of button-presses solve the system?"
  [init target series]
  (let [result (reduce (fn [lights button]
                         (mapv #(bit-xor %1 %2) lights (setup init button)))
                       init series)]
    (= result target)))

(defn- solve
  "'Solve' the given machine definition"
  [{:keys [lights buttons]}]
  (let [target lights
        init   (vec (repeat (count lights) 0))]
    (loop [n 1, solution nil]
      (if (not (nil? solution))
        solution
        (let [choices (comb/selections buttons n)
              soln    (first (filter #(solves? init target %) choices))]
          (recur (inc n) soln))))))

(defn part-1
  "Day 10 Part 1"
  [input]
  (->> (u/to-lines input)
       (map u/tokenize)
       (map to-machine)
       (map solve)
       (map count)
       (apply +)))

;; Use `near-zero?` from fastmath to see if this is "close-enough" to an int
(defn- almost? [^double value] (m/near-zero? (- value (m/round value))))

(defn- lin-prog
  "Use the `linear-optimization` fn from fastmath to try"
  [target system]
  (try
    (opt/linear-optimization target system {:non-negative? true})
    (catch Exception _ [[] ##Inf])))

(defn- subdivide
  "Try to subdivide the problem into two cases. Subdivide recursively as needed"
  [target system coeffs]
  (let [[[idx z]]       (->> (map-indexed vector coeffs)
                             (filter (comp (complement almost?) second)))
        branch-coeff    (assoc (vec (repeat (count coeffs) 0)) idx 1)
        ;; Add new constraints using the branch coefficient and upper/lower z
        system1         (conj system branch-coeff :<= (m/floor z))
        system2         (conj system branch-coeff :>= (m/ceil z))
        [coeffs1 soln1] (lin-prog target system1)
        [coeffs2 soln2] (lin-prog target system2)]
    (min (if (every? almost? coeffs1)
           soln1 (subdivide target system1 coeffs1))
         (if (every? almost? coeffs2)
           soln2 (subdivide target system2 coeffs2)))))

(defn- int-lin-prog
  "Use the `fastmath` library with some integer coercion to solve as a system"
  [{:keys [lights buttons joltage]}]
  (let [blank         (vec (repeat (count lights) 0))
        buttons'      (mapv (partial setup blank) buttons)
        btn-cnt       (count buttons')
        target        (conj (vec (repeat btn-cnt 1)) 0)
        system        (vec (->> (apply map vector buttons')
                                (mapcat (fn [j v] [v := j]) joltage)))
        [coeffs soln] (lin-prog target system)]
    (m/round (if (every? almost? coeffs)
               soln
               (subdivide target system coeffs)))))

(defn part-2
  "Day 10 Part 2"
  [input]
  (->> (u/to-lines input)
       (map u/tokenize)
       (map to-machine)
       (map int-lin-prog)
       (apply +)))

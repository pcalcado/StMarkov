(ns stmarkov)

;;TODO: how can this be made less stateful?
(defn add-succ! [occurences-ref previous succ]
  (dosync
   (let [current-all-succ (get @occurences-ref previous {})
         current-for-desired-succ (get current-all-succ succ 0)
         changed-all-succ (assoc current-all-succ succ (inc  current-for-desired-succ))]
     (alter occurences-ref
           (fn [o] (assoc o previous changed-all-succ))))))

(defn occurences-count [words]
  (let [occurences (ref {})]
   (reduce
    (fn [previous current]
      (add-succ! occurences previous current)
      current)
    words)
   @occurences))

(defn- merge-counts [count1 count2]
  (merge-with + count1 count2))

(defn merge-occurences-count [one another]
  (merge-with merge-counts one another))


(defn- to-map [key-value-pairs]
  (reduce
   (fn [m [k v]] (assoc m k v))
   {}
   key-value-pairs))

(defn to-probability [[word occurences]]
  (let [total (reduce + (vals occurences))
        probability-fn (fn [[follower count]] [follower (/ count total)])
        as-propability  (to-map (map probability-fn occurences))]
    [word as-propability]))

;;TODO; please simplify me
(defn- add-to-ranges [ranges probability]
  (let [prob-as-int (int (* 100 probability))]
    (conj ranges (cond
                  (empty? ranges) [1 prob-as-int]
                  :else (let [previous-maximum (second (last ranges))
                              start-range (+ previous-maximum 1)
                              end-range (+ start-range (dec prob-as-int))]
                          [start-range end-range])))))

(defn to-ranges [[word followers-probability]]
  [word (to-map (map vector
                     (keys followers-probability)
                     (reduce add-to-ranges [] (vals followers-probability))))])

(defn- to-verification-fn [[word [from to]]]
  (fn [a-number]
    (if (and (>= a-number from) (<= a-number to)) word nil)))

(defn pick-follower [number followers-as-ranges]
  (some #(% number) (map to-verification-fn followers-as-ranges)))

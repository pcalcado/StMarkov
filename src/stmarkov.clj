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


(defn- into-map [m [k v]] (assoc m k v))

(defn to-probability [[word occurences]]
  (let [total (reduce + (vals occurences))
        probability-fn (fn [[follower count]] [follower (/ count total)])
        as-propability  (reduce into-map {} (map probability-fn occurences))]
    [word as-propability]))

(ns t-stmarkov
  (:use (midje sweet)
        stmarkov))

(facts "about counting the successors of a given leter"
       (fact "it adds a letter as succ to some other"
             (let [occurences (ref {})]
               (add-succ! occurences "a" "b")
               (add-succ! occurences "a" "c")
               (add-succ! occurences "a" "d")
               (add-succ! occurences "a" "a")
               (add-succ! occurences "a" "b")
               (add-succ! occurences "d" "a")

               @occurences => {"a" {"a" 1
                                   "b" 2
                                   "c" 1
                                   "d" 1}
                              "d" {"a" 1}})))


(facts "about processing a sentence"
       (fact "it counts the number of times a word was followed by another"
             (let [word-stream ["a" "b" "c" "a" "c" "d" "e" "a" "b"]
                   expected-dump {"a" {"b" 2
                                         "c" 1}
                                    "b" {"c" 1}
                                    "c" {"a" 1
                                         "d" 1}
                                    "d" {"e" 1}
                                    "e" {"a" 1}}]
               (occurences-count word-stream) => expected-dump)))

(facts "about merging occurences count"
       (fact "it consolidates counts from multiple maps"
             (let [count-list (list {:a {:b 1
                                    :c 2}
                                :b {:a 1
                                    :d 1}
                                :z {:x 1}}
                               {:a {:d 10
                                    :c 20}
                                :z {:z 30}})                   
                   expected {:a {:b 1
                                 :c 22
                                 :d 10}
                             :b {:a 1
                                 :d 1}
                             :z {:x 1
                                 :z 30}}]
               (reduce merge-occurences-count count-list) => expected)))

(facts "about calculating the probability of each follower"
       (fact "exact probabilities are calculated"
             (to-probability [:a {:b 666}]) => [:a {:b 1}]
             (to-probability [:a {:b 99 :c 1}]) => [:a {:b 0.99 :c 0.01}]
             (to-probability [:a {:b 1 :c 1}]) => [:a {:b 0.5 :c 0.5}])             

       (fact "no followers means no probabilities"
             (to-probability [:a {}]) => [:a {}])

       (fact "inexact probablities are calculated"
             (to-probability [:a {:b 1 :c 1 :d 1}]) => [:a {:b (/ 1 3) :c (/ 1 3) :d (/ 1 3)}]))

(facts "about converting probability to ranges"
       (fact "ranges are calculted"
             (to-ranges [:c {:a 0.50 :b 0.50}]) => [:c {:a [1 50] :b [51 100]}]
             (to-ranges [:d {:a 1}]) => [:d {:a [1 100]}]
             (to-ranges [:f {:a 0.5 :b 0.3 :c 0.2}]) => [:f {:a [1 50] :b [51 80] :c [81 100]}]))

(facts "about picking a follower based on a number"
       (fact "it picks the correct option"
             (pick-follower 1 {:a [1 50] :b [51 100]}) => :a
             (pick-follower 99 {:a [1 100]}) => :a
             (pick-follower 51 {:a [1 50] :b [51 100]}) => :b
             (pick-follower 100 {:a [1 50] :b [51 80] :c [81 100]}) => :c))

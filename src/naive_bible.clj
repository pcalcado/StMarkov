(ns naive-bible
  (:use [stmarkov])
  (:require [stmarkov.pipelines.online-bible :as online-bible])
  (:require [stmarkov.pipelines.tokenising :as tokenising])
  (:import [java.io BufferedReader FileReader File]))

(def data-dir "data/OnlineBible/kjv/")

(def books (filter #(.isDirectory %) (.listFiles (File. data-dir))))

(def chapters (map (fn [d] (filter #(.isFile %) (.listFiles d))) books))

(defn occurences-in-chapter [file]
  (reduce merge-occurences-count
          (map occurences-count
               (map tokenising/process-sentence
                    (online-bible/make-parser (line-seq (BufferedReader. (FileReader. file))))))))

(defn process-chapter [current-occurences chapter]
  (merge-occurences-count current-occurences (occurences-in-chapter chapter)))

(defn ingest []
  (reduce process-chapter {} (flatten chapters)))

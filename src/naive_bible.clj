(ns naive-bible
  (:use [stmarkov])
  (:require [stmarkov.pipelines.online-bible :as online-bible])
  (:require [stmarkov.pipelines.tokenising :as tokenising])
  (:import [java.io BufferedReader FileReader]))

(def data-dir "data/OnlineBible/kjv/")

(def books (filter #(.isDirectory %) (.listFiles (java.io.File. data-dir))))

(def chapters (map (fn [d] (filter #(.isFile %) (.listFiles d))) books))

(defn occurences-in-chapter [file]
  (reduce merge-occurences-count
          (map occurences-count
               (map tokenising/process-sentence
                    (online-bible/make-parser (line-seq (BufferedReader. (FileReader. file))))))))



()

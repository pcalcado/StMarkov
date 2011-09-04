(ns stmarkov.pipelines.tokenising)

(def semicolon-token (keyword ";"))
(def colon-token (keyword ":"))
(def comma-token (keyword ","))
(def period-token (keyword "."))

(defn split-on-whitespace [text]
  (seq (.split text "\\s+")))

;;this is very naive
(defn- char-by-char [text]
  (reduce (fn [acc c] (conj acc (cond
                                (= c \;) semicolon-token 
                                (= c \:) colon-token
                                (= c \,) comma-token 
                                (= c \.) period-token 
                                :else  c)))
          []
          text))

(defn split-on-ponctuation [text]
  (map
   (fn [x] (if (keyword? (first x)) (first x) (apply str x)))
   (partition-by keyword? (char-by-char text))))

(defn wrap-sentence [sentence]
  (concat  [:bos] sentence [:eos]))

(defn as-lowercase [word]
  (.toLowerCase word))

(defn process-sentence [sentence]
  (wrap-sentence (mapcat split-on-ponctuation (map as-lowercase (split-on-whitespace sentence)))))

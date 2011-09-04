(ns stmarkov.pipelines.online-bible
  (:use (stmarkov)))

(def versicle-regexp #"^\S(.+)(<tr>|</table>)$")

(defn make-parser [raw-stream]
  (map
   #(.replaceAll % " (<tr>|</table>)" "\n")
   (filter #(re-matches versicle-regexp %) raw-stream)))



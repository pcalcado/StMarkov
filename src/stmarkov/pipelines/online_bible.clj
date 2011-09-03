(ns stmarkov.pipelines.online-bible)

(def versicle-regexp #"^\S(.+)(<tr>|</table>)$")

(defn make-online-bible-parser [raw-stream]
  (map
   #(.replaceAll % " (<tr>|</table>)" "\n")
   (filter #(re-matches versicle-regexp %) raw-stream)))

(ns stmarkov.pipelines.t-online-bible
  (:use (midje sweet)
        (stmarkov))
  (:require [stmarkov.pipelines.online-bible :as online-bible])
  (:import [java.io StringBufferInputStream BufferedReader InputStreamReader]))

(defn genesis-01-html-reader [] (BufferedReader.
                                 (InputStreamReader.
                                  (StringBufferInputStream.
                                   (slurp "data/OnlineBible/kjv/gen/gen1.htm")))))

(def genesis-01-versicles ["In the beginning God created the heaven and the earth.\n"
                           "And the earth was without form, and void; and darkness was upon the face of the deep. And the Spirit of God moved upon the face of the waters.\n"
                           "And God said, Let there be light: and there was light.\n"
                           "And God saw the light, that it was good: and God divided the light from the darkness.\n"
                           "And God called the light Day, and the darkness he called Night. And the evening and the morning were the first day.\n"
                           "And God said, Let there be a firmament in the midst of the waters, and let it divide the waters from the waters.\n"
                           "And God made the firmament, and divided the waters which were under the firmament from the waters which were above the firmament: and it was so.\n"
                           "And God called the firmament Heaven. And the evening and the morning were the second day.\n"
                           "And God said, Let the waters under the heaven be gathered together unto one place, and let the dry land appear: and it was so.\n"
                           "And God called the dry land Earth; and the gathering together of the waters called he Seas: and God saw that it was good.\n"
                           "And God said, Let the earth bring forth grass, the herb yielding seed, and the fruit tree yielding fruit after his kind, whose seed is in itself, upon the earth: and it was so.\n"
                           "And the earth brought forth grass, and herb yielding seed after his kind, and the tree yielding fruit, whose seed was in itself, after his kind: and God saw that it was good.\n"
                           "And the evening and the morning were the third day.\n"
                           "And God said, Let there be lights in the firmament of the heaven to divide the day from the night; and let them be for signs, and for seasons, and for days, and years:\n"
                           "And let them be for lights in the firmament of the heaven to give light upon the earth: and it was so.\n"
                           "And God made two great lights; the greater light to rule the day, and the lesser light to rule the night: he made the stars also.\n"
                           "And God set them in the firmament of the heaven to give light upon the earth,\n"
                           "And to rule over the day and over the night, and to divide the light from the darkness: and God saw that it was good.\n"
                           "And the evening and the morning were the fourth day.\n"
                           "And God said, Let the waters bring forth abundantly the moving creature that hath life, and fowl that may fly above the earth in the open firmament of heaven.\n"
                           "And God created great whales, and every living creature that moveth, which the waters brought forth abundantly, after their kind, and every winged fowl after his kind: and God saw that it was good.\n"
                           "And God blessed them, saying, Be fruitful, and multiply, and fill the waters in the seas, and let fowl multiply in the earth.\n"
                           "And the evening and the morning were the fifth day.\n"
                           "And God said, Let the earth bring forth the living creature after his kind, cattle, and creeping thing, and beast of the earth after his kind: and it was so.\n"
                           "And God made the beast of the earth after his kind, and cattle after their kind, and every thing that creepeth upon the earth after his kind: and God saw that it was good.\n"
                           "And God said, Let us make man in our image, after our likeness: and let them have dominion over the fish of the sea, and over the fowl of the air, and over the cattle, and over all the earth, and over every creeping thing that creepeth upon the earth.\n"
                           "So God created man in his own image, in the image of God created he him; male and female created he them.\n"
                           "And God blessed them, and God said unto them, Be fruitful, and multiply, and replenish the earth, and subdue it: and have dominion over the fish of the sea, and over the fowl of the air, and over every living thing that moveth upon the earth.\n"
                           "And God said, Behold, I have given you every herb bearing seed, which is upon the face of all the earth, and every tree, in the which is the fruit of a tree yielding seed; to you it shall be for meat.\n"
                           "And to every beast of the earth, and to every fowl of the air, and to every thing that creepeth upon the earth, wherein there is life, I have given every green herb for meat: and it was so.\n"
                           "And God saw every thing that he had made, and, behold, it was very good. And the evening and the morning were the sixth day.\n"])


(facts "about reading the HTML file"
       (fact "it extracts only the versicles"
             (let [raw-stream (line-seq (genesis-01-html-reader))
                   parser (online-bible/make-parser raw-stream)]
               (reduce (fn [a b] (and a b)) (map = genesis-01-versicles parser)) => true)))



(ns stmarkov.pipelines.t-tokenising
  (:use (midje sweet)
        (stmarkov.pipelines tokenising)))

(facts "about whitespace splitting"
       (fact "it splits only on white spaces"
             (split-on-whitespace "aa bb c") => ["aa" "bb" "c"]
             (split-on-whitespace "a  b          c") => ["a" "b" "c"]))

(facts "about sentences"
       (fact "it marks the beginning and ending of a sentence"
             (wrap-sentence ["aa" "bb"]) => [:bos "aa" "bb" :eos]
             (wrap-sentence (list "la" "lb")) => (list :bos "la" "lb" :eos)))

(facts "about ponctuation"
       (fact "it converts ponctuation to special tokens"
             (split-on-ponctuation "and;")   => ["and" semicolon-token]
             (split-on-ponctuation "i")      => ["i"]
             (split-on-ponctuation "don't.") => ["don't" period-token]
             (split-on-ponctuation "think,") => ["think" comma-token]
             (split-on-ponctuation "this:")  => ["this" colon-token]
             (split-on-ponctuation "this: or that")  => ["this" colon-token " or that"]
             (split-on-ponctuation "cool.")  => ["cool" period-token]))

(facts "about normalising the text"
       (fact "it makes everything lower case"
             (map as-lowercase ["Aa" "bB" "CC" "dd" "E'e" "f"]) =>  ["aa" "bb" "cc" "dd" "e'e" "f"]))

(facts "about processing a sentence"
       (let [sentence "And God blessed them, and God said unto them, Be fruitful, and multiply, and replenish the earth, and subdue it: and have dominion over the fish of the sea, and over the fowl of the air, and over every living thing that moveth upon the earth.\n"
             expected-token-stream [:bos "and" "god" "blessed" "them" comma-token
                                    "and" "god" "said" "unto" "them" comma-token
                                    "be" "fruitful" comma-token "and" "multiply"
                                    comma-token "and" "replenish" "the" "earth"
                                    comma-token "and" "subdue" "it" colon-token
                                    "and" "have" "dominion" "over" "the" "fish"
                                    "of" "the" "sea" comma-token "and" "over" "the"
                                    "fowl" "of" "the" "air" comma-token "and" "over"
                                    "every" "living" "thing" "that" "moveth" "upon"
                                    "the" "earth" period-token :eos]]         
        (process-sentence sentence) => expected-token-stream))

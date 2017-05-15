(ns awesome.db
  (:require [clojure.set :as set]))

;------------------ Setup Helper Functions

(defn unique-random-numbers [n]
  (let [a-set (set (take n (repeatedly #(rand-int n))))]
    (concat a-set (set/difference (set (take n (range)))
                                  a-set))))

(defn board-setup [cards]
  (let [num (/ cards 2)
        board (into
                (unique-random-numbers num)
                (unique-random-numbers num))]
    (vec board)))

;------------------- db

(def default-db
  (let [cards 30]
  {:name "Awesome Landmark Concentration"
   :card-back "/img/globe.png"
   :number-of-cards cards
   :active-card 0
   :selected #{}
   :matches #{}
   :board (board-setup cards)
   :cards { 0 {:name "Big Ben" :img "/img/big-ben.png" :description "A big clock in London"}
            1 {:name "Arc de Triomphe" :img "/img/arc-de-triomphe.png" :description "A big arch in Paris"}
            2 {:name "Capitol Building" :img "/img/capitol.png" :description "A big dome in Washington"}
            3 {:name "Burj al-arab" :img "/img/burj-al-arab.png" :description "A big building in Dubai"}
            4 {:name "Chichen Itza" :img "/img/chichen-itza.png" :description "A big pyramid in Mexico"}
            5 {:name "Christ the Redeemer" :img "/img/christ-the-redeemer.png" :description "A big Jesus in Brazil"}
            6 {:name "Eiffel Tower" :img "/img/eiffel-tower.png" :description "A big tower in Paris"}
            7 {:name "Golden Gate Bridge" :img "/img/golden-gate-bridge.png" :description "A big bridge in San Francisco"}
            8 {:name "Great Buddha" :img "/img/great-buddha-of-thailand.png" :description "A big Buddha in Thailand"}
            9 {:name "Great Wall of China" :img "/img/great-wall-of-china.png" :description "A big wall in China"}
            10 {:name "Stonehenge" :img "/img/stonehenge.png" :description "Big rocks in England"}
            11 {:name "Pyramids" :img "/img/pyramids.png" :description "Big pyramids in Egypt"}
            12 {:name "Leaning Tower" :img "/img/leaning-tower-of-pisa.png" :description "Big crooked tower in Italy"}
            13 {:name "Statue of Liberty" :img "/img/statue-of-liberty.png" :description "Big statue in New York"}
            14 {:name "Moais" :img "/img/moais.png" :description "Big heads on Easter Island"}}}))




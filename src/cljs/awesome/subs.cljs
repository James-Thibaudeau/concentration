(ns awesome.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]
            [clojure.set :as set]))

;----------------- Subscriptions

(re-frame/reg-sub
  :name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  :card-back
  (fn [db]
    (:card-back db)))

(re-frame/reg-sub
  :number-of-cards
  (fn [db]
    (:number-of-cards db)))

(re-frame/reg-sub
  :matches
  (fn [db]
    (/ (count (:matches db)) 2)))

(re-frame/reg-sub
  :active-card
  (fn [db]
    (:active-card db)))

(re-frame/reg-sub
  :cards
  (fn [db]
    (:cards db)))

(re-frame/reg-sub
  :win
  (fn [db]
    (= (/ (count (:matches db)) 2) (/ (:number-of-cards db) 2))))

(re-frame/reg-sub
  :card
  (fn [db [_ id]]
    (let [image-id (get-in db [:board id])
          card-info (get-in db [:cards image-id])]
    {:revealed? (or (not (nil? (get-in db [:matches id])))  (not (nil? (get-in db [:selected id]))))
     :image (get card-info :img)})))


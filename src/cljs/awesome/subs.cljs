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
    (:matches db)))

(re-frame/reg-sub
  :active-card
  (fn [db]
    (:active-card db)))

(re-frame/reg-sub
  :cards
  (fn [db]
    (:cards db)))

(re-frame/reg-sub
  :board-setup
  (fn [db]
    (:board db)))

(re-frame/reg-sub
  :win
  (fn [db]
    (:win db)))

(re-frame/reg-sub
  :get-checking-count
  (fn [db]
    (count (:checking-match db))))

(re-frame/reg-sub
  :revealed?
  (fn [db [_ id]]
    (get (:flipped-cards db) id)))


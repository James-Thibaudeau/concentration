(ns awesome.events
  (:require [re-frame.core :as re-frame]
            [awesome.db :as db]))

;------------------ Events

(re-frame/reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/reg-event-fx
  :new-game
  (fn [{:keys [db]} _]
    {:db (assoc db/default-db :board (db/board-setup (:number-of-cards db)))}))

(re-frame/reg-event-db
  :choose-card
  (fn [db [_ id]]
    (assoc db :active-card id)))
;
(re-frame/reg-event-db
  :no-match
  (fn [db _]
    (assoc db :selected #{})))

(re-frame/reg-event-fx
  :check-match
  (fn [{:keys [db]} _]
    (when (= (count (:selected db)) 2)
      (let [selected (vec (:selected db))
            img-one (get-in db [:board (first selected)])
            img-two (get-in db [:board (second selected)])]
        (if (= img-one img-two)
          {:db (assoc db
                 :selected #{}
                 :matches (apply conj (:matches db) (:selected db)))}
          {:dispatch-later [{:ms 1100 :dispatch [:no-match]}]})))))

(re-frame/reg-event-fx
  :add-checking
  (fn [{:keys [db]} [_ grid-id image-id]]
    (if (< (count (:checking-match db)) 2)
      {:db (assoc db :checking-match (conj (:checking-match db) [grid-id image-id]))
       :dispatch [:check-match]})))

(re-frame/reg-event-fx
  :select-card
  (fn [{:keys [db]} [_ card-index]]
    (when (< (count (:selected db)) 2)
      {:db (assoc db :selected (conj (:selected db) card-index))
       :dispatch [:check-match]})))



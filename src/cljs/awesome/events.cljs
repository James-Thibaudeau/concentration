(ns awesome.events
  (:require [re-frame.core :as re-frame]
            [awesome.db :as db]))

;------------------ Events

(re-frame/reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/reg-event-fx
  :check-win
  (fn [cofx _]
    (let [matches (get (:db cofx) :matches)
          db (:db cofx)]
      (prn matches)
      (when (= matches 15)
        {:db (assoc db :win true)}))
    ))

(re-frame/reg-event-db
  :reset-win
  (fn [db _]
    (assoc db :win false)))

(re-frame/reg-event-fx
  :match
  (fn [cofx _]
    (let [db (:db cofx)]
      {:db (update db :matches inc)
       :dispatch [:check-win]}
      )))

(re-frame/reg-event-db
  :reset-matches
  (fn [db _]
    (assoc db :matches 0)))

(re-frame/reg-event-db
  :choose-card
  (fn [db [_ id]]
    (assoc db :active-card id)))

(re-frame/reg-event-db
  :shuffle
  (fn [db _]
    (assoc db :board (db/board-setup (:number-of-cards db)))))


(re-frame/reg-event-db
  :reset-flipped-cards
  (fn [db _]
    (assoc db :flipped-cards (db/flipped-cards (:number-of-cards db)))))

(re-frame/reg-event-db
  :reset-checking-match
  (fn [db _]
    (assoc db :checking-match [])))

(re-frame/reg-event-fx
  :reset
  (fn [cofx _]
    {:dispatch-n (list [:shuffle]
                       [:reset-matches]
                       [:reset-flipped-cards]
                       [:reset-checking-match]
                       [:reset-win])}))

(re-frame/reg-event-db
  :flip
  (fn [db [_ id]]
    (update-in db [:flipped-cards id] not)))

(re-frame/reg-event-fx
  :check-match
  (fn [cofx _]
    (let [db (:db cofx)]
      (if (= 2 (count (:checking-match db)))
        (let [item1 (get-in db [:checking-match 0])
              item2 (get-in db [:checking-match 1])]
          (if (= (get item1 1) (get item2 1))
            {:dispatch-n (list [:match] [:reset-checking-match])}
            {:dispatch-later [{:ms 1500 :dispatch [:reset-checking-match]}
                              {:ms 1500 :dispatch [:flip (first item1)]}
                              {:ms 1500 :dispatch [:flip (first item2)]}]}))))))

(re-frame/reg-event-fx
  :add-checking
  (fn [{:keys [db]} [_ grid-id image-id]]
    (if (< (count (:checking-match db)) 2)
      {:db       (assoc db :checking-match (conj (:checking-match db) [grid-id image-id]))
       :dispatch [:check-match]})))

(re-frame/reg-event-fx
  :flip-w-check
  (fn [{:keys [db]} [_ grid-id image-id]]
    (let [count (count (:checking-match db))
          active (get-in db [:flipped-cards grid-id])]
      (if (and (< count 2) (not active))
        {:db       (update-in db [:flipped-cards grid-id] not)
         :dispatch [:add-checking grid-id image-id]}))))



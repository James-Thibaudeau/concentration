(ns awesome.views
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame]
            [soda-ash.core :as sa]
            [clojure.set :as set]))

;--------------------- Header/Menu/Footer/Matches

(defn about-modal []
  [sa/Modal {:trigger (r/as-element [sa/DropdownItem "About"]) :dimmer "blurring"}
   [sa/ModalHeader "About"]
   [sa/ModalContent {:image true}
    [sa/ModalDescription
     [sa/Header "This game is dedicated to the Taj Mahal"]
     [:p "Demo project to learn Reagent/Re-frame and Clojurescript"]
     [:p "Created by James Thibaudeau"]]
    [sa/Image {:wrapped true :size "small" :src "img/taj-mahal.png"}]]])

(defn menu-bar []
  [sa/Menu {:size "small" :compact true :borderless true}
   [sa/Dropdown {:text "Menu" :item true}
    [sa/DropdownMenu
     [sa/DropdownItem {:on-click (fn [] (re-frame/dispatch [:new-game]))} "New Game"]
     [about-modal]]]])

(defn header [title]
  [:div {:key "title-view" :id "title"}
   [sa/Header {:as "h1"} title]])

(defn footer []
  [sa/Header {:id "footer" :as "h5"}
   "All Rights Reserved 2017"])

(defn matches [matches]
  [sa/Header {:as "h2" :key "matches-view" :id "matches"} (str "Matches: " matches)])

(defn win-message []
  (let [visible? (re-frame/subscribe [:win])]
    (fn []
      [sa/Message {:icon true
                   :floating  true
                   :positive  true
                   :visible   @visible?
                   :hidden    (not @visible?)
                   :onDismiss (fn [] (re-frame/dispatch [:new-game]))}
       [sa/Icon {:name "check"}]
       [sa/MessageContent
        [sa/MessageHeader "You win! Close this message to play again"]]])))

;--------------------- Landmark List / Detailed Cards

(defn make-detail-card [info]
  (let [name (get info :name)
        image (get info :img)
        description (get info :description)]
    [:div {:id "detail-card"}
     [sa/Card {:raised true}
      [sa/Image {:src image}]
      [sa/CardContent
       [sa/CardHeader name]
       [sa/CardDescription description]]]]
    ))

(defn make-list-item [id info]
  (let [name (get info :name)
        image (get info :img)]
    [sa/ListItem {:on-click #(re-frame/dispatch [:choose-card id])}
     [sa/Image {:avatar true :src image}]
     [sa/ListContent
      [sa/ListHeader name]]]))

(defn landmark-list []
  (let [cards (re-frame/subscribe [:cards])]
    [:div {:class-name "scroll-list"}
     [sa/ListSA {:selection true :verticalAlign "middle"}
      (for [[k v] @cards] ^{:key (str "list-" k)} [make-list-item k v])]])) ;--- maybe use map instead

;----------------------- Game Cards / Grid

(defn make-card [grid-id]
  (let [card-info (re-frame/subscribe [:card grid-id])
        card-back (re-frame/subscribe [:card-back])]
    (fn [grid-id]
      [sa/Card {:raised true :on-click (fn []
                                         (re-frame/dispatch [:select-card grid-id]))}
       [sa/Reveal {
                   :animated "move"
                   :disabled (not (:revealed? @card-info))
                   :active   (:revealed? @card-info)}
        [sa/RevealContent {:visible true}
         [sa/Image {:src @card-back}]]
        [sa/RevealContent {:hidden true}
         [sa/Image {:src (:image @card-info)}]]]])))

(defn make-grid [cards]
  (let [num-cards @(re-frame/subscribe [:number-of-cards])]
    [sa/CardGroup {:id "card-board" :itemsPerRow 6}
     (for [grid-id (range num-cards)]
       ^{:key (str "grid-" grid-id)} [make-card grid-id])]))

;------------------------ Page Layout

(defn grid-layout []
  (let [cards (re-frame/subscribe [:cards])
        active-card (re-frame/subscribe [:active-card])
        num-matches (re-frame/subscribe [:matches])
        name (re-frame/subscribe [:name])]
    [:div {:id "main-view"}
     [sa/Grid {:divided "vertically"}
      [sa/GridRow {:columns 3}
       [sa/GridColumn {:width 3}]
       [sa/GridColumn {:width 10 :text-align "right"}
        [header @name]]
       [sa/GridColumn {:width 3 :text-align "center" :verticalAlign "middle"}
        [menu-bar]]]
      [sa/GridRow {:columns 3}
       [sa/GridColumn {:width 3}
        [matches @num-matches]
        [sa/Divider]
        [landmark-list]]
       [sa/GridColumn {:width 10 :verticalAlign "middle"}
        [sa/Container
         [win-message]
         [sa/Segment
          [make-grid @cards]]]]
       [sa/GridColumn {:width 3 :verticalAlign "middle"}
        [make-detail-card (get @cards @active-card)]]]
      [sa/GridRow {:columns 1}
       [sa/GridColumn {:text-align "center"}
        [footer]]]]]))

(defn main-panel []
  (fn []
    [sa/Container
     [grid-layout]
     ]))

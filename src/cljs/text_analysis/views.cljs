(ns text-analysis.views
    (:require [re-frame.core :as re-frame]))


(defn navigation []
  [:div
   [:a {:href "#/"} "Text input"]
   [:span " | "]
   [:a {:href "#/results"} "Results view"]])

;; home

(defn text-changed [event]
  (let [value (-> event .-target .-value)]
    (re-frame/dispatch [:home/text-changed value])))

(defn home-panel []
  (let [name (re-frame/subscribe [:name])
        text (re-frame/subscribe [:text])]
    (fn []
      [:div
       [:h1 "Text input"]
       [:textarea {:on-change text-changed
                   :rows 40
                   :cols 100
                   :default-value @text}]])))


;; results

(defn result-view [r]
  ^{:key r}[:p (first r) " - " (second r)])

(defn results-panel []
  (let [results (re-frame/subscribe [:results])]
    [:div
     [:h1 "Results view"]
     (map result-view @results)]))



;; about

(defn about-panel []
  (fn []
    [:div "This is the About Page."
     [:div [:a {:href "#/"} "go to Home Page"]]]))


;; main

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :results-panel [] [results-panel])
(defmethod panels :default [] [:div])

(defn show-panel
  [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:div
       [navigation]
       [show-panel @active-panel]])))

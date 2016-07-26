(ns text-analysis.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
 :name
 (fn [db]
   (reaction (:name @db))))

(re-frame/register-sub
 :text
 (fn [db]
   (reaction (:text @db))))

(re-frame/register-sub
 :results
 (fn [db]
   (reaction (:results @db))))

; (re-frame/register-sub
;  :results
;  (fn [db]
;    (let [text (re-frame/subscribe [:text])]
;      (reaction (count @text)))))


(re-frame/register-sub
 :active-panel
 (fn [db _]
   (reaction (:active-panel @db))))

(ns text-analysis.handlers
    (:require [re-frame.core :as re-frame]
              [text-analysis.db :as db]
              [clojure.string :refer [split lower-case replace]]))

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/register-handler
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(defn clean-text [text]
 (-> text
     (replace #"\s.\." " ") ; initials like a. e.
     (replace #"\s.\)" " ") ; lists like a)
     (replace "\"" " ")
     (replace "[" " ")
     (replace "]" " ")
     (replace "/" " ")
     (replace "!" " ")
     (replace "?" " ")
     (replace ";" " ")
     (replace #"[,\.\(\)–\d«»%…\-]" " ")))

(defn process-text [text]
  (-> text
      (lower-case)
      (clean-text)
      (split #"\s+")
      (frequencies)
      (->> (into [])
           (sort-by second >))))

(re-frame/register-handler
 :home/text-changed
 (fn [db [_ value]]
   (assoc db :text value :results (process-text value))))

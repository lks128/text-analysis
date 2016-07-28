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
     (replace ":" " ")
     ;; TODO: numbers only if spaces around, not P2P
     (replace #"[,\.\(\)–\d«»%…\-]" " ")))


;; {"слон" {:forms ["слона", "слону"]
;;          :count 5}}

(defn initial-word-form [word]
  (case word
        ("слон" "слона" "слону") "слон"
        ("слово" "слова" "словами" "словом") "слово"
        ("человек" "человека" "человеку") "человек"
        ("сознание" "сознании" "сознания" "сознанием") "сознание"
        ("благо" "блага" "благ" "благами" "благам") "благо"
        word))


(defn process-word [results word-form]
  (let [word (initial-word-form word-form)
        forms (:forms (results word) [])
        count (:count (results word) 0)
        updated-forms (distinct (conj forms word-form))]
    (assoc results word {:forms updated-forms
                         :count (inc count)})))

(defn count-words [words]
  (reduce process-word {} words))


(defn process-text [text]
  (-> text
      (lower-case)
      (clean-text)
      (split #"\s+")
      (count-words)
      (->> (into [])
           (sort-by #(-> % second :count) >))))

(re-frame/register-handler
 :home/text-changed
 (fn [db [_ value]]
   (assoc db :text value :results (process-text value))))

(ns text-analysis.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [devtools.core :as devtools]
              [text-analysis.handlers]
              [text-analysis.subs]
              [text-analysis.routes :as routes]
              [text-analysis.views :as views]
              [text-analysis.config :as config]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")
    (devtools/install!)))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (re-frame/dispatch [:home/text-changed "input text here"])
  (dev-setup)
  (mount-root))

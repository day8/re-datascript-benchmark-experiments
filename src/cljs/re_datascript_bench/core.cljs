(ns re-datascript-bench.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [re-datascript-bench.events :as events]
   [re-datascript-bench.routes :as routes]
   [re-datascript-bench.views :as views]
   [re-datascript-bench.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (routes/start!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))

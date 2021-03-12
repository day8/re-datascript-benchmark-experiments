(ns re-datascript-bench.views
  (:require

   [re-frame.core :as rf]
   [re-com.core :as rc :refer [at]]
   [re-datascript-bench.events :as events]
   [re-datascript-bench.routes :as routes]
   [re-datascript-bench.subs :as subs]))

;; home

(defn home-panel []
  (let [inserts-columns (rf/subscribe [::subs/inserts-columns])
        inserts-model (rf/subscribe [::subs/inserts-model])
        query-columns (rf/subscribe [::subs/query-columns])
        query-model  (rf/subscribe [::subs/query-model])]
    [rc/v-box
     :src      (at)
     :gap      "1em"
     :style    {:padding "31px"}
     :children [[rc/title
                 :label "DataScript Insert Times"
                 :level :level2]
                [rc/simple-v-table
                 :columns @inserts-columns
                 :model inserts-model]
                [rc/title
                 :label "DataScript Query Times"
                 :level :level2]
                [rc/simple-v-table
                 :columns @query-columns
                 :model query-model]]]))



(defmethod routes/panels :home-panel [] [home-panel])

;; main

(defn main-panel []
  (let [active-panel (rf/subscribe [::subs/active-panel])]
    [rc/v-box
     :src      (at)
     :height   "100%"
     :children [(routes/panels @active-panel)]]))

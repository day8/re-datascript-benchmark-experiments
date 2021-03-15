(ns re-datascript-bench.subs
  (:require
    [goog.string :as gstring]
    [re-frame.core :as rf]))

(rf/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(rf/reg-sub
  ::inserts-columns
  (fn [db _]
    [{:id :n :header-label "N" :row-label-fn :n :width 70}
     {:id :ms-1 :header-label "1 tuple (ms)" :row-label-fn #(gstring/format "%0.2f" (or (:ms-1 %) 0)) :width 100}
     {:id :ms-2 :header-label "2 tuple (ms)" :row-label-fn #(gstring/format "%0.2f" (or (:ms-2 %) 0)) :width 100}
     {:id :ms-3 :header-label "3 tuple (ms)" :row-label-fn #(gstring/format "%0.2f" (or (:ms-3 %) 0)) :width 100}]))

(rf/reg-sub
  ::inserts-model
  (fn [db _]
    (reduce-kv
      (fn [rows n {:keys [ms-1 ms-2 ms-3]}]
        (conj rows
              {:n  n
               :ms-1 ms-1
               :ms-2 ms-2
               :ms-3 ms-3}))
      []
      (get-in db [:benchmark :insert]))))

(rf/reg-sub
  ::write-transit-columns
  (fn [db _]
    [{:id :n :header-label "N" :row-label-fn :n :width 70}
     {:id :ms-1 :header-label "1 tuple (ms)" :row-label-fn #(gstring/format "%0.2f" (or (:ms-1 %) 0)) :width 100}
     {:id :ms-2 :header-label "2 tuple (ms)" :row-label-fn #(gstring/format "%0.2f" (or (:ms-2 %) 0)) :width 100}
     {:id :ms-3 :header-label "3 tuple (ms)" :row-label-fn #(gstring/format "%0.2f" (or (:ms-3 %) 0)) :width 100}]))

(rf/reg-sub
  ::write-transit-model
  (fn [db _]
    (reduce-kv
      (fn [rows n {:keys [ms-1 ms-2 ms-3]}]
        (conj rows
              {:n  n
               :ms-1 ms-1
               :ms-2 ms-2
               :ms-3 ms-3}))
      []
      (get-in db [:benchmark :write-transit]))))

(rf/reg-sub
  ::read-transit-columns
  (fn [db _]
    [{:id :n :header-label "N" :row-label-fn :n :width 70}
     {:id :ms-1 :header-label "1 tuple (ms)" :row-label-fn #(gstring/format "%0.2f" (or (:ms-1 %) 0)) :width 100}
     {:id :ms-2 :header-label "2 tuple (ms)" :row-label-fn #(gstring/format "%0.2f" (or (:ms-2 %) 0)) :width 100}
     {:id :ms-3 :header-label "3 tuple (ms)" :row-label-fn #(gstring/format "%0.2f" (or (:ms-3 %) 0)) :width 100}]))

(rf/reg-sub
  ::read-transit-model
  (fn [db _]
    (reduce-kv
      (fn [rows n {:keys [ms-1 ms-2 ms-3]}]
        (conj rows
              {:n  n
               :ms-1 ms-1
               :ms-2 ms-2
               :ms-3 ms-3}))
      []
      (get-in db [:benchmark :read-transit]))))


(rf/reg-sub
  ::query-columns
  (fn [db _]
    [{:id :n :header-label "N" :row-label-fn :n :width 70}
     {:id :eid-1 :header-label "1 tuple (ms) - '[:find ?e :where [?e :person/id _]]" :row-label-fn #(gstring/format "%0.2f" (or (:eid-1 %) 0)) :width 400}
     {:id :eid-2 :header-label "2 tuple (ms) - '[:find ?e :where [?e :person/id _]]" :row-label-fn #(gstring/format "%0.2f" (or (:eid-2 %) 0)) :width 400}
     {:id :eid-3 :header-label "3 tuple (ms) - '[:find ?e :where [?e :person/id _]]" :row-label-fn #(gstring/format "%0.2f" (or (:eid-3 %) 0)) :width 400}
     {:id :same-age :header-label "3 tuple (ms) - '[:find [?other ...] :in $ ?eid :where [?eid :person/id _] [?eid :person/age ?age] [?other :person/age ?age]]" :row-label-fn #(gstring/format "%0.2f" (or (:same-age %) 0)) :width 800}
     {:id :pull-query :header-label "2 tuple (ms) - '[:find (pull ?e [*]) :where [?e :person/name \"Joe\"]]" :row-label-fn #(gstring/format "%0.2f" (or (:pull-query %) 0)) :width 400}]))

(rf/reg-sub
  ::query-model
  (fn [db _]
    (reduce-kv
      (fn [rows n {:keys [eid-1 eid-2 eid-3 same-age pull-query]}]
        (conj rows
              {:n  n
               :eid-1 eid-1
               :eid-2 eid-2
               :eid-3 eid-3
               :same-age same-age
               :pull-query pull-query}))
      []
      (get-in db [:benchmark :query]))))

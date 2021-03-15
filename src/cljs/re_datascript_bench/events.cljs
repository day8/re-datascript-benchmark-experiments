(ns re-datascript-bench.events
  (:require
   [datascript.core :as d]
   [datascript.transit :as dt]
   [re-frame.core :as rf :refer [unwrap inject-cofx]]
   [re-posh.core :refer [connect!]]
   [re-datascript-bench.db :as db]))

(rf/reg-fx
  :connect!
  (fn []
    (js/console.log "connect!")
    (reset! db/ds-db (d/create-conn db/ds-schema))
    (connect! @db/ds-db)))

(rf/reg-event-fx
  ::benchmark-1-tuple
  [unwrap (inject-cofx :ds)]
  (fn [{:keys [db ds]} {:keys [n]}]
    (let [tx-data      (mapv (fn [i]
                               {:person/id i})
                             (range n))
          insert-start (js/performance.now)]
      (d/transact! @db/ds-db tx-data)
      (let [insert-end          (js/performance.now)
            insert-delta        (- insert-end insert-start)
            eid-query-start     (js/performance.now)
            _                   (d/q
                                  '[:find ?e
                                    :where
                                    [?e :person/id _]]
                                  @@db/ds-db)
            eid-query-end       (js/performance.now)
            eid-query-delta     (- eid-query-end eid-query-start)
            write-transit-start (js/performance.now)
            transit             (dt/write-transit-str @@db/ds-db)
            write-transit-end   (js/performance.now)
            write-transit-delta (- write-transit-end write-transit-start)
            read-transit-start  (js/performance.now)
            _                   (dt/read-transit-str transit)
            read-transit-end    (js/performance.now)
            read-transit-delta  (- read-transit-end read-transit-start)]
        (merge
          {:db (-> db
                   (assoc-in [:benchmark :insert n :ms-1] insert-delta)
                   (assoc-in [:benchmark :query n :eid-1] eid-query-delta)
                   (assoc-in [:benchmark :write-transit n :ms-1] write-transit-delta)
                   (assoc-in [:benchmark :read-transit n :ms-1] read-transit-delta))}
          (if (< n 100000)
            {:connect!       {}
             :dispatch-later {:ms       64
                              :dispatch [::benchmark-1-tuple {:n (* n 10)}]}}
            {:connect!       {}
             :dispatch-later {:ms       64
                              :dispatch [::benchmark-2-tuple {:n 100}]}}))))))

(rf/reg-event-fx
  ::benchmark-2-tuple
  [unwrap (inject-cofx :ds)]
  (fn [{:keys [db ds]} {:keys [n]}]
    (let [tx-data (mapv (fn [i]
                          {:person/id i
                           :person/name (rand-nth ["Joe" "Bob" "Kelly" "Ned" "Justin"])})
                        (range n))
          start   (js/performance.now)]
      (d/transact! @db/ds-db tx-data)
      (let [end                 (js/performance.now)
            delta               (- end start)
            eid-query-start     (js/performance.now)
            _                   (d/q
                                  '[:find ?e
                                    :where
                                    [?e :person/id _]]
                                  @@db/ds-db)
            eid-query-end       (js/performance.now)
            eid-query-delta     (- eid-query-end eid-query-start)
            pull-query-start    (js/performance.now)
            res                 (d/q
                                  '[:find (pull ?e [*])
                                    :where [?e :person/name "Joe"]]
                                  @@db/ds-db)
            pull-query-end      (js/performance.now)
            pull-query-delta    (- pull-query-end pull-query-start)
            write-transit-start (js/performance.now)
            transit             (dt/write-transit-str @@db/ds-db)
            write-transit-end   (js/performance.now)
            write-transit-delta (- write-transit-end write-transit-start)
            read-transit-start  (js/performance.now)
            _                   (dt/read-transit-str transit)
            read-transit-end    (js/performance.now)
            read-transit-delta  (- read-transit-end read-transit-start)
            ]
        (js/console.info "'[:find (pull ?e [*])\n :where [?e :person/name \"Joe\"]] found" (count res))
        (merge
          {:db (-> db
                   (assoc-in [:benchmark :insert n :ms-2] delta)
                   (assoc-in [:benchmark :query n :eid-2] eid-query-delta)
                   (assoc-in [:benchmark :query n :pull-query] pull-query-delta)
                   (assoc-in [:benchmark :write-transit n :ms-2] write-transit-delta)
                   (assoc-in [:benchmark :read-transit n :ms-2] read-transit-delta))}
          (if (< n 100000)
            {:connect!       {}
             :dispatch-later {:ms       64
                              :dispatch [::benchmark-2-tuple {:n (* n 10)}]}}
            {:connect!       {}
             :dispatch-later {:ms       64
                              :dispatch [::benchmark-3-tuple {:n 100}]}}))))))

(rf/reg-event-fx
  ::benchmark-3-tuple
  [unwrap (inject-cofx :ds)]
  (fn [{:keys [db ds]} {:keys [n]}]
    (let [tx-data (mapv (fn [i]
                          {:person/id i
                           :person/name (rand-nth ["Joe" "Bob" "Kelly" "Ned" "Justin"])
                           :person/age  (rand-int 100)})
                        (range n))
          start   (js/performance.now)]
      (d/transact! @db/ds-db tx-data)
      (let [end                  (js/performance.now)
            delta                (- end start)
            eid-query-start      (js/performance.now)
            _                    (d/q
                                   '[:find ?e
                                     :where
                                     [?e :person/id _]]
                                   @@db/ds-db)
            eid-query-end        (js/performance.now)
            eid-query-delta      (- eid-query-end eid-query-start)
            same-age-query-start (js/performance.now)
            res                  (d/q
                                   '[:find [?other ...]
                                     :in $ ?eid
                                     :where
                                     [?eid :person/id _]
                                     [?eid :person/age ?age]
                                     [?other :person/age ?age]]
                                   @@db/ds-db 1)
            same-age-query-end   (js/performance.now)
            same-age-query-delta (- same-age-query-end same-age-query-start)
            write-transit-start  (js/performance.now)
            transit              (dt/write-transit-str @@db/ds-db)
            write-transit-end    (js/performance.now)
            write-transit-delta  (- write-transit-end write-transit-start)
            read-transit-start   (js/performance.now)
            _                    (dt/read-transit-str transit)
            read-transit-end     (js/performance.now)
            read-transit-delta   (- read-transit-end read-transit-start)]
        (merge
          {:db (-> db
                   (assoc-in [:benchmark :insert n :ms-3] delta)
                   (assoc-in [:benchmark :query n :eid-3] eid-query-delta)
                   (assoc-in [:benchmark :query n :same-age] same-age-query-delta)
                   (assoc-in [:benchmark :write-transit n :ms-3] write-transit-delta)
                   (assoc-in [:benchmark :read-transit n :ms-3] read-transit-delta))}
          (if (< n 100000)
            {:connect!       {}
             :dispatch-later {:ms       64
                              :dispatch [::benchmark-3-tuple {:n (* n 10)}]}}
            {:connect!       {}
             #_#_:dispatch-later {:ms       64
                                  :dispatch [::benchmark-4-tuple {:n 100}]}}))))))

(rf/reg-event-fx
  ::initialize-db
  (fn [_ _]
    {:connect!       {}
     :db             db/default-db
     :dispatch-later {:ms       64
                      :dispatch [::benchmark-1-tuple {:n 100}]}}))

(rf/reg-event-fx
  ::navigate
  (fn [_ [_ handler]]
   {:navigate handler}))

(rf/reg-event-fx
 ::set-active-panel
 (fn [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel)}))

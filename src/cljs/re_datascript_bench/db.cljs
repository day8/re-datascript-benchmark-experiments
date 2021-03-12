(ns re-datascript-bench.db
  (:require [datascript.core :as d]))

(def default-db
  {})

(def ds-schema
  {:schema/version {}
   :person/id      {:db/unique :db.unique/identity}
   :person/name    {:db/doc "The person's name."}})

(def ds-db
  (atom nil))
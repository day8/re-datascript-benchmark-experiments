(defproject re-datascript-bench "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure       "1.10.3"]
                 [org.clojure/clojurescript "1.10.773"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [thheller/shadow-cljs      "2.11.23"]
                 [datascript                "1.0.7"]
                 [datascript-transit        "0.3.0"]
                 [reagent                   "1.0.0"]
                 [denistakeda/posh          "0.5.8"]
                 [re-frame                  "1.2.0"]
                 [re-posh                   "0.3.2"
                  :exclusions [re-frame/re-frame
                               denistakeda/posh]]
                 [re-com                    "2.13.2"]
                 [bidi                      "2.1.6"]
                 [kibu/pushy                "0.3.8"]]

  :plugins [[lein-shadow  "0.3.1"]
            [lein-shell   "0.5.0"]
            [lein-ancient "0.6.15"]]

  :min-lein-version "2.9.0"

  :source-paths ["src/clj" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]


  :shadow-cljs {:nrepl {:port 8777}

                :builds {:app {:target :browser
                               :output-dir "resources/public/js/compiled"
                               
                               :asset-path "/js/compiled"
                               
                               :compiler-options {:externs ["externs/detect-element-resize-externs.js"]}
                               
                               :modules {:app {:init-fn re-datascript-bench.core/init
                                               :preloads [devtools.preload]}}

                               :devtools {:http-root "resources/public"
                                          :http-port 8280}}}}


  :shell {:commands {"karma" {:windows         ["cmd" "/c" "karma"]
                              :default-command "karma"}
                     "open"  {:windows         ["cmd" "/c" "start"]
                              :macosx          "open"
                              :linux           "xdg-open"}}}

  :aliases {"dev"          ["do"
                            ["shell" "echo" "\"DEPRECATED: Please use lein watch instead.\""]
                            ["watch"]]
            "watch"        ["with-profile" "dev" "do"
                            ["shadow" "watch" "app" "browser-test" "karma-test"]]

            "prod"         ["do"
                            ["shell" "echo" "\"DEPRECATED: Please use lein release instead.\""]
                            ["release"]]

            "release"      ["with-profile" "prod" "do"
                            ["shadow" "release" "app"]]

            "build-report" ["with-profile" "prod" "do"
                            ["shadow" "run" "shadow.cljs.build-report" "app" "target/build-report.html"]
                            ["shell" "open" "target/build-report.html"]]

            "karma"        ["do"
                            ["shell" "echo" "\"DEPRECATED: Please use lein ci instead.\""]
                            ["ci"]]
            "ci"           ["with-profile" "prod" "do"
                            ["shadow" "compile" "karma-test"]
                            ["shell" "karma" "start" "--single-run" "--reporters" "junit,dots"]]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "1.0.2"]]
    :source-paths ["dev"]}

   :prod {}}



  :prep-tasks [])

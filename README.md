# re-datascript-bench

A [re-frame](https://github.com/day8/re-frame) application that does basic measurements of
[DataScript](https://github.com/tonsky/datascript) performance.

Re-runs all measurements with both:
- number of entities `100`, `1000`, `10,000` and `100,000`
- entities with `1`, `2` and `3` attributes

Includes the following measurements:
- insert times (`d/transact!`)
- datalog query times (`d/q`)
- pull query times (`q/pull`)
- transit serialisation of entire db ([datascript-transit](https://github.com/tonsky/datascript-transit))
- transit deserialisation of entire db (([datascript-transit](https://github.com/tonsky/datascript-transit)))

## Observations

In Google Chrome on an AMD Ryzen 5000-series desktop:

- insert times range from 16ms for 100 single attribute entities to 7.2s for 100K triple attribute entities
- query times on the same range from 0.3ms to 145ms
- transit serialisation ranges from 0.7ms to 560ms
- transit deserialisation ranges from 0.8ms to 770ms

So DataScript itself is not terrible, but the 
[posh query analyzer and cache layer on top of DataScript is known to have pathological performance cases](https://github.com/athensresearch/athens/pull/665#issuecomment-790088361).

## Useful DataScript Resources

- https://tonsky.me/blog/datascript-internals/
- https://tonsky.me/blog/datascript-resources/
- https://tonsky.me/blog/datascript-chat/
- https://tonsky.me/blog/acha-acha/
- https://github.com/tonsky/datascript
- https://github.com/denistakeda/re-posh (Do not use!)
- https://github.com/denistakeda/posh (Do not use!)
- https://github.com/mpdairy/posh-todo
- https://github.com/kristianmandrup/datascript-tutorial
- http://web.archive.org/web/20190404202047/http://udayv.com/clojurescript/clojure/2016/04/28/datascript101-chapter1-initializing-inserting-and-querying-records/
- http://web.archive.org/web/20190405010102/https://udayv.com/clojurescript/clojure/2016/05/06/datascript101-chapter2-uniqueness-and-indexing/
- http://web.archive.org/web/20190404223433/http://udayv.com/clojurescript/clojure/2016/06/15/datascript101-chapter3-fetching-data/
- https://www.slideshare.net/fractallambda/datascript-and-reagent
- https://docs.datomic.com/on-prem/query/query.html
- https://github.com/thegeez/clj-crud

## Alternatives

- https://github.com/threatgrid/asami/wiki
- https://github.com/lilactown/autonormal
- https://github.com/noprompt/meander/blob/epsilon/examples/datascript.cljc
- https://github.com/simongray/clojure-graph-resources

## Getting Started

### Environment Setup

1. Install [JDK 8 or later](https://openjdk.java.net/install/) (Java Development Kit)
2. Install [Leiningen](https://leiningen.org/#install) (Clojure/ClojureScript project task &
dependency management)
3. Install [Node.js](https://nodejs.org/) (JavaScript runtime environment) which should include
   [NPM](https://docs.npmjs.com/cli/npm) or if your Node.js installation does not include NPM also install it.
7. Clone this repo and open a terminal in the `re-datascript-bench` project root directory
8. (Optional) Download project dependencies:
    ```sh
    lein deps
    ```
   
## Development

### Running the App

Start a temporary local web server, build the app with the `dev` profile, and serve the app,
browser test runner and karma test runner with hot reload:

```sh
lein watch
```

Please be patient; it may take over 20 seconds to see any output, and over 40 seconds to complete.

When `[:app] Build completed` appears in the output, browse to
[http://localhost:8280/](http://localhost:8280/).

[`shadow-cljs`](https://github.com/thheller/shadow-cljs) will automatically push ClojureScript code
changes to your browser on save. To prevent a few common issues, see
[Hot Reload in ClojureScript: Things to avoid](https://code.thheller.com/blog/shadow-cljs/2019/08/25/hot-reload-in-clojurescript.html#things-to-avoid).

Opening the app in your browser starts a
[ClojureScript browser REPL](https://clojurescript.org/reference/repl#using-the-browser-as-an-evaluation-environment),
to which you may now connect.

#### Connecting to the browser REPL from your editor

See
[Shadow CLJS User's Guide: Editor Integration](https://shadow-cljs.github.io/docs/UsersGuide.html#_editor_integration).
Note that `lein watch` runs `shadow-cljs watch` for you, and that this project's running build ids is
`app`, `browser-test`, `karma-test`, or the keywords `:app`, `:browser-test`, `:karma-test` in a Clojure context.

Alternatively, search the web for info on connecting to a `shadow-cljs` ClojureScript browser REPL
from your editor and configuration.

For example, in Vim / Neovim with `fireplace.vim`
1. Open a `.cljs` file in the project to activate `fireplace.vim`
2. In normal mode, execute the `Piggieback` command with this project's running build id, `:app`:
    ```vim
    :Piggieback :app
    ```

#### Connecting to the browser REPL from a terminal

1. Connect to the `shadow-cljs` nREPL:
    ```sh
    lein repl :connect localhost:8777
    ```
    The REPL prompt, `shadow.user=>`, indicates that is a Clojure REPL, not ClojureScript.

2. In the REPL, switch the session to this project's running build id, `:app`:
    ```clj
    (shadow.cljs.devtools.api/nrepl-select :app)
    ```
    The REPL prompt changes to `cljs.user=>`, indicating that this is now a ClojureScript REPL.
3. See [`user.cljs`](dev/cljs/user.cljs) for symbols that are immediately accessible in the REPL
without needing to `require`.

### Running `shadow-cljs` Actions

See a list of [`shadow-cljs CLI`](https://shadow-cljs.github.io/docs/UsersGuide.html#_command_line)
actions:
```sh
lein run -m shadow.cljs.devtools.cli --help
```

Please be patient; it may take over 10 seconds to see any output. Also note that some actions shown
may not actually be supported, outputting "Unknown action." when run.

Run a shadow-cljs action on this project's build id (without the colon, just `app`):
```sh
lein run -m shadow.cljs.devtools.cli <action> app
```

## License

The MIT License (MIT)

Copyright &copy; 2021 Isaac Johnston

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
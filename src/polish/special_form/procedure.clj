(ns polish.special-form.procedure
  (:require
    [polish.ctx :as ctx]
    [polish.special-form :as sf]
    [polish.token :as t]))


; Procedures
(defprotocol Procedure
  (eval-procedure [_ ctx]))

(defn procedure? [p]
  (satisfies? Procedure p))

(defn new-procedure [bindings body]
  (reify
    Procedure
    (eval-procedure [_ ctx]
      #p body
      (ctx/eval-with-scope ctx bindings body))))

(def defproc-special-form
  (let [defproc-usage-message "defproc is expected to be of the form \"defproc <proc-name> <bindings> <body>\"
  where <proc-name> is a symbol, <bindings> is a vector of symbols, and <body> is a list of tokens"]
    (reify
      sf/SpecialForm
      (get-symbol [_] 'defproc)
      (eval-form [_ ctx]
        (let [[ctx proc-name] (ctx/unshift ctx)
              _ (assert (symbol? proc-name) defproc-usage-message)
              [ctx bindings] (ctx/unshift ctx)
              _ (assert (and (vector? bindings)
                             (every? symbol? bindings)) defproc-usage-message)
              [ctx body] (ctx/unshift ctx)
              _ (assert (list? body) defproc-usage-message)]
          (ctx/bind-sym ctx proc-name (new-procedure bindings body)))))))

(def procedure-token-type
  (reify
    t/TokenType
    (satisfied? [_ token] (procedure? token))
    (handle [_ token ctx]
      (eval-procedure token ctx))))

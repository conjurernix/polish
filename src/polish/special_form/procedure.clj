(ns polish.special-form.procedure
  (:require
    [polish.ctx.core :as ctx]
    [polish.special-form.core :as sf]
    [polish.token.core :as t]))


; Procedures
(defprotocol Procedure
  (eval-procedure [_ ctx]))

(defn procedure? [p]
  (satisfies? Procedure p))

(defn new-procedure [body]
  (reify
    Procedure
    (eval-procedure [_ ctx] (reduce ctx/shift ctx body))))

(def defproc-special-form
  (let [defproc-usage-message "defproc is expected to be of the form \"defproc <proc-name> <body>\"
  where <proc-name> is a symbol and <body> is a list of tokens"]
    (reify
      sf/SpecialForm
      (get-symbol [_] 'defproc)
      (eval-form [_ ctx]
        (let [[ctx proc-name] (ctx/unshift ctx)
              _ (assert (symbol? proc-name) defproc-usage-message)
              [ctx body] (ctx/unshift ctx)
              _ (assert (list? body) defproc-usage-message)]
          (update ctx :env assoc proc-name (new-procedure body)))))))

(def procedure-token-type
  (reify
    t/TokenType
    (satisfied? [_ token] (procedure? token))
    (handle [_ token ctx]
      (eval-procedure token ctx))))

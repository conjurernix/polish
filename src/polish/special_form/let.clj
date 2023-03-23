(ns polish.special-form.let
  (:require
    [polish.ctx :as ctx]
    [polish.special-form :as sf]))

(def let-special-form
  (let [let-usage-message "let is expected to be of the form \"let <bindings> <body>\"
  where <bindings> is a vector of symbols and <body> is a list of tokens"]
    (reify
      sf/SpecialForm
      (get-symbol [_] 'let)
      (eval-form [_ ctx]
        (let [[ctx bindings] (ctx/unshift ctx)
              _ (assert (and (vector? bindings)
                             (every? symbol? bindings)) let-usage-message)
              [ctx body] (ctx/unshift ctx)
              _ (assert (list? body) let-usage-message)]
          (ctx/eval-with-scope ctx bindings body))))))
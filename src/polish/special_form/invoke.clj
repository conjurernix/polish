(ns polish.special-form.invoke
  (:require [polish.ctx :as ctx]
            [polish.special-form :as sf]))

(def invoke-special-form
  (reify
    sf/SpecialForm
    (get-symbol [_] 'invoke)
    (eval-form [_ ctx]
      (let [[ctx arity] (ctx/pop ctx)
            [ctx f] (ctx/pop ctx)
            [ctx values] (ctx/pop-values ctx arity)]
        (ctx/push ctx (apply f values))))))

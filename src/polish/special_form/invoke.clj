(ns polish.special-form.invoke
  (:require [polish.ctx.core :as ctx]
            [polish.special-form.core :as sf]))

(def invoke-special-form
  (reify
    sf/SpecialForm
    (get-symbol [_] 'invoke)
    (eval-form [_ ctx]
      (let [[ctx arity] (pop ctx)
            [ctx f] (pop ctx)
            [ctx values] (ctx/pop-values ctx arity)]
        (ctx/push ctx (apply f values))))))

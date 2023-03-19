(ns polish.special-form.dup
  (:require
    [polish.ctx.core :as ctx]
    [polish.special-form.core :as sf]))

(def dup-special-form
  (reify
    sf/SpecialForm
    (get-symbol [_] 'dup)
    (eval-form [_ ctx]
      (ctx/push ctx (ctx/peek ctx)))))

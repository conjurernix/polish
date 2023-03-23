(ns polish.special-form.swap
  (:require
    [polish.ctx :as ctx]
    [polish.special-form :as sf]))


(def swap-special-form
  (reify
    sf/SpecialForm
    (get-symbol [_] 'swap)
    (eval-form [_ ctx]
      (let [[ctx a] (ctx/pop ctx)
            [ctx b] (ctx/pop ctx)]
        (-> ctx
            (ctx/push a)
            (ctx/push b))))))

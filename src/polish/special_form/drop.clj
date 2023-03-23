(ns polish.special-form.drop
  (:require
    [polish.ctx :as ctx]
    [polish.special-form :as sf]))

(def drop-special-form
  (reify
    sf/SpecialForm
    (get-symbol [_] 'drop)
    (eval-form [_ ctx]
      (-> ctx
          (ctx/pop)
          (first)))))

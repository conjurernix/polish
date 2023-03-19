(ns polish.special-form.pop
  (:require
    [polish.ctx.core :as ctx]
    [polish.special-form.core :as sf]))

(def pop-special-form
  (reify
    sf/SpecialForm
    (get-symbol [_] 'pop)
    (eval-form [_ ctx]
      (-> ctx
          (ctx/pop)
          (first)))))

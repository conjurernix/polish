(ns polish.special-form.pop
  (:require
    [polish.ctx :as ctx]
    [polish.special-form :as sf]))

(def pop-special-form
  (reify
    sf/SpecialForm
    (get-symbol [_] 'pop)
    (eval-form [_ ctx]
      (-> ctx
          (ctx/pop)
          (first)))))

(ns polish.special-form)

(defprotocol SpecialForm
  (get-symbol [_])
  (eval-form [this ctx]))

(defn special-form? [token]
  (satisfies? SpecialForm token))

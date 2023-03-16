(ns polish.special-form.core)

(defprotocol SpecialForm
  (get-symbol [_])
  (eval-form [this ctx]))

(defn special-form? [token]
  (satisfies? SpecialForm token))

(ns polish.token.special-form
  (:require
    [polish.special-form :as sf]
    [polish.token :as t]))

(def special-form-token-type
  (reify
    t/TokenType
    (satisfied? [_ token] (sf/special-form? token))
    (handle [_ token ctx]
      (sf/eval-form token ctx))))

(ns polish.token.symbol
  (:require
    [polish.ctx.core :as ctx]
    [polish.token.core :as t]))

(def symbol-token-type
  (reify
    t/TokenType
    (satisfied? [_ token] (symbol? token))
    (handle [_ token ctx]
      (let [token-meta (meta token)]
        (ctx/shift ctx (with-meta (ctx/lookup ctx token) token-meta))))))

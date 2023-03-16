(ns polish.token.var
  (:require
    [polish.ctx.core :as ctx]
    [polish.token.core :as t]))

(def var-token-type
  (reify
    t/TokenType
    (satisfied? [_ token] (var? token))
    (handle [_ token ctx]
      (let [token-meta (meta token)]
        (ctx/shift ctx (with-meta (var-get token) token-meta))))))



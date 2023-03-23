(ns polish.token.symbol
  (:require
    [polish.ctx :as ctx]
    [polish.token :as t]))

(def symbol-token-type
  (reify
    t/TokenType
    (satisfied? [_ token] (symbol? token))
    (handle [_ token ctx]
      (let [token-meta (meta token)]
        (ctx/shift ctx (cond-> (ctx/lookup ctx token)
                         token-meta (with-meta token-meta)))))))

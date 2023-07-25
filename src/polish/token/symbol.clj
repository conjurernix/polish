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
        (ctx/shift ctx (cond->
                         (or (ctx/lookup ctx token)
                             (var-get (or (resolve token)
                                          (throw (ex-info (str "Could not resolve symbol: " token)
                                                          {:sym token})))))
                         token-meta (with-meta token-meta)))))))

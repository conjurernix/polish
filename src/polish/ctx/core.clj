(ns polish.ctx.core
  (:refer-clojure :exclude [eval peek pop])
  (:require
    [medley.core :as m]
    [polish.special-form.core :as sf]
    [polish.token.core :as t]))

(def ^:dynamic *ctx*)

(defprotocol ICtx
  (with-program [ctx program])
  (with-data [ctx data])
  (with-env [ctx env])
  (with-special-form [this special-form])
  (with-token-type [this token-type])
  (push [ctx value])
  (pop [ctx])
  (peek [ctx])
  (shift [ctx value])
  (unshift [ctx])
  (look [ctx])
  (eval-1 [ctx]))

(defrecord Ctx [token-types env ps ds]
  ICtx
  (with-program [this program]
    (assoc this :ps program))

  (with-data [this data]
    (assoc this :ds data))

  (with-env [this e]
    (assoc this :env e))

  (with-special-form [this special-form]
    (update this :env assoc (sf/get-symbol special-form) special-form))

  (with-token-type [this token-type]
    (update this :token-types conj token-type))

  (pop [this]
    [(with-data this (rest ds)) (first ds)])

  (push [this value]
    (update this :ds conj value))

  (peek [_]
    (first ds))

  (shift [this value]
    (update this :ps conj value))

  (unshift [this]
    [(with-program this (rest ps)) (first ps)])

  (look [_]
    (first ps))

  (eval-1 [this]
    (let [[this token] (unshift this)]
      (when token
        (or (some-> (->> token-types
                         (m/find-first #(t/satisfied? % token)))
                    (t/handle token this))
            (push this token))))))

(defn eval [ctx]
  (if (look ctx)
    (recur (eval-1 ctx))
    ctx))

(defn pop-values [ctx n]
  (if (zero? n)
    [ctx '()]
    (let [[ctx values] (pop-values ctx (dec n))
          [ctx value] (pop ctx)]
      [ctx (conj values value)])))

(defn resolve-symbol [ctx sym]
  (or (get-in ctx [:env sym])
      (var-get (resolve sym))))
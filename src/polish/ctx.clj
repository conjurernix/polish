(ns polish.ctx
  (:refer-clojure :exclude [eval peek pop])
  (:require
    [medley.core :as m]
    [polish.special-form :as sf]
    [polish.token :as t]))

(def ^:dynamic *ctx*)

(defprotocol ICtx
  (with-program [ctx program])
  (with-data [ctx data])
  (with-env [ctx env])
  (bind-sym [this sym value])
  (with-special-form [this special-form])
  (with-token-type [this token-type])
  (new-scope [ctx])
  (remove-scope [ctx])
  (push [ctx value])
  (pop [ctx])
  (peek [ctx])
  (shift [ctx value-or-values])
  (unshift [ctx])
  (look [ctx])
  (lookup [ctx sym])
  (eval-1 [ctx]))

(defrecord Ctx [token-types env ps ds]
  ICtx
  (with-program [this program]
    (assoc this :ps program))

  (with-data [this data]
    (assoc this :ds data))

  (with-env [this e]
    (assoc this :env e))

  (bind-sym [this sym value]
    (let [[head tail] [(first env) (rest env)]]
      (->> value
           (assoc head sym)
           (conj tail)
           (with-env this))))

  (with-special-form [this special-form]
    (bind-sym this (sf/get-symbol special-form)
              special-form))

  (with-token-type [this token-type]
    (update this :token-types conj token-type))

  (new-scope [this]
    (update this :env conj {}))

  (remove-scope [this]
    (update this :env rest))

  (pop [this]
    [(with-data this (rest ds))
     (or (first ds)
         (throw (ex-info "Tried to pop from an empty data stack"
                         {:ctx this})))])

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

  (lookup [_ sym]
    (or (some #(get % sym) env)
        (var-get (resolve sym))))

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

(defn eval-with-scope [ctx bindings body]
  (let [bindings-count (count bindings)
        body-count (count body)
        body (reverse body)
        ctx (new-scope ctx)
        [ctx values] (pop-values ctx bindings-count)
        ctx (->> values
                 (zipmap bindings)
                 (reduce (fn [ctx [sym value]]
                           (bind-sym ctx sym value)) ctx))
        ctx (reduce shift ctx body)
        ctx (reduce (fn [ctx _] (eval-1 ctx)) ctx (range body-count))]
    (remove-scope ctx)))
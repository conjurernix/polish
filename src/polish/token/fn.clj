(ns polish.token.fn
  (:require [polish.ctx.core :as ctx]
            [polish.token.core :as t]))

(defn fn-arity
  "Returns the maximum arity of:
    - anonymous functions like `#()` and `(fn [])`.
    - defined functions like `map` or `+`.
    - macros, by passing a var like `#'->`.

  Returns `:variadic` if the function/macro is variadic."
  [f]
  (let [func (if (var? f) @f f)
        methods (->> func class .getDeclaredMethods
                     (map #(vector (.getName %)
                                   (count (.getParameterTypes %)))))
        var-args? (some #(-> % first #{"getRequiredArity"})
                        methods)]
    (if var-args?
      :variadic
      (let [max-arity (->> methods
                           (filter (comp #{"invoke"} first))
                           (sort-by second)
                           last
                           second)]
        (if (and (var? f) (-> f meta :macro))
          (- max-arity 2)                                   ;; substract implicit &form and &env arguments
          max-arity)))))

(def fn-token-type
  (reify
    t/TokenType
    (satisfied? [_ token] (fn? token))
    (handle [_ token ctx]
      (let [{:keys [arity] :as token-meta} (meta token)
            arity (or arity (fn-arity token))
            {push? :push} token-meta]
        (cond
          (and (= arity :variadic)
               (not push?))
          (throw (ex-info "Have to explicitly provide arity for
            functions with variadic arguments."
                          {:fn  token
                           :ctx ctx}))

          push? (ctx/push ctx token)
          :else (let [[ctx values] (ctx/pop-values ctx arity)]
                  (ctx/push ctx (apply token values))))))))



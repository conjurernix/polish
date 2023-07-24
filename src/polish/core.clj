(ns polish.core
  (:refer-clojure :exclude [eval])
  (:require
    [polish.ctx :as ctx]
    [polish.prologue :refer [prologue]]
    [polish.special-form.invoke :refer [invoke-special-form]]
    [polish.special-form.let :refer [let-special-form]]
    [polish.special-form.procedure :refer [defproc-special-form procedure-token-type]]
    [polish.token.fn :refer [fn-token-type]]
    [polish.token.special-form :refer [special-form-token-type]]
    [polish.token.symbol :refer [symbol-token-type]]
    [polish.token.var :refer [var-token-type]]
    [polish.utils :as u]))

(defn with-default-env [ctx]
  (-> ctx
      (ctx/with-special-form defproc-special-form)
      (ctx/with-special-form let-special-form)
      (ctx/with-special-form invoke-special-form)))

(defn with-default-token-types [ctx]
  (-> ctx
      (ctx/with-token-type procedure-token-type)
      (ctx/with-token-type special-form-token-type)
      (ctx/with-token-type fn-token-type)
      (ctx/with-token-type symbol-token-type)
      (ctx/with-token-type var-token-type)))

(defmacro with-context [ctx & body]
  `(binding [ctx/*ctx* ~ctx]
     ~@body))

(defn new-ctx
  ([] (new-ctx {}))
  ([{:keys [program env token-types ds]}]
   (cond->
     (ctx/map->Ctx {:ps          (or program '())
                    :env         env
                    :token-types token-types
                    :ds          (or ds '())})
     (not env) (with-default-env)
     (not token-types) (with-default-token-types))))

(defmacro eval [& body]
  `(let [program# (quote ~body)]
     (ctx/eval (if (bound? #'ctx/*ctx*)
                 (ctx/with-program ctx/*ctx* program#)
                 (new-ctx (concat prologue
                                  program#))))))

(defmacro evaluations [& body]
  `(let [program# (quote ~body)
         ctx# (if (bound? #'ctx/*ctx*)
                (ctx/with-program ctx/*ctx* program#)
                (new-ctx {:ps (concat prologue
                                      program#)}))]
     (->> ctx#
          (iterate ctx/eval-1)
          (u/take-until-distinct))))

(defn result [ctx]
  (-> ctx
      :ds
      (first)))



(comment

  (do
    (defn op-hash []
        (println "Hello World"))

      (->> (eval
             asd)
           (with-context (-> {:ds '("pubkey" "privatekeysign")}
                             (new-ctx)
                             (ctx/bind-sym 'op-hash op-hash)))
           (result)))

  (evaluations
    1 2 ^{:arity 2} +
    )

  )
(ns polish.core
  (:refer-clojure :exclude [eval])
  (:require
    [polish.ctx :as ctx]
    [polish.special-form.dup :refer [dup-special-form]]
    [polish.special-form.swap :refer [swap-special-form]]
    [polish.special-form.invoke :refer [invoke-special-form]]
    [polish.special-form.drop :refer [drop-special-form]]
    [polish.special-form.let :refer [let-special-form]]
    [polish.special-form.procedure :refer [defproc-special-form procedure-token-type]]
    [polish.token.fn :refer [fn-token-type]]
    [polish.token.special-form :refer [special-form-token-type]]
    [polish.token.symbol :refer [symbol-token-type]]
    [polish.token.var :refer [var-token-type]]
    [polish.prologue :refer [prologue]]))

(defn with-default-env [ctx]
  (-> ctx
      (ctx/with-special-form defproc-special-form)
      (ctx/with-special-form let-special-form)
      (ctx/with-special-form invoke-special-form)
      (ctx/with-special-form drop-special-form)
      (ctx/with-special-form dup-special-form)
      (ctx/with-special-form swap-special-form)))

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
  ([] (new-ctx '()))
  ([program] (-> (ctx/map->Ctx {:ps program
                                :ds '()})
                 (with-default-env)
                 (with-default-token-types)))
  ([env program] (-> (ctx/map->Ctx {:env env
                                    :ps  program
                                    :ds  '()})
                     (with-default-token-types)))
  ([token-types env program] (ctx/map->Ctx {:token-types token-types
                                            :env         env
                                            :ps          program
                                            :ds          '()})))

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
                (new-ctx (concat prologue
                                 program#)))]
     (->> ctx#
          (iterate ctx/eval-1)
          (take-while some?))))

(defn result [ctx]
  (-> ctx
      :ds
      (first)))


(new-ctx '(1 2 ^:push + 2 invoke 1 ^{:arity 2} +))

;plus2 (x) = x + 2
(comment

  (eval

    defproc plus2 [x]
    ( x 2 add)

    1 plus2 plus2 dup add )

  (evaluations 1 2 ^{:arity 2} + inc)

  )
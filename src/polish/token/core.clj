(ns polish.token.core)

(defprotocol TokenType
  (satisfied? [this token])
  (handle [this token ctx]))

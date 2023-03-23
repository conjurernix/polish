(ns polish.token)

(defprotocol TokenType
  (satisfied? [this token])
  (handle [this token ctx]))

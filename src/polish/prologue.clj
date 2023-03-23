(ns polish.prologue)


(def prologue
  '(
     defproc add [x y]
     ( x y ^{:arity 2} + )

     defproc sub [x y]
     ( x y ^{:arity 2} - )

     defproc div [x y]
     ( x y ^{:arity 2} / )

     defproc mul [x y]
     ( x y ^{:arity 2} * ))

  )

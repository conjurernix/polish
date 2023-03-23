# Polish

This is a stack-based evaluation engine and embedded concatenative domain-specific language (DSL) written in Clojure. It
allows you to write programs using a stack-based approach, where each operation takes the top elements from the stack,
performs some computation, and pushes the result back onto the stack.

The language syntax is based on edn (extensible data notation), which is a subset of Clojure's syntax for expressing
data. This means that you can write programs using edn syntax and evaluate them using this library.

The library is open and extensible from Clojure, which means that you can easily add your own custom operations or
modify existing ones.

[//]: # (## Getting Started)

[//]: # ()
[//]: # (To use this library, you can include it as a dependency in your project by adding the following to your `project.clj`)

[//]: # (file:)

[//]: # ()
[//]: # (```clojure)

[//]: # ([drbuchkov/polish "0.1.0"])

[//]: # (```)

[//]: # ()
[//]: # (Then, you can use the functions and macros provided by the library in your Clojure code.)

## Usage

Here is an example of how to use this library to evaluate a simple program:

```clojure
(require '[polish.core :as p])
(require '[polish.ctx :as p.ctx])

(p.ctx/pop
  (p/eval
    1 2 add 3 mul))
;; => 9
```

In this example, we define a program that adds 1 and 2, multiplies the result by 3, and then evaluates the program
using the se/eval-program function.

## Language Syntax

The language syntax for this embedded concatenative DSL is based
on [edn (extensible data notation)](https://learnxinyminutes.com/docs/edn/), which is a subset of Clojure's syntax for
expressing data. In edn syntax, each program is represented as a concatenation of tokens, where each element can be
either a number, a string, keyword, symbol, map, vector, lists or sets.

Here is a more nuanced example of a program written in this language:

```clojure
; Adds 2 to a number
defproc add2 []
(2 add)

2 add2 dup add
; => 8
```

In this program, the `defproc` operator defines a new procedure, that pushes the value `2` on the stack and adds it with
the previous head of the stack. The expression `2 add2 dup add` leads to the following evaluations of the stack

```clojure
2
; => (2)
2 add2
; => (4)
2 add2 dup
; => (4 4)
2 add2 dup add
; => (8)
```

## Supported Operations

This library provides a number of built-in operations that you can use in your programs, including:

* add: Adds the top two elements on the stack.
* sub: Subtracts the second element on the stack from the top element.
* mul: Multiplies the top two elements on the stack.
* div: Divides the top element on the stack by the second element.
* dup: Duplicates the top element on the stack.
* drop: Drops the top element from the stack.
* swap: Swaps the top two elements on the stack.
* defproc: defines a new procedure (more examples will follow soon).
* let: defines scoped variables (more examples will follow soon)

## CLI

In the future, a command-line interface (CLI) may be provided for this library to allow you to interactively enter
programs and evaluate them. Stay tuned for updates!

## Contributing

If you find a bug or would like to contribute to this project, please feel free to open an issue or submit a pull
request on the GitHub repository.

## License

Copyright 2023 Nikolas Pafitis

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.











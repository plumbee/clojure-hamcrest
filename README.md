# Clojure-Hamcrest

A Clojure library inspired by [Java Hamcrest](http://hamcrest.org/JavaHamcrest/).
A specific aim of this library is to make usage for those familiar with the
Java version of Hamcrest as simple as possible.

## Usage

Intended for usage within a `clojure.test/deftest` expression.

Example REPL session:
```
(require '[com.plumbee.hamcrest.assert :refer [assert-that]])
=> nil
(require '[com.plumbee.hamcrest.matchers :refer :all])
=> nil
(assert-that 5 (greater-than 4))
=> nil
(assert-that 5 (greater-than 6))

FAIL in clojure.lang.PersistentList$EmptyList@1 (form-init3661951057044227532.clj:1)

expected: a number greater than <6>
  actual: was <5>
=> nil
```

For more examples, please see the tests.

## License (See LICENSE file for full license)

Copyright © 2016 Plumbee

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
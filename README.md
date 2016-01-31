# relative-timeout

An proof of concept implementation of a
[core.async](https://github.com/clojure/core.async) `timeout` channel
that uses a single threaded `ScheduledExecutorService` to schedule and
complete timeouts.

## Rationale

The core.async timeout implementation internally uses absolute
timestamps, calculated using `System/currentTimeMillis` and is
therefore subject to unpredictable behaviour when the system clock
changes.

This implementation provides an alternative timeout implementation,
with different tradeoffs, but is built on JDK constructs that use
monotonic clocks.

### Tradeoffs



## License

Copyright © 2016 Ragnar Dahlen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

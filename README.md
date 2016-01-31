# relative-timeout

An trivial proof of concept implementation of a
[core.async](https://github.com/clojure/core.async) `timeout` like
channel that is insensitive to clock changes.

*This document is a work in progress.*

## Rationale

The core.async timeout implementation internally uses absolute
timestamps, calculated using `System/currentTimeMillis` and is
therefore subject to unpredictable behaviour when the system clock
changes.

This implementation provides an alternative timeout implementation
that is built on JDK constructs that use relative time and monotonic
clocks. Concretely, a single threaded `ScheduledExecutorService` is
used to schedule and complete timeouts.

### Other Differences

**No sharing of channels** The default implementation shares timeout
channels. This means that timeouts expiring at the same absolute time
(with a granularity of 10ms) will be the same channel instance, even
if they were created at different times.

This has the following advantages:

- Fewer channels are potentially created meaning less resource
  (memory) usage
- At most one channel needs to be closed every 10ms. This means that
  there is an constant upper bound on the amount of work that the
  timout worker thread needs to do. This can be important if you have
  a large number of timeouts expiring at (almost) the same absolute
  time.
  
However, there are also certain disadvantages:

- Timeout channels aren't guaranteed to be distinct instances or have
  unique identities. For example, if you create two timeouts like so:

  ```clojure
  (def t1 (timeout 1000)
  (Thread/sleep (timeout 500)
  (def t2 (timeout 500)
  ```

  `t1` and `t2` could (depending on timing) be the exact same channel
  instance. This can have a number of surprising implications, for
  example if you want to store them in a set/map, or figure out which
  of them closed.
- The advantages listed are only realised if you actually
  (intentionally/unintentionally) request lots of timeouts expiring at
  the same time.


## License

Copyright © 2016 Ragnar Dahlen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[1]: I don't actually know the design reasons behind this, so this is my guess.

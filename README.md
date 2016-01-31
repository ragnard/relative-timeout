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

**No sharing of channels**
The default implementation shares channels in order to improve resouce usage<sup>1</sup>. If two ore more `timeouts` are created and set to expire at the same absolute time (within 10ms), the same underlying channel will be used for both. This means fewer channels are created. However, it also means that `timeout` channels don't necessarily have their own identity. If you create two timeouts like so:

```clojure
(def t1 (timeout 1000)
(Thread/sleep (timeout 500)
(def t2 (timeout 500)
```

`t1` and `t2` could (depending on timing) be the exact same channel, meaning you can't for example store them separetely in a map.




## License

Copyright © 2016 Ragnar Dahlen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[1]: I don't actually know the design reasons behind this, so this is my guess.

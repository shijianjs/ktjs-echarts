package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*

/** Fixed random source of `scatter-large`, standing in for its `Math.random()` calls. */
internal class ScatterRandom(seed: Int) {
    private var state = seed

    /** Next value in `0..1`. */
    fun next(): Double {
        state = state * 1664525 + 1013904223
        return (state ushr 1).toDouble() / 2147483648.0
    }
}

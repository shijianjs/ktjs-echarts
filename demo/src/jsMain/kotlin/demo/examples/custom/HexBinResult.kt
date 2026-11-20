package demo.examples.custom

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.examples.shared.every

/** Result of the binning: the largest bin and every bin. */
internal class HexBinResult(val maxBinLen: Int, val bins: List<HexBin>)

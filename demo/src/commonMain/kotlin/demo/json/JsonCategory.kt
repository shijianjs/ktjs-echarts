package demo.json

import io.github.shijianjs.ktjs.echarts.json.EChartsOption
import io.github.shijianjs.ktjs.echarts.json.echartsOption

class JsonCategory(private val sourcePath: String) {
    /** One example of this family. */
    fun example(id: String, block: EChartsOption.() -> Unit): JsonExample =
        JsonExample(id, sourcePath) { echartsOption(block) }
}

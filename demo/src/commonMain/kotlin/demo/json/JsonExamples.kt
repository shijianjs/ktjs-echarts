package demo.json

import io.github.shijianjs.kmpechartsjson.EChartsOption
import io.github.shijianjs.kmpechartsjson.echartsOption
import io.github.shijianjs.kmpechartsjson.toJsonString
import demo.json.examples.barJsonExamples
import demo.json.examples.calendarJsonExamples
import demo.json.examples.candleBoxHeatmapJsonExamples
import demo.json.examples.datasetJsonExamples
import demo.json.examples.docExtraJsonExamples
import demo.json.examples.docJsonExamples
import demo.json.examples.gaugeFunnelRadarJsonExamples
import demo.json.examples.graphChordJsonExamples
import demo.json.examples.lineJsonExamples
import demo.json.examples.parallelPictorialJsonExamples
import demo.json.examples.pieJsonExamples
import demo.json.examples.radarScatterExtraJsonExamples
import demo.json.examples.sankeyJsonExamples
import demo.json.examples.scatterJsonExamples
import demo.json.examples.sunburstJsonExamples
import demo.json.examples.treeJsonExamples

/**
 * One example rendered from a `kmp-echarts-json` option.
 *
 * The model lives in `commonMain`, so the same builder can be used by any target; the showcase only
 * needs the serialized [json]. [option] and [json] are built once, on the first page visit, because
 * examples carry generated data.
 */
class JsonExample internal constructor(
    /** Official example id; it is also the route id of the page that shows this example. */
    val id: String,
    /** Path of this file below `demo/src/commonMain/kotlin/demo`, used by the source link. */
    val sourcePath: String,
    private val build: () -> EChartsOption,
) {
    /** The option built by the model DSL. */
    val option: EChartsOption by lazy { build() }

    /** The pretty-printed JSON the chart consumes. */
    val json: String by lazy { option.toJsonString(prettyPrint = true) }
}

/**
 * Factory of JSON examples for one family file.
 *
 * Mirrors `demo.examples.Category`: the source path is bound once per file, and every example only
 * declares the official id plus the model builder.
 */
class JsonCategory(private val sourcePath: String) {
    /** One example of this family. */
    fun example(id: String, block: EChartsOption.() -> Unit): JsonExample =
        JsonExample(id, sourcePath) { echartsOption(block) }
}

/**
 * Every JSON example, keyed by the official example id.
 *
 * The js layer looks a page's id up in this map: a hit makes the top-bar Kotlin/JSON switch apply to
 * the page, and the JSON mode builds the chart from [JsonExample.json]. Examples that have no JSON
 * counterpart (3D charts, pages driven by user interaction or external assets) stay Kotlin-only and
 * are recorded in `demo/COVERAGE-JSON.md`.
 */
val jsonExamples: Map<String, JsonExample> =
    buildMap {
        for (examples in
            listOf(
                barJsonExamples,
                lineJsonExamples,
                pieJsonExamples,
                scatterJsonExamples,
                gaugeFunnelRadarJsonExamples,
                candleBoxHeatmapJsonExamples,
                graphChordJsonExamples,
                treeJsonExamples,
                sunburstJsonExamples,
                sankeyJsonExamples,
                calendarJsonExamples,
                datasetJsonExamples,
                docJsonExamples,
                docExtraJsonExamples,
                radarScatterExtraJsonExamples,
                parallelPictorialJsonExamples,
            )) {
            for (example in examples) put(example.id, example)
        }
    }

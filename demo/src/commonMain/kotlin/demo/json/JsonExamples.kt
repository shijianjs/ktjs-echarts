package demo.json

import io.github.shijianjs.ktjs.echarts.json.EChartsOption
import io.github.shijianjs.ktjs.echarts.json.echartsOption
import io.github.shijianjs.ktjs.echarts.json.toJsonString
import demo.json.examples.bar.barJsonExamples
import demo.json.examples.calendar.calendarJsonExamples
import demo.json.examples.candlestick.candlestickJsonExamples
import demo.json.examples.boxplot.boxplotJsonExamples
import demo.json.examples.heatmap.heatmapJsonExamples
import demo.json.examples.dataset.datasetJsonExamples
import demo.json.examples.doc.docJsonExamples
import demo.json.examples.gauge.gaugeJsonExamples
import demo.json.examples.funnel.funnelJsonExamples
import demo.json.examples.radar.radarJsonExamples
import demo.json.examples.graph.graphJsonExamples
import demo.json.examples.chord.chordJsonExamples
import demo.json.examples.line.lineJsonExamples
import demo.json.examples.parallel.parallelJsonExamples
import demo.json.examples.pictorialbar.pictorialbarJsonExamples
import demo.json.examples.pie.pieJsonExamples
import demo.json.examples.scatter.scatterJsonExamples
import demo.json.examples.sankey.sankeyJsonExamples
import demo.json.examples.themeriver.themeriverJsonExamples
import demo.json.examples.sunburst.sunburstJsonExamples
import demo.json.examples.tree.treeJsonExamples
import demo.json.examples.treemap.treemapJsonExamples

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


/** Every JSON example, keyed by the official example id. */
val jsonExamples: Map<String, JsonExample> =
    buildMap {
        for (examples in
            listOf(
                barJsonExamples,
                calendarJsonExamples,
                candlestickJsonExamples,
                boxplotJsonExamples,
                heatmapJsonExamples,
                datasetJsonExamples,
                docJsonExamples,
                gaugeJsonExamples,
                funnelJsonExamples,
                radarJsonExamples,
                graphJsonExamples,
                chordJsonExamples,
                lineJsonExamples,
                parallelJsonExamples,
                pictorialbarJsonExamples,
                pieJsonExamples,
                scatterJsonExamples,
                sankeyJsonExamples,
                themeriverJsonExamples,
                sunburstJsonExamples,
                treeJsonExamples,
                treemapJsonExamples,
            )) {
            for (example in examples) put(example.id, example)
        }
    }

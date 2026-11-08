package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.options.AxisMinMax
import io.github.shijianjs.ktjs.echarts.options.CustomSeriesOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.shared.numberAt

private val flameGraphCategory = Category(Groups.CUSTOM, "examples/custom/FlameGraph.kt")

val flameGraphEntry: CatalogEntry =
    flameGraphCategory.interactive(
            "flame-graph",
            "火焰图",
            "Flame graph",
            summaryZh = "堆栈采样数据按层级铺成火焰图；点击某个格子会以该帧为根重新展开。",
            summaryEn = "A sampled stack trace laid out as a flame graph; clicking a frame re-roots the graph on it.",
        ) { chart ->
            loadJson("data/stack-trace.json").then { json ->
                val root = json.unsafeCast<StackTraceNode>()
                chart.setOption(flameOption(root))
                chart.on("click") { event ->
                    val data = flameData(filterTrace(copyTrace(root), event.name))
                    val rootValue = numberAt(data[0]!!.unsafeCast<FlameFrame>().value, 2)
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            xAxis = one(unsafeJso<XAxisOption> { max = AxisMinMax(rootValue) })
                            series = one<SeriesOption>(unsafeJso<CustomSeriesOption> { typedData = data })
                        },
                    )
                }
                null
            }
        }

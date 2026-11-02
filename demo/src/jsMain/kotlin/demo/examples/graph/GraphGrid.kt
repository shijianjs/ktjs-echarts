package demo.examples.graph

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import zrender.ZRColor
import demo.examples.Category
import demo.examples.lines.coordinate

private val graphGridCategory = Category(Groups.GRAPH, "examples/graph/GraphGrid.kt")

val graphGridEntry: CatalogEntry =
    graphGridCategory.chart(
            "graph-grid",
            "笛卡尔坐标系上的 Graph",
            "Graph on Cartesian",
            "节点直接落在直角坐标系的取值上，链子从周一走到周日，`layout: 'none'` 让位置完全由数据决定。",
            "Nodes placed on a `cartesian2d` coordinate system with `layout: 'none'`, chained from " +
                "Monday to Sunday.",
        ) {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Graph on Cartesian" })
                tooltip = one(unsafeJso<TooltipOption> {})
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings(*GRID_DAYS)
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        graphSeries {
                            layout = GraphLayouts.none
                            coordinateSystem = "cartesian2d"
                            symbolSize = SymbolSize(40.0)
                            label = unsafeJso<LabelOption> { show = true }
                            edgeSymbol = StringOrStringArray(arrayOf("circle", "arrow"))
                            edgeSymbolSize = DoubleOrDoubleArray(arrayOf(4.0, 10.0))
                            typedData = numbers(*GRID_VALUES.toDoubleArray())
                            links = GRID_LINKS
                            lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#2f4554") }
                        },
                    )
            }
        }

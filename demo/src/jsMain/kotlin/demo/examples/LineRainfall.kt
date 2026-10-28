package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor

/*
 * The two rainfall pages, ported from `public/examples/ts/grid-multiple.ts` and
 * `public/examples/ts/area-rainfall.ts`.
 *
 * Both upstream files inline a 3079-point hourly table (June 12 to September 3); the tables live in
 * `resources/data/rainfall-evaporation.json` and `rainfall-flow.json` instead. The upstream
 * `area-rainfall` file repeats the same `time` array in its x axis, so each JSON keeps its own copy
 * of the labels — including the duplicated `2009/6/15 0:00` the original table contains.
 */

private val rainfallLines = Category(Groups.LINE, "examples/LineRainfall.kt")

private external interface RainfallEvaporationData {
    val time: Array<String>
    val evaporation: Array<Double>
    val rainfall: Array<Double>
}

private external interface RainfallFlowData {
    val time: Array<String>
    val flow: Array<Double>
    val rainfall: Array<Double>
}

private fun rainfallToolbox(): Array<ToolboxOption> =
    one(
        unsafeJso<ToolboxOption> {
            feature =
                unsafeJso<ToolboxFeature> {
                    dataZoom = unsafeJso<ToolboxDataZoomFeatureOption> { yAxisIndex = ModelFinderIndexQuery("none") }
                    restore = unsafeJso<ToolboxFeatureOption> {}
                    saveAsImage = unsafeJso<SaveAsImageOption> {}
                }
        },
    )

/** A shaded `[from, to]` band between two category labels. */
private fun band(from: String, to: String): MarkAreaDataItem =
    MarkAreaDataItem(
        arrayOf(
            unsafeJso<MarkArea2DDataItemDimOption> { xAxis = ScaleDataValue(from) },
            unsafeJso<MarkArea2DDataItemDimOption> { xAxis = ScaleDataValue(to) },
        ),
    )

private fun gridMultipleOption(table: RainfallEvaporationData): EChartsOption =
    unsafeJso {
        title = one(unsafeJso<TitleOption> { text = "Rainfall vs Evaporation"; left = StringOrDouble("center") })
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.axis
                    axisPointer = unsafeJso<AxisPointerOption> { animation = BooleanOrString(false) }
                },
            )
        legend = one(unsafeJso<LegendOption> { data = legendNames("Evaporation", "Rainfall"); left = StringOrDouble(10.0) })
        toolbox = rainfallToolbox()
        axisPointer =
            one(
                unsafeJso<AxisPointerOption> {
                    link = one(unsafeJso<AxisPointerLink> { xAxisIndex = ModelFinderIndexQuery("all") })
                },
            )
        dataZoom =
            arrayOf(
                unsafeJso<DataZoomOption> {
                    show = true
                    realtime = true
                    start = 30.0
                    end = 70.0
                    xAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
                },
                unsafeJso<DataZoomOption> {
                    type = DataZoomTypes.inside
                    realtime = true
                    start = 30.0
                    end = 70.0
                    xAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
                },
            )
        grid =
            arrayOf(
                unsafeJso<GridOption> {
                    left = StringOrDouble(60.0)
                    right = StringOrDouble(50.0)
                    height = StringOrDouble("35%")
                },
                unsafeJso<GridOption> {
                    left = StringOrDouble(60.0)
                    right = StringOrDouble(50.0)
                    top = StringOrDouble("55%")
                    height = StringOrDouble("35%")
                },
            )
        xAxis =
            arrayOf(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    boundaryGap = BooleanOrStringArray(false)
                    axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(true) }
                    data = strings(*table.time)
                },
                unsafeJso<XAxisOption> {
                    gridIndex = 1.0
                    type = AxisTypes.category
                    boundaryGap = BooleanOrStringArray(false)
                    axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(true) }
                    data = strings(*table.time)
                    position = AxisPosition("top")
                },
            )
        yAxis =
            arrayOf(
                unsafeJso<YAxisOption> {
                    name = "Evaporation(m³/s)"
                    type = AxisTypes.value
                    max = AxisMinMax(500.0)
                },
                unsafeJso<YAxisOption> {
                    gridIndex = 1.0
                    name = "Rainfall(mm)"
                    type = AxisTypes.value
                    inverse = true
                },
            )
        series =
            arrayOf<SeriesOption>(
                lineSeries {
                    name = "Evaporation"
                    symbolSize = SymbolSize(8.0)
                    typedData = numbers(*table.evaporation.toDoubleArray())
                },
                lineSeries {
                    name = "Rainfall"
                    symbolSize = SymbolSize(8.0)
                    xAxisIndex = 1.0
                    yAxisIndex = 1.0
                    typedData = numbers(*table.rainfall.toDoubleArray())
                },
            )
    }

private fun rainfallLine(
    lineName: String,
    values: DoubleArray,
    from: String,
    to: String,
    secondAxis: Boolean,
): SeriesOption =
    lineSeries {
        name = lineName
        if (secondAxis) yAxisIndex = 1.0
        areaStyle = unsafeJso<AreaStyleOption> {}
        lineStyle = unsafeJso<LineStyleOption> { width = 1.0 }
        emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
        markArea =
            unsafeJso<MarkAreaOption> {
                silent = true
                itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.3 }
                data = arrayOf(band(from, to))
            }
        typedData = numbers(*values)
    }

private fun areaRainfallOption(table: RainfallFlowData): EChartsOption =
    unsafeJso {
        title = one(unsafeJso<TitleOption> { text = "Rainfall and Flow Relationship"; left = StringOrDouble("center") })
        grid = one(unsafeJso<GridOption> { bottom = StringOrDouble(80.0) })
        toolbox = rainfallToolbox()
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.axis
                    axisPointer =
                        unsafeJso<AxisPointerOption> {
                            type = AxisPointerTypes.cross
                            animation = BooleanOrString(false)
                            label = unsafeJso<AxisPointerLabelOption> { backgroundColor = ZRColor("#505765") }
                        }
                },
            )
        legend = one(unsafeJso<LegendOption> { data = legendNames("Flow", "Rainfall"); left = StringOrDouble(10.0) })
        dataZoom =
            arrayOf(
                unsafeJso<DataZoomOption> { show = true; realtime = true; start = 65.0; end = 85.0 },
                unsafeJso<DataZoomOption> { type = DataZoomTypes.inside; realtime = true; start = 65.0; end = 85.0 },
            )
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    boundaryGap = BooleanOrStringArray(false)
                    axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
                    data = strings(*table.time)
                },
            )
        yAxis =
            arrayOf(
                unsafeJso<YAxisOption> { name = "Flow(m³/s)"; type = AxisTypes.value },
                unsafeJso<YAxisOption> {
                    name = "Rainfall(mm)"
                    nameLocation = AxisNameLocations.start
                    alignTicks = true
                    type = AxisTypes.value
                    inverse = true
                },
            )
        series =
            arrayOf<SeriesOption>(
                rainfallLine("Flow", table.flow.toDoubleArray(), "2009/9/12\n7:00", "2009/9/22\n7:00", secondAxis = false),
                rainfallLine("Rainfall", table.rainfall.toDoubleArray(), "2009/9/10\n7:00", "2009/9/20\n7:00", secondAxis = true),
            )
    }

val rainfallLineEntries: List<CatalogEntry> =
    listOf(
        rainfallLines.interactive(
            "grid-multiple",
            "雨量蒸发量关系图",
            "Rainfall vs Evaporation",
            "上下两个网格共享 3079 个小时级刻度，上轴蒸发量、下轴雨量，dataZoom 与轴线联动。",
            "Two grids over 3079 hourly ticks: evaporation above, rainfall below, linked zoom and axes.",
        ) { chart ->
            loadJson("data/rainfall-evaporation.json").then { json ->
                chart.setOption(gridMultipleOption(json.unsafeCast<RainfallEvaporationData>()))
                null
            }
        },
        rainfallLines.interactive(
            "area-rainfall",
            "雨量流量关系图",
            "Rainfall and Flow Relationship",
            "流量与雨量的双轴面积图，九月两段降雨用 markArea 标出，双 y 轴自动对齐刻度。",
            "Flow and rainfall as two axes of area series, with the September rain spells marked.",
        ) { chart ->
            loadJson("data/rainfall-flow.json").then { json ->
                chart.setOption(areaRainfallOption(json.unsafeCast<RainfallFlowData>()))
                null
            }
        },
    )

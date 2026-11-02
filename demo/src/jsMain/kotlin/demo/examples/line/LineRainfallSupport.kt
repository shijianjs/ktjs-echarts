package demo.examples.line

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.funnel.labels
import demo.examples.gauge.center
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

internal external interface RainfallEvaporationData {
    val time: Array<String>
    val evaporation: Array<Double>
    val rainfall: Array<Double>
}

internal external interface RainfallFlowData {
    val time: Array<String>
    val flow: Array<Double>
    val rainfall: Array<Double>
}

internal fun rainfallToolbox(): Array<ToolboxOption> =
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
internal fun band(from: String, to: String): MarkAreaDataItem =
    MarkAreaDataItem(
        arrayOf(
            unsafeJso<MarkArea2DDataItemDimOption> { xAxis = ScaleDataValue(from) },
            unsafeJso<MarkArea2DDataItemDimOption> { xAxis = ScaleDataValue(to) },
        ),
    )

internal fun gridMultipleOption(table: RainfallEvaporationData): EChartsOption =
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

internal fun rainfallLine(
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

internal fun areaRainfallOption(table: RainfallFlowData): EChartsOption =
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

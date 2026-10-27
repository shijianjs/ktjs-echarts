package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AxisPointerTypes
import echarts.AxisTypes
import echarts.TooltipTriggers
import echarts.options.AxisMinMax
import echarts.options.AxisPointerOption
import echarts.options.BooleanOrStringArray
import echarts.options.DataTransformOption
import echarts.options.DataZoomOption
import echarts.options.DatasetOption
import echarts.options.DimensionLoose
import echarts.DataZoomTypes
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.IndexOrIndexArray
import echarts.options.LegendOption
import echarts.options.OptionEncode
import echarts.options.OptionEncodeValue
import echarts.options.ScatterSeriesOption
import echarts.options.SeriesOption
import echarts.options.SplitAreaOption
import echarts.options.SplitLineOption
import echarts.options.StringOrDouble
import echarts.options.TextStyleOption
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.boxplotSeries
import echarts.options.one
import echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.JsAny
import zrender.ZRFontWeights
import zrender.ZRColor

/*
 * Boxplot examples ported from `public/examples/ts/boxplot-*.ts` of apache/echarts-examples.
 *
 * The two Michelson-Morley examples feed raw measurements through the `boxplot` dataset transform,
 * which exercises `dataset` + `transform` through the typed API.
 */

private val boxplot = Category(Groups.BOXPLOT, "examples/Boxplot.kt")

/** The 5 x 20 measurements of the Michelson-Morley experiment. */
private val michelson =
    listOf(
        doubleArrayOf(850.0, 740.0, 900.0, 1070.0, 930.0, 850.0, 950.0, 980.0, 980.0, 880.0, 1000.0, 980.0, 930.0, 650.0, 760.0, 810.0, 1000.0, 1000.0, 960.0, 960.0),
        doubleArrayOf(960.0, 940.0, 960.0, 940.0, 880.0, 800.0, 850.0, 880.0, 900.0, 840.0, 830.0, 790.0, 810.0, 880.0, 880.0, 830.0, 800.0, 790.0, 760.0, 800.0),
        doubleArrayOf(880.0, 880.0, 880.0, 860.0, 720.0, 720.0, 620.0, 860.0, 970.0, 950.0, 880.0, 910.0, 850.0, 870.0, 840.0, 840.0, 850.0, 840.0, 840.0, 840.0),
        doubleArrayOf(890.0, 810.0, 810.0, 820.0, 800.0, 770.0, 760.0, 740.0, 750.0, 760.0, 910.0, 920.0, 890.0, 860.0, 880.0, 720.0, 840.0, 850.0, 850.0, 780.0),
        doubleArrayOf(890.0, 840.0, 780.0, 810.0, 760.0, 810.0, 790.0, 810.0, 820.0, 850.0, 870.0, 870.0, 810.0, 740.0, 810.0, 940.0, 950.0, 800.0, 810.0, 870.0),
    )

/** The `boxplot` transform, optionally with a custom item name formatter. */
private fun boxplotTransform(itemNameFormatter: String? = null): DataTransformOption =
    unsafeJso {
        type = "boxplot"
        if (itemNameFormatter != null) {
            config = recordOf("itemNameFormatter" to (itemNameFormatter as JsAny))
        }
    }

/** `boxplot-multi` generates three 18 x 100 tables; a fixed sequence keeps the page reproducible. */
private fun generatedTables(): List<DoubleArray> {
    var state = 20261027L
    fun next(): Double {
        state = (state * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
        return (state % 100000L).toDouble() / 100000.0
    }
    val rows = mutableListOf<DoubleArray>()
    repeat(3) { repeat(18) { rows += DoubleArray(100) { next() * 200.0 } } }
    return rows
}

val boxplotEntries: List<CatalogEntry> =
    listOf(
        boxplot.chart("boxplot-light-velocity", "基础盒须图", "Boxplot Light Velocity") {
            unsafeJso<EChartsOption> {
                title = michelsonTitles(normalWeight = true)
                dataset =
                    arrayOf(
                        unsafeJso<DatasetOption> { source = numberRows(michelson) },
                        unsafeJso<DatasetOption> { transform = arrayOf(boxplotTransform("expr {value}")) },
                        unsafeJso<DatasetOption> { fromDatasetIndex = 1.0; fromTransformResult = 1.0 },
                    )
                tooltip = boxplotTooltip()
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble("10%")
                            bottom = StringOrDouble("15%")
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(true)
                            nameGap = 30.0
                            splitArea = unsafeJso<SplitAreaOption> { show = false }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "km/s minus 299,000"
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        boxplotSeries { name = "boxplot"; datasetIndex = 1.0 },
                        scatterSeries { name = "outlier"; datasetIndex = 2.0 },
                    )
            }
        },
        boxplot.chart("boxplot-light-velocity2", "垂直方向盒须图", "Boxplot Light Velocity2") {
            unsafeJso<EChartsOption> {
                title = michelsonTitles(normalWeight = false)
                dataset =
                    arrayOf(
                        unsafeJso<DatasetOption> { source = numberRows(michelson) },
                        unsafeJso<DatasetOption> { transform = arrayOf(boxplotTransform()) },
                        unsafeJso<DatasetOption> { fromDatasetIndex = 1.0; fromTransformResult = 1.0 },
                    )
                tooltip = boxplotTooltip()
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble("10%")
                            bottom = StringOrDouble("15%")
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(true)
                            nameGap = 30.0
                            splitArea = unsafeJso<SplitAreaOption> { show = false }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            name = "km/s minus 299,000"
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        boxplotSeries { name = "boxplot"; datasetIndex = 1.0 },
                        outlierSeries(datasetIndex = 2.0, encoded = true),
                    )
            }
        },
        boxplot.chart("boxplot-multi", "多系列盒须图", "Multiple Categories") {
            val tables = generatedTables()
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Multiple Categories"
                            left = StringOrDouble("center")
                        },
                    )
                dataset =
                    arrayOf(
                        unsafeJso<DatasetOption> { source = numberRows(tables.subList(0, 18)) },
                        unsafeJso<DatasetOption> { source = numberRows(tables.subList(18, 36)) },
                        unsafeJso<DatasetOption> { source = numberRows(tables.subList(36, 54)) },
                        unsafeJso<DatasetOption> { fromDatasetIndex = 0.0; transform = arrayOf(boxplotTransform()) },
                        unsafeJso<DatasetOption> { fromDatasetIndex = 1.0; transform = arrayOf(boxplotTransform()) },
                        unsafeJso<DatasetOption> { fromDatasetIndex = 2.0; transform = arrayOf(boxplotTransform()) },
                    )
                legend = one(unsafeJso<LegendOption> { top = StringOrDouble("10%") })
                tooltip = boxplotTooltip()
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            top = StringOrDouble("20%")
                            right = StringOrDouble("10%")
                            bottom = StringOrDouble("15%")
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(true)
                            nameGap = 30.0
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "Value"
                            min = AxisMinMax(-400.0)
                            max = AxisMinMax(600.0)
                            splitArea = unsafeJso<SplitAreaOption> { show = false }
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.inside; start = 0.0; end = 20.0 },
                        unsafeJso<DataZoomOption> {
                            show = true
                            type = DataZoomTypes.slider
                            top = StringOrDouble("90%")
                            xAxisIndex = IndexOrIndexArray(arrayOf(0.0))
                            start = 0.0
                            end = 20.0
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        boxplotSeries { name = "category0"; datasetIndex = 3.0 },
                        boxplotSeries { name = "category1"; datasetIndex = 4.0 },
                        boxplotSeries { name = "category2"; datasetIndex = 5.0 },
                    )
            }
        },
    )

/** The outlier scatter of the Michelson-Morley examples; the second one swaps the axes. */
private fun outlierSeries(datasetIndex: Double, encoded: Boolean): ScatterSeriesOption =
    scatterSeries {
        name = "outlier"
        this.datasetIndex = datasetIndex
        if (encoded) {
            encode =
                unsafeJso<OptionEncode> {
                    x = OptionEncodeValue(DimensionLoose(1.0))
                    y = OptionEncodeValue(DimensionLoose(0.0))
                }
        }
    }

/** The two captions of the Michelson-Morley examples. */
private fun michelsonTitles(normalWeight: Boolean): Array<TitleOption> =
    arrayOf(
        unsafeJso<TitleOption> {
            text = "Michelson-Morley Experiment"
            left = StringOrDouble("center")
        },
        unsafeJso<TitleOption> {
            text = "upper: Q3 + 1.5 * IQR \nlower: Q1 - 1.5 * IQR"
            borderColor = ZRColor("#999")
            borderWidth = 1.0
            textStyle =
                unsafeJso<TextStyleOption> {
                    if (normalWeight) fontWeight = ZRFontWeights.normal
                    fontSize = StringOrDouble(14.0)
                    lineHeight = 20.0
                }
            left = StringOrDouble("10%")
            top = StringOrDouble("90%")
        },
    )

private fun boxplotTooltip(): Array<TooltipOption> =
    one(
        unsafeJso<TooltipOption> {
            trigger = TooltipTriggers.item
            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
        },
    )

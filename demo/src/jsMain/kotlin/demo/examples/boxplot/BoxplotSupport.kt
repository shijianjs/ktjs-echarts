package demo.examples.boxplot

import echarts.AxisPointerTypes
import echarts.TooltipTriggers
import echarts.options.AxisPointerOption
import echarts.options.DataTransformOption
import echarts.options.DimensionLoose
import echarts.options.OptionEncode
import echarts.options.OptionEncodeValue
import echarts.options.ScatterSeriesOption
import echarts.options.StringOrDouble
import echarts.options.TextStyleOption
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.one
import echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.JsAny
import zrender.ZRFontWeights
import zrender.ZRColor
import demo.examples.dataset.transform
import demo.examples.gauge.center
import demo.examples.shared.fixed
import demo.examples.shared.recordOf

/** The 5 x 20 measurements of the Michelson-Morley experiment. */
internal val michelson =
    listOf(
        doubleArrayOf(850.0, 740.0, 900.0, 1070.0, 930.0, 850.0, 950.0, 980.0, 980.0, 880.0, 1000.0, 980.0, 930.0, 650.0, 760.0, 810.0, 1000.0, 1000.0, 960.0, 960.0),
        doubleArrayOf(960.0, 940.0, 960.0, 940.0, 880.0, 800.0, 850.0, 880.0, 900.0, 840.0, 830.0, 790.0, 810.0, 880.0, 880.0, 830.0, 800.0, 790.0, 760.0, 800.0),
        doubleArrayOf(880.0, 880.0, 880.0, 860.0, 720.0, 720.0, 620.0, 860.0, 970.0, 950.0, 880.0, 910.0, 850.0, 870.0, 840.0, 840.0, 850.0, 840.0, 840.0, 840.0),
        doubleArrayOf(890.0, 810.0, 810.0, 820.0, 800.0, 770.0, 760.0, 740.0, 750.0, 760.0, 910.0, 920.0, 890.0, 860.0, 880.0, 720.0, 840.0, 850.0, 850.0, 780.0),
        doubleArrayOf(890.0, 840.0, 780.0, 810.0, 760.0, 810.0, 790.0, 810.0, 820.0, 850.0, 870.0, 870.0, 810.0, 740.0, 810.0, 940.0, 950.0, 800.0, 810.0, 870.0),
    )

/** The `boxplot` transform, optionally with a custom item name formatter. */
internal fun boxplotTransform(itemNameFormatter: String? = null): DataTransformOption =
    unsafeJso {
        type = "boxplot"
        if (itemNameFormatter != null) {
            config = recordOf("itemNameFormatter" to (itemNameFormatter as JsAny))
        }
    }

/** `boxplot-multi` generates three 18 x 100 tables; a fixed sequence keeps the page reproducible. */
internal fun generatedTables(): List<DoubleArray> {
    var state = 20261027L
    fun next(): Double {
        state = (state * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
        return (state % 100000L).toDouble() / 100000.0
    }
    val rows = mutableListOf<DoubleArray>()
    repeat(3) { repeat(18) { rows += DoubleArray(100) { next() * 200.0 } } }
    return rows
}

/** The outlier scatter of the Michelson-Morley examples; the second one swaps the axes. */
internal fun outlierSeries(datasetIndex: Double, encoded: Boolean): ScatterSeriesOption =
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
internal fun michelsonTitles(normalWeight: Boolean): Array<TitleOption> =
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

internal fun boxplotTooltip(): Array<TooltipOption> =
    one(
        unsafeJso<TooltipOption> {
            trigger = TooltipTriggers.item
            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
        },
    )

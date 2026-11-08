package demo.examples.shared

import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.options.DimensionDefinitionLoose
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TextStyleOption
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.scatterSeries
import io.github.shijianjs.ktjs.echarts.gl.CoordinateSystems2D
import io.github.shijianjs.ktjs.echarts.gl.GlColor
import io.github.shijianjs.ktjs.echarts.gl.options.EChartsGlOption
import io.github.shijianjs.ktjs.echarts.gl.options.GlItemStyleOption
import io.github.shijianjs.ktjs.echarts.gl.options.scatterGLSeries
import js.buffer.ArrayBuffer
import js.objects.unsafeJso
import js.typedarrays.Float64Array
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.math.round
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.chord.column
import demo.examples.gauge.center
import demo.examples.scatter.point

/** The three strengths of the check-in table, in the order `weibo.json` stores them. */
internal val weiboSeriesNames = arrayOf("弱", "中", "强")

/** The point color of each series; the GL page drops the shadow that goes with it on the 2D one. */
internal val weiboColors =
    arrayOf(
        "rgba(37, 140, 249, 0.8)",
        "rgba(14, 241, 242, 0.8)",
        "rgba(255, 255, 255, 0.8)",
    )

/** The three columns of a decoded series, needed because flat data has no row boundaries. */
internal fun weiboDimensions() =
    arrayOf(
        DimensionDefinitionLoose("lng"),
        DimensionDefinitionLoose("lat"),
        DimensionDefinitionLoose("value"),
    )

/** The check-in table: three flat delta-encoded series, with the map registered underneath. */
internal fun loadWeibo(): Promise<JsAny> =
    Promise.all(arrayOf(registerChinaMap(), loadJson("data/weibo.json"))).then { loaded -> loaded[1] }

/**
 * Decodes the [index] series of [table] into `[lng, lat, 1]` rows.
 *
 * The coordinates are already longitude and latitude, so they land on `china.json` directly; only the
 * scaling by 1000 and the delta summing are undone here, with the two-decimal rounding upstream
 * applies to every point after the first.
 */
internal fun weiboPoints(table: JsAny, index: Int): Float64Array<ArrayBuffer> {
    val row: Array<Double> = js("(table, index) => table[index]")(table, index) as Array<Double>
    val out = Float64Array<ArrayBuffer>((row.size - 2) / 2 * 3 + 3)
    var px = row[0] / 1000
    var py = row[1] / 1000
    var written = 0
    out[written++] = px
    out[written++] = py
    out[written++] = 1.0
    var column = 2
    while (column < row.size) {
        px += row[column] / 1000
        py += row[column + 1] / 1000
        out[written++] = round(px * 100) / 100
        out[written++] = round(py * 100) / 100
        out[written++] = 1.0
        column += 2
    }
    return out
}

internal fun weiboTitle(): Array<TitleOption> =
    one(
        unsafeJso<TitleOption> {
            text = "微博签到数据点亮中国"
            subtext = "From ThinkGIS"
            sublink = "http://www.thinkgis.cn/public/sina"
            left = StringOrDouble("center")
            top = StringOrDouble("top")
            textStyle = unsafeJso<TextStyleOption> { color = "#fff" }
        },
    )

internal fun weiboLegend(): Array<LegendOption> =
    one(
        unsafeJso<LegendOption> {
            left = StringOrDouble("left")
            data = legendNames("强", "中", "弱")
            textStyle = unsafeJso<LabelOption> { color = "#ccc" }
        },
    )

internal fun weiboScatterOption(table: JsAny): EChartsOption =
    unsafeJso {
        backgroundColor = ZRColor("#404a59")
        title = weiboTitle()
        tooltip = one(unsafeJso<TooltipOption> {})
        legend = weiboLegend()
        geo = one(darkChinaGeo(roam = true))
        series =
            Array<SeriesOption>(weiboSeriesNames.size) { index ->
                scatterSeries {
                    name = weiboSeriesNames[index]
                    coordinateSystem = "geo"
                    symbolSize = SymbolSize(1.0)
                    large = true
                    dimensions = weiboDimensions()
                    typedFlatData = weiboPoints(table, index)
                    itemStyle =
                        unsafeJso<ItemStyleOption> {
                            shadowBlur = 2.0
                            shadowColor = ZRColor(weiboColors[index])
                            color = ZRColor(weiboColors[index])
                        }
                }
            }
    }

internal fun weiboGlOption(table: JsAny): EChartsGlOption =
    unsafeJso {
        backgroundColor = ZRColor("#404a59")
        title = weiboTitle()
        tooltip = one(unsafeJso<TooltipOption> {})
        legend = weiboLegend()
        geo = one(darkChinaGeo(roam = true))
        series =
            Array<SeriesOption>(weiboSeriesNames.size) { index ->
                scatterGLSeries {
                    name = weiboSeriesNames[index]
                    coordinateSystem = "geo"
                    typedCoordinateSystem = CoordinateSystems2D.geo
                    symbolSize = SymbolSize(1.0)
                    dimensions = weiboDimensions()
                    typedFlatData = weiboPoints(table, index)
                    typedItemStyle =
                        unsafeJso<GlItemStyleOption> {
                            color = GlColor(ZRColor(weiboColors[index]))
                        }
                }
            }
    }

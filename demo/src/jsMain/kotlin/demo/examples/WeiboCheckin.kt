package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.options.DimensionDefinitionLoose
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LabelOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolSize
import echarts.options.TextStyleOption
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.one
import echarts.options.scatterSeries
import echartsgl.CoordinateSystems2D
import echartsgl.GlColor
import echartsgl.options.EChartsGlOption
import echartsgl.options.GlItemStyleOption
import echartsgl.options.scatterGLSeries
import js.buffer.ArrayBuffer
import js.objects.unsafeJso
import js.typedarrays.Float64Array
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.math.round
import zrender.ZRColor

/*
 * The two pages that light up China with the weibo check-in table: the 2D `scatter-weibo` and its
 * WebGL twin `scattergl-weibo`.
 *
 * `weibo.json` is not a list of points: each of its three series is one flat
 * `[x0, y0, dx1, dy1, dx2, dy2, ...]` array scaled by 1000, where every point is the running sum of
 * the deltas before it. [weiboPoints] decodes one series into the flat `[lng, lat, value]` rows of a
 * large-data series, which is the branch both pages use: 118k points as arrays of arrays would mean
 * 118k allocations per page.
 *
 * Changed on purpose:
 *
 * - upstream's first point of every series carries only `[lng, lat]`; a flat row table has one fixed
 *   width, so it gets the same trailing `1` as the rest. Nothing reads it — neither page has a
 *   `visualMap`;
 * - `scattergl-weibo` is drawn on the `theme: dark` of the examples site, so it sets the background of
 *   the 2D page explicitly: the site theme is a visitor setting and white text on white is not a
 *   demo;
 * - the `shadowBlur` / `shadowColor` of the GL page are left out. `scatterGL` builds its symbol sprite
 *   in `PointsBuilder` with `shadowColor: 'transparent'` and reads only `lineWidth` from the style, so
 *   the two keys are dead in the GL renderer;
 * - the ECharts 4 `itemStyle.normal` state of the GL page is written in the current flat form.
 */

private val weiboScatter = Category(Groups.SCATTER, "examples/WeiboCheckin.kt")
private val weiboGl = Category(Groups.GL, "examples/WeiboCheckin.kt")

/** The three strengths of the check-in table, in the order `weibo.json` stores them. */
private val weiboSeriesNames = arrayOf("弱", "中", "强")

/** The point color of each series; the GL page drops the shadow that goes with it on the 2D one. */
private val weiboColors =
    arrayOf(
        "rgba(37, 140, 249, 0.8)",
        "rgba(14, 241, 242, 0.8)",
        "rgba(255, 255, 255, 0.8)",
    )

/** The three columns of a decoded series, needed because flat data has no row boundaries. */
private fun weiboDimensions() =
    arrayOf(
        DimensionDefinitionLoose("lng"),
        DimensionDefinitionLoose("lat"),
        DimensionDefinitionLoose("value"),
    )

/** The check-in table: three flat delta-encoded series, with the map registered underneath. */
private fun loadWeibo(): Promise<JsAny> =
    Promise.all(arrayOf(registerChinaMap(), loadJson("data/weibo.json"))).then { loaded -> loaded[1] }

/**
 * Decodes the [index] series of [table] into `[lng, lat, 1]` rows.
 *
 * The coordinates are already longitude and latitude, so they land on `china.json` directly; only the
 * scaling by 1000 and the delta summing are undone here, with the two-decimal rounding upstream
 * applies to every point after the first.
 */
private fun weiboPoints(table: JsAny, index: Int): Float64Array<ArrayBuffer> {
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

private fun weiboTitle(): Array<TitleOption> =
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

private fun weiboLegend(): Array<LegendOption> =
    one(
        unsafeJso<LegendOption> {
            left = StringOrDouble("left")
            data = legendNames("强", "中", "弱")
            textStyle = unsafeJso<LabelOption> { color = "#ccc" }
        },
    )

private fun weiboScatterOption(table: JsAny): EChartsOption =
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

private fun weiboGlOption(table: JsAny): EChartsGlOption =
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

/** The two check-in pages, one drawn by zrender and one on the GPU. */
val weiboCheckinEntries: List<CatalogEntry> =
    listOf(
        weiboScatter.interactive(
            "scatter-weibo",
            "微博签到数据点亮中国",
            "Sign in of weibo",
            "11.8 万个签到点按强度分三组画在 `geo` 上，`large` 与扁平数据数组让页面撑得住这个量级。",
            "118k check-in points in three strength groups drawn on a `geo`; `large` plus flat data " +
                "arrays are what keep the page usable at that size.",
        ) { chart ->
            chart.showLoading(null)
            loadWeibo().then { table ->
                if (!chart.isDisposed()) {
                    chart.hideLoading()
                    chart.setOption(weiboScatterOption(table))
                }
                null
            }
        },
        weiboGl.interactive(
            "scattergl-weibo",
            "微博签到数据点亮中国 - WebGL",
            "Sign in of weibo (WebGL)",
            "同一份签到表交给 `scatterGL` 在 GPU 上画，漫游地图时依然流畅；GL 渲染器不读 itemStyle 的阴影。",
            "The same table drawn by `scatterGL` on the GPU, so roaming stays smooth; the GL renderer " +
                "does not read the shadow keys of `itemStyle`.",
        ) { chart ->
            chart.showLoading(null)
            loadWeibo().then { table ->
                if (!chart.isDisposed()) {
                    chart.hideLoading()
                    chart.setOption(weiboGlOption(table))
                }
                null
            }
        },
    )

package echarts.options

import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import zrender.ImageLike
import zrender.ImagePatternObject
import zrender.PatternObject
import zrender.SVGPatternObject
import zrender.ZRColor

/**
 * Tests for the colour members that accept more than a plain string.
 *
 * Upstream types `itemStyle.color` as `ZRColor | callback(params)` and lets a `PatternObject` stand
 * in for a `ZRColor` wherever zrender paints one; the rich-text pages of the demo are the first
 * callers of both.
 */
class ZRColorAndPatternTest {
    @Test
    fun colorAcceptsACallbackOfTheDataItemParams() {
        val colors = arrayOf("#5470c6", "#91cc75")
        val color = ZRColor({ params -> colors[params.dataIndex] })

        val series = scatterSeries {
            itemStyle = unsafeJso<ItemStyleOption> { this.color = color }
            typedData = points<ScatterDataItem>(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        }

        val pick = series.itemStyle!!.color!!.unsafeCast<(CallbackDataParams) -> String>()
        val params = js("({ dataIndex: 1 })") as CallbackDataParams
        assertEquals("#91cc75", pick(params))
    }

    @Test
    fun anImagePatternIsAColor() {
        val image: web.html.HTMLImageElement =
            js("(url) => { const img = new Image(); img.src = url; return img; }")("data/missing.png")
        val pattern =
            PatternObject(
                unsafeJso<ImagePatternObject> {
                    this.image = ImageLike(image)
                    repeat = "no-repeat"
                    imageWidth = 128.0
                    imageHeight = 128.0
                },
            )

        val label: LabelOption = unsafeJso { backgroundColor = ZRColor(pattern) }
        assertEquals<JsAny?>(pattern, label.backgroundColor)
    }

    @Test
    fun anSvgPatternCarriesItsTileSize() {
        val svgTile = unsafeJso<SVGPatternObject> {
            svgWidth = 16.0
            svgHeight = 24.0
        }
        assertEquals(16.0, svgTile.svgWidth)
        assertEquals(24.0, svgTile.svgHeight)

        val pattern = PatternObject(svgTile)
        val label: LabelOption = unsafeJso { backgroundColor = ZRColor(pattern) }
        assertSame<JsAny?>(pattern, label.backgroundColor)
    }

    @Test
    fun sankeyEdgesFollowTheColourOfTheirEnds() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    series =
                        arrayOf<SeriesOption>(
                            sankeySeries {
                                typedData =
                                    arrayOf(
                                        unsafeJso<SankeyNodeItemOption> { name = OptionId("a") },
                                        unsafeJso<SankeyNodeItemOption> { name = OptionId("b") },
                                    )
                                links =
                                    arrayOf(
                                        unsafeJso<SankeyEdgeItemOption> {
                                            source = StringOrDouble("a")
                                            target = StringOrDouble("b")
                                            value = OptionDataValue(4.0)
                                        },
                                    )
                                lineStyle =
                                    unsafeJso<SankeyEdgeStyleOption> {
                                        typedColor = SankeyEdgeColor("gradient")
                                        curveness = 0.5
                                    }
                            },
                        )
                },
            )

            val stored: EChartsOption = chart.getOption().unsafeCast<EChartsOption>()
            val series = stored.series!![0].unsafeCast<SankeySeriesOption>()
            // the typed view writes the inherited `color` property, which is what the renderer reads
            val style = series.lineStyle!!.unsafeCast<LineStyleOption>()
            assertEquals<JsAny?>("gradient", style.color)
            assertEquals<Double?>(0.5, series.lineStyle!!.curveness)
            val canvas = chart.renderToCanvas()
            assertTrue(canvas.width > 0 && canvas.height > 0, "the sankey chart renders")
        } finally {
            destroyChart(chart, container)
        }
    }
}

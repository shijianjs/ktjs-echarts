package echarts

import echarts.options.EChartsOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import zrender.RendererTypes
import zrender.ZRColor

/**
 * Tests for the runtime option objects and action/event payload types of [EChartsInstance].
 *
 * These are the types a caller constructs when driving the instance: the third argument of
 * `init` ([EChartsInitOpts]), the second argument of `setOption` ([SetOptionOpts]), the loading
 * effect ([LoadingOpts]), the SVG export ([RenderToSVGStringOpts]), the dispatch options
 * ([DispatchActionOpts]) and the query/payload objects ([ModelFinderObject], [Payload],
 * [SelectChangedPayload]). Each field is read back from the object the wrapper produced, and the
 * cases that reach the instance also confirm ECharts itself honours the option.
 */
class RuntimeOptsPayloadTest {
    @Test
    fun initOptsConfigureTheRenderer() {
        val opts = unsafeJso<EChartsInitOpts> {
            renderer = RendererTypes.canvas
            devicePixelRatio = 2.0
            useDirtyRect = true
            useCoarsePointer = true
            pointerSize = 44.0
            width = StringOrDouble(320.0)
            height = StringOrDouble(210.0)
            locale = StringOrLocaleOption("EN")
        }
        assertEquals(2.0, opts.devicePixelRatio)
        assertEquals(true, opts.useDirtyRect)
        assertEquals(true, opts.useCoarsePointer)
        assertEquals(44.0, opts.pointerSize)
        assertEquals("EN", opts.locale!!.unsafeCast<String>())

        val serverOpts = unsafeJso<EChartsInitOpts> { ssr = false }
        assertEquals(false, serverOpts.ssr)

        val (chart, container) = createChart(opts = opts)
        try {
            assertEquals(2.0, chart.getDevicePixelRatio())
            assertEquals(320.0, chart.getWidth())
            assertEquals(210.0, chart.getHeight())
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun setOptionOptsReplaceTheCurrentOption() {
        val (chart, container) = createChart()
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0, 3.0))
            val opts = unsafeJso<SetOptionOpts> {
                notMerge = true
                lazyUpdate = false
                silent = false
                replaceMerge = StringOrStringArray(arrayOf("series"))
            }
            assertEquals(true, opts.notMerge)
            assertEquals(false, opts.lazyUpdate)

            val replacement = unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series = arrayOf(
                    lineSeries { typedData = numbers(9.0) },
                    lineSeries { typedData = numbers(8.0) },
                )
            }
            chart.setOption(replacement, opts)

            val series = assertNotNull(chart.getOption().series)
            assertEquals(2, series.size, "notMerge replaced the single series with two")
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun loadingDispatchAndSvgOptsDriveTheInstance() {
        val loading = unsafeJso<LoadingOpts> {
            text = "loading"
            color = ZRColor("#0f0")
            textColor = ZRColor("#333")
            maskColor = ZRColor("rgba(0, 0, 0, 0.2)")
            zlevel = 10.0
            fontSize = 16.0
            showSpinner = true
            spinnerRadius = 12.0
            lineWidth = 3.0
            fontFamily = "Arial"
        }
        assertEquals("rgba(0, 0, 0, 0.2)", loading.maskColor!!.unsafeCast<String>())
        assertEquals(true, loading.showSpinner)
        assertEquals(12.0, loading.spinnerRadius)
        assertEquals("Arial", loading.fontFamily)

        val dispatchOpts = unsafeJso<DispatchActionOpts> { flush = true }
        assertEquals(true, dispatchOpts.flush)

        val (chart, container) = createChart(
            opts = unsafeJso<EChartsInitOpts> { renderer = RendererTypes.svg },
        )
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0, 3.0))
            chart.showLoading(loading)
            chart.hideLoading()
            chart.dispatchAction(
                unsafeJso<Payload> { type = "showTip"; seriesIndex = 0.0 },
                dispatchOpts,
            )
            val svg = chart.renderToSVGString(unsafeJso<RenderToSVGStringOpts> { useViewBox = true })
            assertTrue(svg.contains("viewBox"), "useViewBox switches the SVG to a viewBox")
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun payloadModelFinderAndSelectChangedFieldsRoundTrip() {
        val payload = unsafeJso<Payload> {
            type = "highlight"
            escapeConnect = true
            excludeSeriesId = StringOrStringArray(arrayOf("hidden"))
            seriesName = "sales"
            notBlur = true
            batch = arrayOf(
                unsafeJso<Payload> { type = "highlight"; seriesIndex = 0.0; dataIndex = 1.0 },
            )
        }
        assertEquals(true, payload.escapeConnect)
        assertEquals(1, payload.batch!!.size)
        assertEquals("hidden", payload.excludeSeriesId!!.unsafeCast<Array<String>>()[0])
        assertEquals("sales", payload.seriesName)
        assertEquals(true, payload.notBlur)

        val finder = unsafeJso<ModelFinderObject> {
            seriesName = "sales"
            geoName = "china"
            bmapIndex = 0.0
            bmapId = "bm"
            bmapName = "baidu"
            xAxisName = "day"
            yAxisName = "value"
            gridName = "main"
            matrixName = "cells"
        }
        assertEquals("sales", finder.seriesName)
        assertEquals("china", finder.geoName)
        assertEquals(0.0, finder.bmapIndex)
        assertEquals("bm", finder.bmapId)
        assertEquals("baidu", finder.bmapName)
        assertEquals("day", finder.xAxisName)
        assertEquals("value", finder.yAxisName)
        assertEquals("main", finder.gridName)
        assertEquals("cells", finder.matrixName)

        val selectChanged = unsafeJso<SelectChangedPayload> {
            type = "selectchanged"
            isFromClick = true
            fromActionPayload = unsafeJso<Payload> { type = "select"; seriesIndex = 0.0 }
        }
        assertEquals(true, selectChanged.isFromClick)
        assertEquals("select", selectChanged.fromActionPayload!!.type)
    }
}

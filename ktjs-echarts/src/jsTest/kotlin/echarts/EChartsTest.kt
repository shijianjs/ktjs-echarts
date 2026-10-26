package echarts

import echarts.options.BooleanOrString
import echarts.options.CalendarOption
import echarts.options.EChartsOption
import echarts.options.StringOrDouble
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.echartsOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.pieSeries
import kotlin.js.JsAny
import kotlin.js.jsTypeOf
import kotlin.js.json
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Lifecycle, echartsOption and rendering tests for the module API and [EChartsInstance].
 *
 * The upstream package has no equivalent unit-test file; these cases cover the wrapper surface
 * against a real Chromium canvas.
 */
class EChartsTest {
    @Test
    fun initCreatesAChartBoundToTheElement() {
        val (chart, container) = createChart()
        try {
            assertFalse(chart.isDisposed())
            assertEquals(container, chart.getDom())
            assertEquals(chart.id, chart.getId())
            assertTrue(chart.getWidth() > 0)
            assertTrue(chart.getHeight() > 0)
            assertTrue(chart.getDevicePixelRatio() > 0)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun initAppliesTheRequestedSize() {
        val (chart, container) = createChart()
        try {
            assertEquals(400.0, chart.getWidth())
            assertEquals(300.0, chart.getHeight())
            chart.resize(
                echartsOption<ResizeOpts> {
                    width = StringOrDouble(250.0)
                    height = StringOrDouble(150.0)
                },
            )
            assertEquals(250.0, chart.getWidth())
            assertEquals(150.0, chart.getHeight())
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun getInstanceByDomAndIdFindTheChart() {
        val (chart, container) = createChart()
        try {
            assertEquals(chart, ECharts.getInstanceByDom(container))
            assertEquals(chart, ECharts.getInstanceById(chart.id))
            assertNull(ECharts.getInstanceById("missing-id"))
        } finally {
            destroyChart(chart, container)
        }
        assertNull(ECharts.getInstanceByDom(container))
    }

    @Test
    fun disposeCanTargetAnElementOrId() {
        val (first, firstContainer) = createChart()
        val (second, secondContainer) = createChart()
        ECharts.dispose(firstContainer)
        assertTrue(first.isDisposed())
        ECharts.dispose(second.id)
        assertTrue(second.isDisposed())
        firstContainer.remove()
        secondContainer.remove()
    }

    @Test
    fun setOptionStoresTheSeriesAndAxes() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                echartsOption<EChartsOption> {
                    xAxis = one(echartsOption<XAxisOption> { type = "category"; data = numbers(1.0, 2.0) })
                    yAxis = one(echartsOption<YAxisOption> { type = "value" })
                    series = one(lineSeries { name = "s"; data = numbers(3.0, 4.0) })
                },
            )
            val stored = chart.getOption().unsafeCast<EChartsOption>()
            assertEquals(1, stored.series?.size)
            assertEquals("line", stored.series!![0].type)
            assertEquals("s", stored.series!![0].name)
            assertEquals("category", stored.xAxis!![0].type)
            assertEquals(2, stored.xAxis!![0].data?.size)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun setOptionSupportsRawObjects() {
        val (chart, container) = createChart()
        try {
            chart.setOptionRaw(
                json(
                    "series" to arrayOf<JsAny>(
                        json(
                            "type" to "pie",
                            "data" to arrayOf<JsAny>(1.0, 2.0),
                        ),
                    ),
                ),
            )
            val stored = chart.getOption().unsafeCast<EChartsOption>()
            assertEquals("pie", stored.series!![0].type)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun setOptionReplacesWhenNotMergeIsSet() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                echartsOption<EChartsOption> {
                    xAxis = one(echartsOption<XAxisOption> { type = "category"; data = numbers(1.0, 2.0) })
                    yAxis = one(echartsOption<YAxisOption> { type = "value" })
                    series = one(lineSeries { name = "a"; data = numbers(1.0, 2.0) })
                },
            )
            chart.setOption(echartsOption<EChartsOption> { series = one(pieSeries { name = "b" }) }, true)
            val stored = chart.getOption().unsafeCast<EChartsOption>()
            assertEquals(1, stored.series?.size)
            assertEquals("pie", stored.series!![0].type)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun groupConnectsAndDisconnectsCharts() {
        val (first, firstContainer) = createChart()
        val (second, secondContainer) = createChart()
        try {
            first.group = "test-group"
            second.group = "test-group"
            assertEquals("test-group", first.group)
            assertEquals("test-group", ECharts.connect("test-group"))
            ECharts.disconnect("test-group")
            ECharts.disConnect("test-group")
        } finally {
            destroyChart(first, firstContainer)
            destroyChart(second, secondContainer)
        }
    }

    @Test
    fun connectAcceptsAnArrayOfCharts() {
        val (first, firstContainer) = createChart()
        val (second, secondContainer) = createChart()
        try {
            val groupId = ECharts.connect(arrayOf(first, second))
            assertTrue(groupId.isNotEmpty())
            ECharts.disconnect(groupId)
        } finally {
            destroyChart(first, firstContainer)
            destroyChart(second, secondContainer)
        }
    }

    @Test
    fun registerThemeAppliesAColorsPalette() {
        ECharts.registerTheme(
            "wrapper-test-theme",
            echartsOption<EChartsOption> { color = arrayOf("#123456") },
        )
        val (chart, container) = createChart(theme = "wrapper-test-theme")
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0))
            assertEquals("#123456", seriesColor(chart))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun setThemeRepaintsWithTheNewPalette() {
        val (chart, container) = createChart()
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0))
            chart.setTheme(echartsOption<EChartsOption> { color = arrayOf("#654321") })
            assertEquals("#654321", seriesColor(chart))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun registerMapAndGetMapRoundTrip() {
        val geoJson = json(
            "type" to "FeatureCollection",
            "features" to arrayOf<JsAny>(
                json(
                    "type" to "Feature",
                    "properties" to json("name" to "A"),
                    "geometry" to json(
                        "type" to "Polygon",
                        "coordinates" to arrayOf(
                            arrayOf(
                                doubleArrayOf(0.0, 0.0),
                                doubleArrayOf(0.0, 1.0),
                                doubleArrayOf(1.0, 1.0),
                                doubleArrayOf(0.0, 0.0),
                            ),
                        ),
                    ),
                ),
            ),
        )
        ECharts.registerMap("wrapper-test-map", geoJson)
        assertNotNull(ECharts.getMap("wrapper-test-map"))
        assertNull(ECharts.getMap("wrapper-test-missing-map"))
    }

    @Test
    fun customActionDispatchesAndEmitsTheEvent() {
        var actionPayload: Payload? = null
        ECharts.registerAction("wrapperTestAction", "wrapperTestActionEvent") { payload ->
            actionPayload = payload
        }
        val (chart, container) = createChart()
        var eventType: String? = null
        try {
            chart.setOption(echartsOption<EChartsOption> { series = one(pieSeries { data = numbers(1.0, 2.0) }) })
            chart.on("wrapperTestActionEvent") { event -> eventType = event.type }
            chart.dispatchAction(echartsOption { type = "wrapperTestAction"; seriesIndex = 0.0 })
            assertNotNull(actionPayload)
            assertEquals("wrappertestactionevent", eventType)
            assertEquals("wrapperTestAction", actionPayload.type)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun eventHandlersCanBeRemoved() {
        ECharts.registerAction("wrapperRemovableAction", "wrapperRemovableEvent") { }
        val (chart, container) = createChart()
        var calls = 0
        try {
            chart.setOption(echartsOption<EChartsOption> { series = one(pieSeries { data = numbers(1.0, 2.0) }) })
            val handler: (ECElementEvent) -> Unit = { calls += 1 }
            chart.on("wrapperRemovableEvent", handler)
            chart.dispatchAction(echartsOption { type = "wrapperRemovableAction" })
            chart.off("wrapperRemovableEvent", handler)
            chart.dispatchAction(echartsOption { type = "wrapperRemovableAction" })
            assertEquals(1, calls)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun finishedAndRenderedEventsAreObserved() {
        val (chart, container) = createChart()
        var finished = 0
        var rendered = 0
        try {
            chart.onFinished { finished += 1 }
            chart.onRendered { rendered += 1 }
            chart.setOption(
                echartsOption<EChartsOption> {
                    animation = false
                    series = one(pieSeries { data = numbers(1.0, 2.0) })
                },
            )
            assertEquals(1, rendered)
            assertEquals(1, finished)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun selectionEmitsSelectChanged() {
        val (chart, container) = createChart()
        var selectedCount = -1
        try {
            chart.setOption(
                echartsOption<EChartsOption> {
                    series = one(
                        pieSeries {
                            selectedMode = BooleanOrString("multiple")
                            data = numbers(1.0, 2.0)
                        },
                    )
                },
            )
            chart.onSelectChanged { event -> selectedCount = event.selected.size }
            chart.dispatchAction(echartsOption { type = "select"; seriesIndex = 0.0; dataIndex = 0.0 })
            assertEquals(1, selectedCount)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun showLoadingAndHideLoadingDoNotThrow() {
        val (chart, container) = createChart()
        try {
            chart.showLoading()
            chart.showLoading("default", echartsOption<LoadingOpts> { text = "loading" })
            chart.showLoading(echartsOption<LoadingOpts> { text = "loading" })
            chart.hideLoading()
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun clearKeepsTheInstanceAlive() {
        val (chart, container) = createChart()
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0))
            chart.clear()
            assertFalse(chart.isDisposed())
            val series = chart.getOption().unsafeCast<EChartsOption>().series
            assertTrue(series == null || series.isEmpty())
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun renderToCanvasProducesACanvas() {
        val (chart, container) = createChart()
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0))
            val canvas = chart.renderToCanvas()
            assertTrue(canvas.width > 0)
            assertTrue(canvas.height > 0)
            assertNotNull(chart.getZr())
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun getDataURLReturnsAPngDataUrl() {
        val (chart, container) = createChart()
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0))
            assertTrue(chart.getDataURL().startsWith("data:image/png;base64,"))
            // Upstream reads `opts.type` without a default, so `opts` must be an object here.
            assertTrue(
                chart.getConnectedDataURL(echartsOption<GetConnectedDataURLOpts> { })
                    .startsWith("data:image/png;base64,"),
            )
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun svgRendererProducesSvgOutput() {
        val (chart, container) = createChart(opts = echartsOption { renderer = "svg" })
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0))
            val svg = chart.renderToSVGString()
            assertTrue(svg.contains("<svg"))
            assertTrue(chart.getSvgDataURL().startsWith("data:image/svg+xml"))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun convertToPixelAndBackRoundTrips() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                echartsOption<EChartsOption> {
                    xAxis = one(echartsOption<XAxisOption> { type = "value"; min = 0.0; max = 100.0 })
                    yAxis = one(echartsOption<YAxisOption> { type = "value"; min = 0.0; max = 100.0 })
                    calendar = one(echartsOption<CalendarOption> { range = "2026-10" })
                    series = one(lineSeries { data = numbers(0.0, 100.0) })
                },
            )
            val finder = ModelFinder(echartsOption<ModelFinderObject> { xAxisIndex = 0.0 })
            val pixel = chart.convertToPixel(finder, 50.0).unsafeCast<Double>()
            val value = chart.convertFromPixel(finder, pixel).unsafeCast<Double>()
            assertEquals(50.0, value, 0.5)
            // Cartesian grids do not implement `convertToLayout`; calendar does.
            assertEquals("object", jsTypeOf(chart.convertToLayout(ModelFinder("calendar"), "2026-10-15")))
            val gridFinder = ModelFinder(echartsOption<ModelFinderObject> { gridIndex = 0.0 })
            val inside = chart.convertToPixel(
                gridFinder,
                arrayOf<JsAny>(50.0, 50.0),
            ).unsafeCast<Array<Double>>()
            assertTrue(chart.containPixel(gridFinder, inside))
            assertFalse(chart.containPixel(gridFinder, arrayOf(-1000.0, -1000.0)))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun getVisualReturnsTheSeriesColor() {
        val (chart, container) = createChart()
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0) { color = arrayOf("#010203") })
            assertEquals("#010203", seriesColor(chart))
            assertTrue(jsTypeOf(chart.getVisual(ModelFinder("series"), "symbolSize")) != "undefined")
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun appendDataAndUpdateLabelLayoutAreCallable() {
        val (chart, container) = createChart()
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0))
            chart.appendData(echartsOption { seriesIndex = 0.0; data = numbers(3.0, 4.0) })
            chart.updateLabelLayout()
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun makeActionFromEventBuildsAPayload() {
        val (chart, container) = createChart()
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0))
            val payload = chart.makeActionFromEvent(
                json("type" to "highlight", "seriesIndex" to 0).unsafeCast<ECActionEvent>(),
            )
            assertEquals("highlight", payload.type)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun throttleWrapsAFunction() {
        val callback: () -> Unit = { }
        val throttled = ECharts.throttle(callback.unsafeCast<JsAny>(), 100.0)
        assertNotNull(throttled)
    }

    @Test
    fun versionAndEnvironmentAreExported() {
        assertEquals("6.1.0", ECharts.version)
        assertNotNull(ECharts.env.browser)
        assertNotNull(ECharts.dependencies.zrender)
        assertTrue(ECharts.PRIORITY.VISUAL.CHART > 0)
        assertTrue(ECharts.PRIORITY.PROCESSOR.FILTER > 0)
    }

    @Test
    fun registrationsAreCallable() {
        ECharts.registerPreprocessor { }
        ECharts.registerProcessor { }
        ECharts.registerProcessor(10.0) { }
        ECharts.registerLayout { }
        ECharts.registerLayout(10.0) { }
        ECharts.registerVisual { }
        ECharts.registerVisual(10.0) { }
        ECharts.registerLoading("wrapper-test-loading", echartsOption<RawOption> { })
        ECharts.registerPostInit { }
        ECharts.registerPostUpdate { }
        ECharts.registerUpdateLifecycle("afterinit") { }
        ECharts.registerTransform(
            json(
                "type" to "wrapper:noop",
                "transform" to ({ value: JsAny -> value }.unsafeCast<JsAny>()),
            ),
        )
        ECharts.registerLocale("wrapper-test-locale", echartsOption<RawOption> { })
        ECharts.setPlatformAPI(echartsOption<RawOption> { })
        ECharts.use(arrayOf(EChartsFeatures.LabelLayout))
        assertNotNull(ECharts.dataTool)
        assertNotNull(ECharts.matrix)
        assertNotNull(ECharts.vector)
        assertNotNull(ECharts.util)
        assertNotNull(ECharts.zrUtil)
        assertNotNull(ECharts.zrender)
    }

    @Test
    fun coreEntryPointCanRegisterInstallersAndRender() {
        EChartsCore.use(
            arrayOf(
                EChartsCharts.LineChart,
                EChartsComponents.GridComponent,
                EChartsComponents.TooltipComponent,
                EChartsRenderers.CanvasRenderer,
            ),
        )
        val container = createContainer()
        val chart = EChartsCore.init(container, null, null)
        try {
            chart.setOption(
                echartsOption<EChartsOption> {
                    xAxis = one(echartsOption<XAxisOption> { type = "category"; data = numbers(1.0, 2.0) })
                    yAxis = one(echartsOption<YAxisOption> { type = "value" })
                    series = one(lineSeries { data = numbers(1.0, 2.0) })
                },
            )
            assertTrue(chart.renderToCanvas().width > 0)
            assertNotEquals("", chart.getDataURL())
        } finally {
            chart.dispose()
            container.remove()
        }
    }

    @Test
    fun eventAndActionConstantsMatchTheUpstreamNames() {
        assertEquals("click", EChartsEvents.Click)
        assertEquals("selectchanged", EChartsEvents.SelectChanged)
        assertEquals("finished", EChartsEvents.Finished)
        assertEquals("highlight", EChartsActions.Highlight)
        assertEquals("showTip", EChartsActions.ShowTip)
    }
}

private fun seriesColor(chart: EChartsInstance): String =
    chart.getVisual(ModelFinder("series"), "color").toString()

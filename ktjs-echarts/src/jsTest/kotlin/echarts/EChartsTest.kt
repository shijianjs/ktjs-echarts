package echarts

import echarts.options.AxisMinMax
import echarts.options.SelectionModes
import echarts.options.CalendarOption
import echarts.options.CalendarRange
import echarts.options.DataZoomValue
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.StringOrDouble
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.pieSeries
import js.objects.unsafeJso
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
import web.dom.document
import web.html.HtmlTagName
import zrender.RendererTypes
import zrender.ZRColor
import zrender.ZRElement

/**
 * Lifecycle, option building and rendering tests for the module API and [EChartsInstance].
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
                unsafeJso<ResizeOpts> {
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
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = numbers(1.0, 2.0) })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = one(lineSeries { name = "s"; typedData = numbers(3.0, 4.0) })
                },
            )
            val stored = chart.getOption().unsafeCast<EChartsOption>()
            assertEquals(1, stored.series?.size)
            assertEquals(SeriesTypes.line, stored.series!![0].type)
            assertEquals("s", stored.series!![0].name)
            assertEquals(AxisTypes.category, stored.xAxis!![0].type)
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
            assertEquals(SeriesTypes.pie, stored.series!![0].type)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun setOptionReplacesWhenNotMergeIsSet() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = numbers(1.0, 2.0) })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = one(lineSeries { name = "a"; typedData = numbers(1.0, 2.0) })
                },
            )
            chart.setOption(unsafeJso<EChartsOption> { series = one(pieSeries { name = "b" }) }, true)
            val stored = chart.getOption().unsafeCast<EChartsOption>()
            assertEquals(1, stored.series?.size)
            assertEquals(SeriesTypes.pie, stored.series!![0].type)
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
            unsafeJso<EChartsOption> { color = arrayOf(ZRColor("#123456")) },
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
            chart.setTheme(unsafeJso<EChartsOption> { color = arrayOf(ZRColor("#654321")) })
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
        ECharts.registerMap(
            "wrapper-test-map",
            MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = GeoJSONSourceInput(geoJson.unsafeCast<GeoJSON>()) }),
        )
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
            chart.setOption(unsafeJso<EChartsOption> { series = one(pieSeries { typedData = numbers(1.0, 2.0) }) })
            chart.on("wrapperTestActionEvent") { event -> eventType = event.type }
            chart.dispatchAction(unsafeJso { type = "wrapperTestAction"; seriesIndex = 0.0 })
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
            chart.setOption(unsafeJso<EChartsOption> { series = one(pieSeries { typedData = numbers(1.0, 2.0) }) })
            val handler: (ECElementEvent) -> Unit = { calls += 1 }
            chart.on("wrapperRemovableEvent", handler)
            chart.dispatchAction(unsafeJso { type = "wrapperRemovableAction" })
            chart.off("wrapperRemovableEvent", handler)
            chart.dispatchAction(unsafeJso { type = "wrapperRemovableAction" })
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
                unsafeJso<EChartsOption> {
                    animation = false
                    series = one(pieSeries { typedData = numbers(1.0, 2.0) })
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
                unsafeJso<EChartsOption> {
                    series = one(
                        pieSeries {
                            selectedMode = SelectionModes.multiple
                            typedData = numbers(1.0, 2.0)
                        },
                    )
                },
            )
            chart.onSelectChanged { event -> selectedCount = event.selected.size }
            chart.dispatchAction(unsafeJso { type = "select"; seriesIndex = 0.0; dataIndex = 0.0 })
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
            chart.showLoading("default", unsafeJso<LoadingOpts> { text = "loading" })
            chart.showLoading(unsafeJso<LoadingOpts> { text = "loading" })
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
    fun configLayerAcceptsMotionBlur() {
        val (chart, container) = createChart()
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0))
            val zr = assertNotNull(chart.getZr())
            zr.configLayer(1.0, unsafeJso { motionBlur = true; lastFrameAlpha = 0.5; clearColor = ZRColor("#ffffff") })
            zr.refresh()
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
                chart.getConnectedDataURL(unsafeJso<GetConnectedDataURLOpts> { })
                    .startsWith("data:image/png;base64,"),
            )
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun svgRendererProducesSvgOutput() {
        val (chart, container) = createChart(opts = unsafeJso { renderer = RendererTypes.svg })
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
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value; min = AxisMinMax(0.0); max = AxisMinMax(100.0) })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value; min = AxisMinMax(0.0); max = AxisMinMax(100.0) })
                    calendar = one(unsafeJso<CalendarOption> { range = CalendarRange(DataZoomValue("2026-10")) })
                    series = one(lineSeries { typedData = numbers(0.0, 100.0) })
                },
            )
            val finder = ModelFinder(unsafeJso<ModelFinderObject> { xAxisIndex = 0.0 })
            val pixel = chart.convertToPixel(finder, OptionDataValueOrArray(50.0)).unsafeCast<Double>()
            val value = chart.convertFromPixel(finder, DoubleOrDoubleArray(pixel)).unsafeCast<Double>()
            assertEquals(50.0, value, 0.5)
            // Cartesian grids do not implement `convertToLayout`; calendar does.
            assertEquals("object", jsTypeOf(chart.convertToLayout(ModelFinder("calendar"), "2026-10-15")))
            val gridFinder = ModelFinder(unsafeJso<ModelFinderObject> { gridIndex = 0.0 })
            val inside = chart.convertToPixel(
                gridFinder,
                OptionDataValueOrArray(arrayOf(OptionDataValue(50.0), OptionDataValue(50.0))),
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
            chart.setOption(cartesianLineOption(1.0, 2.0) { color = arrayOf(ZRColor("#010203")) })
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
            chart.appendData(unsafeJso { seriesIndex = 0.0; data = numbers(3.0, 4.0) })
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
            assertEquals(EChartsActions.Highlight, payload.type)
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
        ECharts.registerPreprocessor { _, _ -> }
        ECharts.registerProcessor { _, _, _ -> }
        ECharts.registerProcessor(10.0) { _, _, _ -> }
        ECharts.registerLayout { _, _, _ -> }
        ECharts.registerLayout(10.0) { _, _, _ -> }
        ECharts.registerVisual { _, _, _ -> }
        ECharts.registerVisual(10.0) { _, _, _ -> }
        ECharts.registerLoading("wrapper-test-loading") { _, _ ->
            // Registration does not inspect the element; only showing the effect would.
            document.createElement(HtmlTagName.div).unsafeCast<ZRElement>()
        }
        ECharts.registerPostInit { }
        ECharts.registerPostUpdate { _, _ -> }
        ECharts.registerUpdateLifecycle("afterinit") { }
        ECharts.registerTransform(
            unsafeJso<ExternalTransform> {
                type = "wrapper:noop"
                transform = { param -> arrayOf(unsafeJso { data = param.upstream.getRawData() }) }
            },
        )
        ECharts.registerLocale("wrapper-test-locale", unsafeJso<LocaleOption> { })
        ECharts.setPlatformAPI(unsafeJso<PlatformAPI> { })
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
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = numbers(1.0, 2.0) })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = one(lineSeries { typedData = numbers(1.0, 2.0) })
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

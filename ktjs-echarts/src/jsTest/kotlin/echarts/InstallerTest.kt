package echarts

import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.jsTypeOf
import kotlin.js.json
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import zrender.RendererTypes

/**
 * Tests for the modular entry point, the installer exports and the extension classes.
 *
 * The installers are exercised through `EChartsCore.use`, which throws when a value is not a valid
 * ECharts extension, so installing every one of them verifies the whole list at once.
 */
class InstallerTest {
    @Test
    fun chartsComponentsRenderersAndFeaturesCanBeInstalled() {
        EChartsCore.use(arrayOf(
            EChartsCharts.BarChart,
            EChartsCharts.BoxplotChart,
            EChartsCharts.CandlestickChart,
            EChartsCharts.ChordChart,
            EChartsCharts.CustomChart,
            EChartsCharts.EffectScatterChart,
            EChartsCharts.FunnelChart,
            EChartsCharts.GaugeChart,
            EChartsCharts.GraphChart,
            EChartsCharts.HeatmapChart,
            EChartsCharts.LineChart,
            EChartsCharts.LinesChart,
            EChartsCharts.MapChart,
            EChartsCharts.ParallelChart,
            EChartsCharts.PictorialBarChart,
            EChartsCharts.PieChart,
            EChartsCharts.RadarChart,
            EChartsCharts.SankeyChart,
            EChartsCharts.ScatterChart,
            EChartsCharts.SunburstChart,
            EChartsCharts.ThemeRiverChart,
            EChartsCharts.TreeChart,
            EChartsCharts.TreemapChart,
        ))
        EChartsCore.use(arrayOf(
            EChartsComponents.AriaComponent,
            EChartsComponents.AxisPointerComponent,
            EChartsComponents.BrushComponent,
            EChartsComponents.CalendarComponent,
            EChartsComponents.DataZoomComponent,
            EChartsComponents.DataZoomInsideComponent,
            EChartsComponents.DataZoomSliderComponent,
            EChartsComponents.DatasetComponent,
            EChartsComponents.GeoComponent,
            EChartsComponents.GraphicComponent,
            EChartsComponents.GridComponent,
            EChartsComponents.GridSimpleComponent,
            EChartsComponents.LegendComponent,
            EChartsComponents.LegendPlainComponent,
            EChartsComponents.LegendScrollComponent,
            EChartsComponents.MarkAreaComponent,
            EChartsComponents.MarkLineComponent,
            EChartsComponents.MarkPointComponent,
            EChartsComponents.MatrixComponent,
            EChartsComponents.ParallelComponent,
            EChartsComponents.PolarComponent,
            EChartsComponents.RadarComponent,
            EChartsComponents.SingleAxisComponent,
            EChartsComponents.ThumbnailComponent,
            EChartsComponents.TimelineComponent,
            EChartsComponents.TitleComponent,
            EChartsComponents.ToolboxComponent,
            EChartsComponents.TooltipComponent,
            EChartsComponents.TransformComponent,
            EChartsComponents.VisualMapComponent,
            EChartsComponents.VisualMapContinuousComponent,
            EChartsComponents.VisualMapPiecewiseComponent,
        ))
        EChartsCore.use(arrayOf(EChartsRenderers.CanvasRenderer, EChartsRenderers.SVGRenderer))
        EChartsCore.use(arrayOf(
            EChartsFeatures.AxisBreak,
            EChartsFeatures.LabelLayout,
            EChartsFeatures.LegacyGridContainLabel,
            EChartsFeatures.ScatterJitter,
            EChartsFeatures.UniversalTransition,
        ))

        val (chart, container) = createChart()
        try {
            chart.setOption(cartesianLineOption(1.0, 2.0, 3.0))
            assertEquals(1, chart.getOption().unsafeCast<OptionWithSeries>().series.size)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun installedSvgRendererCanBeSelected() {
        EChartsCore.use(EChartsCharts.LineChart)
        EChartsCore.use(EChartsComponents.GridComponent)
        EChartsCore.use(EChartsRenderers.SVGRenderer)
        val (chart, container) = createChart(
            opts = unsafeJso<EChartsInitOpts> { renderer = RendererTypes.svg },
        )
        try {
            chart.setOption(cartesianLineOption(1.0))
            assertTrue(chart.renderToSVGString().contains("<svg"))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun parseGeoJsonIsAnAliasOfParseGeoJSON() {
        val geoJson = json(
            "type" to "FeatureCollection",
            "features" to arrayOf(
                json(
                    "type" to "Feature",
                    "properties" to json("name" to "RegionA"),
                    "geometry" to json(
                        "type" to "Polygon",
                        "coordinates" to arrayOf(
                            arrayOf(
                                arrayOf(0.0, 0.0),
                                arrayOf(1.0, 0.0),
                                arrayOf(1.0, 1.0),
                                arrayOf(0.0, 0.0),
                            ),
                        ),
                    ),
                ),
            ),
        )

        val upperCase = ECharts.parseGeoJSON(geoJson.unsafeCast<GeoJSON>(), "name")
        val lowerCase = ECharts.parseGeoJson(geoJson.unsafeCast<GeoJSON>(), "name")
        assertEquals(upperCase.size, lowerCase.size)
        assertEquals(1, upperCase.size)
    }

    @Test
    fun extensionHelpersCreateModelAndViewClasses() {
        assertEquals("function", jsTypeOf(extendComponentModel(json())))
        assertEquals("function", jsTypeOf(extendComponentView(json())))
        assertEquals("function", jsTypeOf(extendChartView(json())))

        // The returned constructor is an extension in its own right, so `use` accepts it directly.
        val seriesModel = extendSeriesModel(json("type" to "wrapperTestSeries"))
        assertEquals("function", jsTypeOf(seriesModel))
        // A class is a JavaScript function, which is what `use` accepts at runtime.
        EChartsCore.use(seriesModel.unsafeCast<EChartsExtensionInstaller>())
    }

    @Test
    fun modelsExposeOptionValues() {
        val model = Model(json("title" to json("text" to "hello")))
        assertEquals("hello", model.getModel("title").get("text").unsafeCast<String>())
        assertTrue(!model.isEmpty())
        assertEquals("object", jsTypeOf(model.clone()))
    }

    @Test
    fun seriesDataExposesRows() {
        val model = Model(json())
        val data = SeriesData(arrayOf("x", "y"), model)
        data.initData(
            arrayOf(
                arrayOf(1.0.unsafeCast<JsAny>(), 2.0.unsafeCast<JsAny>()),
                arrayOf(3.0.unsafeCast<JsAny>(), 4.0.unsafeCast<JsAny>()),
            ).unsafeCast<JsAny>(),
            arrayOf("a", "b"),
        )

        assertEquals(2, data.count())
        assertEquals(1.0, data.get("x", 0).unsafeCast<Double>())
        assertEquals(4.0, data.get("y", 1).unsafeCast<Double>())
        assertEquals("b", data.getName(1))
        assertEquals(arrayOf("x", "y").toList(), data.dimensions.toList())
        assertEquals(1, data.getDimensionIndex("y"))
        assertEquals(4.0, data.getSum("x"))
    }
}

private external interface OptionWithSeries : JsAny {
    val series: Array<JsAny>
}

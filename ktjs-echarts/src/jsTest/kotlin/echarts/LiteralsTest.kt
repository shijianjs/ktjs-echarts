package echarts

import echarts.options.BooleanOrString
import echarts.options.EChartsOption
import echarts.options.GaugeSeriesOption
import echarts.options.LineSeriesOption
import echarts.options.PieSeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.SymbolOffset
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.gaugeSeries
import echarts.options.lineSeries
import echarts.options.pieSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import zrender.RendererTypes
import zrender.TextOverflows
import zrender.ZRFontWeight
import zrender.ZRFontWeights
import zrender.ZRLineType
import zrender.ZRLineTypes

/**
 * Tests for the string-literal unions.
 *
 * The constants must compile to the bare strings that the upstream TypeScript definitions use, so
 * every case compares the constant with the literal it replaces. The last cases show that the
 * typed fields accept the constants and that a chart still receives the literal values.
 */
class LiteralsTest {
    @Test
    fun axisTypesMatchTheUpstreamNames() {
        assertEquals("value", AxisTypes.value.unsafeCast<String>())
        assertEquals("category", AxisTypes.category.unsafeCast<String>())
        assertEquals("time", AxisTypes.time.unsafeCast<String>())
        assertEquals("log", AxisTypes.log.unsafeCast<String>())
    }

    @Test
    fun componentLiteralsMatchTheUpstreamNames() {
        assertEquals("horizontal", Orients.horizontal.unsafeCast<String>())
        assertEquals("vertical", Orients.vertical.unsafeCast<String>())
        assertEquals("item", TooltipTriggers.item.unsafeCast<String>())
        assertEquals("axis", TooltipTriggers.axis.unsafeCast<String>())
        assertEquals("none", TooltipTriggers.none.unsafeCast<String>())
        assertEquals("plain", LegendTypes.plain.unsafeCast<String>())
        assertEquals("scroll", LegendTypes.scroll.unsafeCast<String>())
        assertEquals("inside", DataZoomTypes.inside.unsafeCast<String>())
        assertEquals("slider", DataZoomTypes.slider.unsafeCast<String>())
        assertEquals("continuous", VisualMapTypes.continuous.unsafeCast<String>())
        assertEquals("piecewise", VisualMapTypes.piecewise.unsafeCast<String>())
        assertEquals("filter", FilterModes.filter.unsafeCast<String>())
        assertEquals("weakFilter", FilterModes.weakFilter.unsafeCast<String>())
        assertEquals("empty", FilterModes.empty.unsafeCast<String>())
        assertEquals("none", FilterModes.none.unsafeCast<String>())
        assertEquals("none", EmphasisFocuses.none.unsafeCast<String>())
        assertEquals("self", EmphasisFocuses.self.unsafeCast<String>())
        assertEquals("series", EmphasisFocuses.series.unsafeCast<String>())
        assertEquals("coordinateSystem", BlurScopes.coordinateSystem.unsafeCast<String>())
        assertEquals("series", BlurScopes.series.unsafeCast<String>())
        assertEquals("global", BlurScopes.global.unsafeCast<String>())
        assertEquals("line", AxisPointerTypes.line.unsafeCast<String>())
        assertEquals("shadow", AxisPointerTypes.shadow.unsafeCast<String>())
        assertEquals("cross", AxisPointerTypes.cross.unsafeCast<String>())
        assertEquals("none", AxisPointerTypes.none.unsafeCast<String>())
        assertEquals("radius", RoseTypes.radius.unsafeCast<String>())
        assertEquals("area", RoseTypes.area.unsafeCast<String>())
        assertEquals("png", ImageTypes.png.unsafeCast<String>())
        assertEquals("jpeg", ImageTypes.jpeg.unsafeCast<String>())
        assertEquals("svg", ImageTypes.svg.unsafeCast<String>())
    }

    @Test
    fun everySeriesTypeMatchesTheUpstreamName() {
        val seriesTypes = arrayOf(
            Pair("line", SeriesTypes.line),
            Pair("bar", SeriesTypes.bar),
            Pair("pie", SeriesTypes.pie),
            Pair("scatter", SeriesTypes.scatter),
            Pair("effectScatter", SeriesTypes.effectScatter),
            Pair("radar", SeriesTypes.radar),
            Pair("map", SeriesTypes.map),
            Pair("tree", SeriesTypes.tree),
            Pair("treemap", SeriesTypes.treemap),
            Pair("sunburst", SeriesTypes.sunburst),
            Pair("graph", SeriesTypes.graph),
            Pair("chord", SeriesTypes.chord),
            Pair("funnel", SeriesTypes.funnel),
            Pair("gauge", SeriesTypes.gauge),
            Pair("pictorialBar", SeriesTypes.pictorialBar),
            Pair("themeRiver", SeriesTypes.themeRiver),
            Pair("custom", SeriesTypes.custom),
            Pair("boxplot", SeriesTypes.boxplot),
            Pair("candlestick", SeriesTypes.candlestick),
            Pair("heatmap", SeriesTypes.heatmap),
            Pair("parallel", SeriesTypes.parallel),
            Pair("lines", SeriesTypes.lines),
            Pair("sankey", SeriesTypes.sankey),
        )
        seriesTypes.forEach { (name, value) -> assertEquals(name, value.unsafeCast<String>()) }
    }

    @Test
    fun everyBuiltInAnimationEasingMatchesTheUpstreamName() {
        val easings = arrayOf(
            Pair("linear", AnimationEasings.linear),
            Pair("quadraticIn", AnimationEasings.quadraticIn),
            Pair("quadraticOut", AnimationEasings.quadraticOut),
            Pair("quadraticInOut", AnimationEasings.quadraticInOut),
            Pair("cubicIn", AnimationEasings.cubicIn),
            Pair("cubicOut", AnimationEasings.cubicOut),
            Pair("cubicInOut", AnimationEasings.cubicInOut),
            Pair("quarticIn", AnimationEasings.quarticIn),
            Pair("quarticOut", AnimationEasings.quarticOut),
            Pair("quarticInOut", AnimationEasings.quarticInOut),
            Pair("quinticIn", AnimationEasings.quinticIn),
            Pair("quinticOut", AnimationEasings.quinticOut),
            Pair("quinticInOut", AnimationEasings.quinticInOut),
            Pair("sinusoidalIn", AnimationEasings.sinusoidalIn),
            Pair("sinusoidalOut", AnimationEasings.sinusoidalOut),
            Pair("sinusoidalInOut", AnimationEasings.sinusoidalInOut),
            Pair("exponentialIn", AnimationEasings.exponentialIn),
            Pair("exponentialOut", AnimationEasings.exponentialOut),
            Pair("exponentialInOut", AnimationEasings.exponentialInOut),
            Pair("circularIn", AnimationEasings.circularIn),
            Pair("circularOut", AnimationEasings.circularOut),
            Pair("circularInOut", AnimationEasings.circularInOut),
            Pair("elasticIn", AnimationEasings.elasticIn),
            Pair("elasticOut", AnimationEasings.elasticOut),
            Pair("elasticInOut", AnimationEasings.elasticInOut),
            Pair("backIn", AnimationEasings.backIn),
            Pair("backOut", AnimationEasings.backOut),
            Pair("backInOut", AnimationEasings.backInOut),
            Pair("bounceIn", AnimationEasings.bounceIn),
            Pair("bounceOut", AnimationEasings.bounceOut),
            Pair("bounceInOut", AnimationEasings.bounceInOut),
        )
        easings.forEach { (name, value) -> assertEquals(name, value.unsafeCast<String>()) }
    }

    @Test
    fun zrenderLiteralsMatchTheUpstreamNames() {
        assertEquals("canvas", RendererTypes.canvas.unsafeCast<String>())
        assertEquals("svg", RendererTypes.svg.unsafeCast<String>())
        assertEquals("truncate", TextOverflows.truncate.unsafeCast<String>())
        assertEquals("break", TextOverflows.`break`.unsafeCast<String>())
        assertEquals("breakAll", TextOverflows.breakAll.unsafeCast<String>())
        assertEquals("none", TextOverflows.none.unsafeCast<String>())
        assertEquals("solid", ZRLineTypes.solid.unsafeCast<String>())
        assertEquals("dashed", ZRLineTypes.dashed.unsafeCast<String>())
        assertEquals("dotted", ZRLineTypes.dotted.unsafeCast<String>())
        assertEquals("normal", ZRFontWeights.normal.unsafeCast<String>())
        assertEquals("bold", ZRFontWeights.bold.unsafeCast<String>())
        assertEquals("bolder", ZRFontWeights.bolder.unsafeCast<String>())
        assertEquals("lighter", ZRFontWeights.lighter.unsafeCast<String>())
    }

    @Test
    fun literalFactoriesRoundTripJavaScriptValues() {
        assertEquals("category", AxisType("category").unsafeCast<String>())
        assertEquals(700.0, ZRFontWeight(700.0).unsafeCast<Double>())
        assertEquals("solid", ZRLineType("solid").unsafeCast<String>())
    }

    @Test
    fun typedSeriesFieldsKeepTheirValues() {
        val option = unsafeJso<EChartsOption> {
            xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings("a", "b") })
            yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
            series = arrayOf(
                pieSeries {
                    radius = StringOrDoubleOrArray("55%")
                    roseType = RoseTypes.radius
                },
                gaugeSeries { radius = StringOrDouble("80%") },
                lineSeries {
                    step = BooleanOrString("middle")
                    symbolOffset = SymbolOffset(4.0)
                },
            )
        }

        val (chart, container) = createChart()
        try {
            chart.setOption(option)
            val series = chart.getOption().series!!
            val pie = series[0].unsafeCast<PieSeriesOption>()
            val gauge = series[1].unsafeCast<GaugeSeriesOption>()
            val line = series[2].unsafeCast<LineSeriesOption>()

            assertEquals("55%", pie.radius!!.unsafeCast<String>())
            assertEquals(RoseTypes.radius, pie.roseType)
            assertEquals("80%", gauge.radius!!.unsafeCast<String>())
            assertEquals("middle", line.step!!.unsafeCast<String>())
            assertEquals(4.0, line.symbolOffset!!.unsafeCast<Double>())
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun typedOptionFieldsAcceptTheConstantsAndKeepTheirValue() {
        val option = unsafeJso<EChartsOption> {
            animation = false
            animationEasing = AnimationEasings.cubicOut
            xAxis = one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    data = strings("a", "b")
                },
            )
            yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
            series = one(lineSeries { data = numbers(1.0, 2.0) })
        }

        val (chart, container) = createChart()
        try {
            chart.setOption(option)
            val stored = chart.getOption()
            assertEquals(AxisTypes.category, stored.xAxis!![0].type)
            assertEquals(AxisTypes.value, stored.yAxis!![0].type)
            assertEquals(SeriesTypes.line, stored.series!![0].type)
            assertEquals("cubicOut", stored.animationEasing!!.unsafeCast<String>())
            assertTrue(stored.xAxis!![0].data?.size == 2)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun markerLiteralsMatchTheUpstreamNames() {
        assertEquals("average", MarkerStatisticTypes.average.unsafeCast<String>())
        assertEquals("min", MarkerStatisticTypes.min.unsafeCast<String>())
        assertEquals("max", MarkerStatisticTypes.max.unsafeCast<String>())
        assertEquals("median", MarkerStatisticTypes.median.unsafeCast<String>())
        assertEquals("container", MarkerRelativeTos.container.unsafeCast<String>())
        assertEquals("coordinate", MarkerRelativeTos.coordinate.unsafeCast<String>())
    }

    @Test
    fun hierarchyLiteralsMatchTheUpstreamNames() {
        assertEquals("asc", SortOrders.asc.unsafeCast<String>())
        assertEquals("desc", SortOrders.desc.unsafeCast<String>())
        assertEquals("value", ColorMappingBys.`value`.unsafeCast<String>())
        assertEquals("index", ColorMappingBys.index.unsafeCast<String>())
        assertEquals("id", ColorMappingBys.id.unsafeCast<String>())
        assertEquals("origin", ClipWindows.origin.unsafeCast<String>())
        assertEquals("fullscreen", ClipWindows.fullscreen.unsafeCast<String>())
        assertEquals("zoomToNode", NodeClickBehaviors.zoomToNode.unsafeCast<String>())
        assertEquals("rootToNode", NodeClickBehaviors.rootToNode.unsafeCast<String>())
        assertEquals("link", NodeClickBehaviors.link.unsafeCast<String>())
        assertFalse(NodeClickBehaviors.none.unsafeCast<Boolean>())
        assertEquals("expansion", AnimationTypes.expansion.unsafeCast<String>())
        assertEquals("scale", AnimationTypes.scale.unsafeCast<String>())
    }

    @Test
    fun relationLiteralsMatchTheUpstreamNames() {
        assertEquals("justify", NodeAligns.justify.unsafeCast<String>())
        assertEquals("left", NodeAligns.left.unsafeCast<String>())
        assertEquals("right", NodeAligns.right.unsafeCast<String>())
        assertEquals("inEdges", FocusNodeAdjacencies.inEdges.unsafeCast<String>())
        assertEquals("outEdges", FocusNodeAdjacencies.outEdges.unsafeCast<String>())
        assertEquals("allEdges", FocusNodeAdjacencies.allEdges.unsafeCast<String>())
        assertTrue(FocusNodeAdjacency(true).unsafeCast<Boolean>())
    }

    @Test
    fun labelLayoutLiteralsMatchTheUpstreamNames() {
        assertEquals("shiftX", MoveOverlaps.shiftX.unsafeCast<String>())
        assertEquals("shiftY", MoveOverlaps.shiftY.unsafeCast<String>())
        assertEquals("shuffleX", MoveOverlaps.shuffleX.unsafeCast<String>())
        assertEquals("shuffleY", MoveOverlaps.shuffleY.unsafeCast<String>())
    }

    @Test
    fun seriesShapeAndLayoutLiteralsMatchTheUpstreamNames() {
        assertEquals("column", SeriesLayoutBys.column.unsafeCast<String>())
        assertEquals("row", SeriesLayoutBys.row.unsafeCast<String>())
        assertEquals("polygon", RadarShapes.polygon.unsafeCast<String>())
        assertEquals("circle", RadarShapes.circle.unsafeCast<String>())
        assertEquals("ascending", FunnelSorts.ascending.unsafeCast<String>())
        assertEquals("descending", FunnelSorts.descending.unsafeCast<String>())
        assertEquals("none", FunnelSorts.none.unsafeCast<String>())
        assertEquals("left", FunnelAligns.left.unsafeCast<String>())
        assertEquals("center", FunnelAligns.center.unsafeCast<String>())
        assertEquals("right", FunnelAligns.right.unsafeCast<String>())
        assertEquals("top", FunnelAligns.top.unsafeCast<String>())
        assertEquals("middle", FunnelAligns.middle.unsafeCast<String>())
        assertEquals("bottom", FunnelAligns.bottom.unsafeCast<String>())
        assertEquals("none", GraphLayouts.none.unsafeCast<String>())
        assertEquals("force", GraphLayouts.force.unsafeCast<String>())
        assertEquals("circular", GraphLayouts.circular.unsafeCast<String>())
        assertEquals("circular", GraphInitLayouts.circular.unsafeCast<String>())
        assertEquals("none", GraphInitLayouts.none.unsafeCast<String>())
        assertEquals("orthogonal", TreeLayouts.orthogonal.unsafeCast<String>())
        assertEquals("radial", TreeLayouts.radial.unsafeCast<String>())
        assertEquals("polyline", TreeEdgeShapes.polyline.unsafeCast<String>())
        assertEquals("curve", TreeEdgeShapes.curve.unsafeCast<String>())
    }

    @Test
    fun pictorialBarLiteralsMatchTheUpstreamNames() {
        assertEquals("start", PictorialBarSymbolPositions.start.unsafeCast<String>())
        assertEquals("end", PictorialBarSymbolPositions.end.unsafeCast<String>())
        assertEquals("center", PictorialBarSymbolPositions.center.unsafeCast<String>())
        assertEquals("start", PictorialBarSymbolRepeatDirections.start.unsafeCast<String>())
        assertEquals("end", PictorialBarSymbolRepeatDirections.end.unsafeCast<String>())
    }

    @Test
    fun componentKeywordLiteralsMatchTheUpstreamNames() {
        assertEquals("mousemove", TooltipTriggerOns.mouseMove.unsafeCast<String>())
        assertEquals("click", TooltipTriggerOns.click.unsafeCast<String>())
        assertEquals("none", TooltipTriggerOns.none.unsafeCast<String>())
        assertEquals("mousewheel", TooltipTriggerOns.mouseWheel.unsafeCast<String>())
        assertEquals("mousemove|click|mousewheel", TooltipTriggerOns.mouseMoveClickWheel.unsafeCast<String>())
        assertEquals("top", AxisPositions.top.unsafeCast<String>())
        assertEquals("bottom", AxisPositions.bottom.unsafeCast<String>())
        assertEquals("left", AxisPositions.left.unsafeCast<String>())
        assertEquals("right", AxisPositions.right.unsafeCast<String>())
    }
}

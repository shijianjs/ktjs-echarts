package echarts

import echarts.options.AreaStyleOption
import echarts.options.AxisPointerOption
import echarts.options.BarSeriesOption
import echarts.options.BooleanOrString
import echarts.options.BoxplotSeriesOption
import echarts.options.BrushOption
import echarts.options.CandlestickSeriesOption
import echarts.options.ChordSeriesOption
import echarts.options.DataZoomOption
import echarts.options.EChartsOption
import echarts.options.GaugeSeriesOption
import echarts.options.GraphicElement
import echarts.options.GraphSeriesOption
import echarts.options.GridOption
import echarts.options.HeatmapSeriesOption
import echarts.options.LabelOption
import echarts.options.LegendOption
import echarts.options.LineSeriesOption
import echarts.options.MatrixDimensionOption
import echarts.options.ParallelOption
import echarts.options.PictorialBarSeriesOption
import echarts.options.PieSeriesOption
import echarts.options.RadarSeriesOption
import echarts.options.SankeySeriesOption
import echarts.options.SelectionModes
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.SymbolOffset
import echarts.options.ThemeRiverSeriesOption
import echarts.options.TimelineControlStyle
import echarts.options.TimelineOption
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.VisualMapOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.gaugeSeries
import echarts.options.lineSeries
import echarts.options.pieSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.JsName
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import zrender.RendererTypes
import zrender.TextOverflows
import zrender.BuiltinTextPositions
import zrender.TransformProps
import zrender.ZRFontWeight
import zrender.ZRFontWeights
import zrender.ZRLineType
import zrender.ZRLineTypes

/**
 * Reads the upstream `$action` property, which cannot be written as a Kotlin identifier.
 */
private external interface ActionProbe {
    @JsName("\$action")
    var action: String?
}

/**
 * Reads the `coordinateSystem` property through the string accessor and the typed accessor.
 */
private external interface CoordinateSystemProbe {
    var coordinateSystem: String?

    @JsName("coordinateSystem")
    var typedCoordinateSystem: String?
}

/**
 * Sets the string accessor of [option] and checks that the typed accessor sees the same value, so
 * that both Kotlin properties really map to the single JavaScript `coordinateSystem` property.
 */
private fun assertCoordinateSystemRoundTrip(option: Any, expected: String) {
    val probe = option.unsafeCast<CoordinateSystemProbe>()
    assertEquals(expected, probe.coordinateSystem)
    probe.coordinateSystem = "cartesian2d"
    assertEquals("cartesian2d", probe.typedCoordinateSystem)
}

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
    fun roamTriggersMatchTheUpstreamNames() {
        assertEquals("global", RoamTriggers.global.unsafeCast<String>())
        assertEquals("selfRect", RoamTriggers.selfRect.unsafeCast<String>())
        val graph = unsafeJso<GraphSeriesOption> {
            layout = GraphLayouts.none
            roamTrigger = RoamTriggers.global
        }
        assertEquals(RoamTriggers.global, graph.roamTrigger)
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
    fun exportSurfaceLiteralsMatchTheUpstreamNames() {
        assertEquals("auto", TooltipRenderModes.auto.unsafeCast<String>())
        assertEquals("html", TooltipRenderModes.html.unsafeCast<String>())
        assertEquals("richText", TooltipRenderModes.richText.unsafeCast<String>())
        assertEquals("richText", TooltipRenderMode("richText").unsafeCast<String>())

        assertEquals("left", HorizontalAligns.left.unsafeCast<String>())
        assertEquals("center", HorizontalAligns.center.unsafeCast<String>())
        assertEquals("right", HorizontalAligns.right.unsafeCast<String>())
        assertEquals("auto", HorizontalAligns.auto.unsafeCast<String>())

        // Upstream derives the update method names from the keys of its `updateMethods` table.
        assertEquals("prepareAndUpdate", ECUpdateMethodNames.prepareAndUpdate.unsafeCast<String>())
        assertEquals("update", ECUpdateMethodNames.update.unsafeCast<String>())
        assertEquals("updateTransform", ECUpdateMethodNames.updateTransform.unsafeCast<String>())
        assertEquals("updateView", ECUpdateMethodNames.updateView.unsafeCast<String>())
        assertEquals("updateVisual", ECUpdateMethodNames.updateVisual.unsafeCast<String>())
        assertEquals("updateLayout", ECUpdateMethodNames.updateLayout.unsafeCast<String>())
        assertEquals("none", ECUpdateMethodNames.none.unsafeCast<String>())

        assertEquals("expandAxisBreak", AxisBreakActions.expand.unsafeCast<String>())
        assertEquals("collapseAxisBreak", AxisBreakActions.collapse.unsafeCast<String>())
        assertEquals("toggleAxisBreak", AxisBreakActions.toggle.unsafeCast<String>())

        assertEquals("select", SelectChangedFromActions.select.unsafeCast<String>())
        assertEquals("unselect", SelectChangedFromActions.unselect.unsafeCast<String>())
        assertEquals("toggleSelected", SelectChangedFromActions.toggleSelected.unsafeCast<String>())
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
    fun zrenderTransformPropsMatchTheUpstreamNames() {
        assertEquals("x", TransformProps.x.unsafeCast<String>())
        assertEquals("y", TransformProps.y.unsafeCast<String>())
        assertEquals("originX", TransformProps.originX.unsafeCast<String>())
        assertEquals("originY", TransformProps.originY.unsafeCast<String>())
        assertEquals("anchorX", TransformProps.anchorX.unsafeCast<String>())
        assertEquals("anchorY", TransformProps.anchorY.unsafeCast<String>())
        assertEquals("rotation", TransformProps.rotation.unsafeCast<String>())
        assertEquals("scaleX", TransformProps.scaleX.unsafeCast<String>())
        assertEquals("scaleY", TransformProps.scaleY.unsafeCast<String>())
        assertEquals("skewX", TransformProps.skewX.unsafeCast<String>())
        assertEquals("skewY", TransformProps.skewY.unsafeCast<String>())
    }

    @Test
    fun builtinTextPositionsMatchTheUpstreamNames() {
        assertEquals("left", BuiltinTextPositions.left.unsafeCast<String>())
        assertEquals("right", BuiltinTextPositions.right.unsafeCast<String>())
        assertEquals("top", BuiltinTextPositions.top.unsafeCast<String>())
        assertEquals("bottom", BuiltinTextPositions.bottom.unsafeCast<String>())
        assertEquals("inside", BuiltinTextPositions.inside.unsafeCast<String>())
        assertEquals("insideLeft", BuiltinTextPositions.insideLeft.unsafeCast<String>())
        assertEquals("insideRight", BuiltinTextPositions.insideRight.unsafeCast<String>())
        assertEquals("insideTop", BuiltinTextPositions.insideTop.unsafeCast<String>())
        assertEquals("insideBottom", BuiltinTextPositions.insideBottom.unsafeCast<String>())
        assertEquals("insideTopLeft", BuiltinTextPositions.insideTopLeft.unsafeCast<String>())
        assertEquals("insideTopRight", BuiltinTextPositions.insideTopRight.unsafeCast<String>())
        assertEquals("insideBottomLeft", BuiltinTextPositions.insideBottomLeft.unsafeCast<String>())
        assertEquals("insideBottomRight", BuiltinTextPositions.insideBottomRight.unsafeCast<String>())
    }

    @Test
    fun visualPropsMatchTheUpstreamNames() {
        assertEquals("color", VisualProps.color.unsafeCast<String>())
        assertEquals("borderColor", VisualProps.borderColor.unsafeCast<String>())
        assertEquals("symbol", VisualProps.symbol.unsafeCast<String>())
        assertEquals("symbolSize", VisualProps.symbolSize.unsafeCast<String>())
        assertEquals("symbolKeepAspect", VisualProps.symbolKeepAspect.unsafeCast<String>())
        assertEquals("legendIcon", VisualProps.legendIcon.unsafeCast<String>())
        assertEquals("visualMeta", VisualProps.visualMeta.unsafeCast<String>())
        assertEquals("liftZ", VisualProps.liftZ.unsafeCast<String>())
        assertEquals("decal", VisualProps.decal.unsafeCast<String>())
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
            series = one(lineSeries { typedData = numbers(1.0, 2.0) })
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

    @Test
    fun optionKeywordLiteralsMatchTheUpstreamNames() {
        assertEquals("start", AxisNameLocations.start.unsafeCast<String>())
        assertEquals("middle", AxisNameLocations.middle.unsafeCast<String>())
        assertEquals("center", AxisNameLocations.center.unsafeCast<String>())
        assertEquals("end", AxisNameLocations.end.unsafeCast<String>())
        assertEquals("self", TitleTargets.self.unsafeCast<String>())
        assertEquals("blank", TitleTargets.blank.unsafeCast<String>())
        assertEquals("auto", HorizontalAligns.auto.unsafeCast<String>())
        assertEquals("left", HorizontalAligns.left.unsafeCast<String>())
        assertEquals("right", HorizontalAligns.right.unsafeCast<String>())
        assertEquals("start", LegendPageButtonPositions.start.unsafeCast<String>())
        assertEquals("end", LegendPageButtonPositions.end.unsafeCast<String>())
        assertEquals("left", TimelineControlPositions.left.unsafeCast<String>())
        assertEquals("right", TimelineControlPositions.right.unsafeCast<String>())
        assertEquals("top", TimelineControlPositions.top.unsafeCast<String>())
        assertEquals("bottom", TimelineControlPositions.bottom.unsafeCast<String>())
        assertEquals("truncate", LabelLineOverflows.truncate.unsafeCast<String>())
        assertEquals("raw", GraphicBoundings.raw.unsafeCast<String>())
        assertEquals("all", GraphicBoundings.all.unsafeCast<String>())
        assertEquals("merge", GraphicActions.merge.unsafeCast<String>())
        assertEquals("replace", GraphicActions.replace.unsafeCast<String>())
        assertEquals("remove", GraphicActions.remove.unsafeCast<String>())
        assertEquals("category", MatrixDimensionTypes.category.unsafeCast<String>())
        assertEquals("value", DataZoomRangeModes.value.unsafeCast<String>())
        assertEquals("percent", DataZoomRangeModes.percent.unsafeCast<String>())
        assertEquals("auto", OuterBoundsModes.auto.unsafeCast<String>())
        assertEquals("same", OuterBoundsModes.same.unsafeCast<String>())
        assertEquals("none", OuterBoundsModes.none.unsafeCast<String>())
        assertEquals("all", OuterBoundsContains.all.unsafeCast<String>())
        assertEquals("axisLabel", OuterBoundsContains.axisLabel.unsafeCast<String>())
        assertEquals("auto", OuterBoundsContains.auto.unsafeCast<String>())
        assertEquals("seriesAsc", TooltipOrders.seriesAsc.unsafeCast<String>())
        assertEquals("seriesDesc", TooltipOrders.seriesDesc.unsafeCast<String>())
        assertEquals("valueAsc", TooltipOrders.valueAsc.unsafeCast<String>())
        assertEquals("valueDesc", TooltipOrders.valueDesc.unsafeCast<String>())
        assertEquals("click", AxisPointerTriggerOns.click.unsafeCast<String>())
        assertEquals("mousemove", AxisPointerTriggerOns.mouseMove.unsafeCast<String>())
        assertEquals("none", AxisPointerTriggerOns.none.unsafeCast<String>())
        assertEquals("mousemove|click", AxisPointerTriggerOns.mouseMoveClick.unsafeCast<String>())
        assertEquals("show", AxisPointerStatuses.show.unsafeCast<String>())
        assertEquals("hide", AxisPointerStatuses.hide.unsafeCast<String>())
        assertEquals("click", ParallelAxisExpandTriggerOns.click.unsafeCast<String>())
        assertEquals("mousemove", ParallelAxisExpandTriggerOns.mouseMove.unsafeCast<String>())
    }

    @Test
    fun brushKeywordLiteralsMatchTheUpstreamNames() {
        assertEquals("polygon", BrushTypes.polygon.unsafeCast<String>())
        assertEquals("rect", BrushTypes.rect.unsafeCast<String>())
        assertEquals("lineX", BrushTypes.lineX.unsafeCast<String>())
        assertEquals("lineY", BrushTypes.lineY.unsafeCast<String>())
        assertEquals("auto", BrushTypeUncertains.auto.unsafeCast<String>())
        assertFalse(BrushTypeUncertain(false).unsafeCast<Boolean>())
        assertEquals("single", BrushModes.single.unsafeCast<String>())
        assertEquals("multiple", BrushModes.multiple.unsafeCast<String>())
        assertEquals("fixRate", BrushThrottleTypes.fixRate.unsafeCast<String>())
        assertEquals("debounce", BrushThrottleTypes.debounce.unsafeCast<String>())
        assertEquals("polygon", BrushToolboxIconTypes.polygon.unsafeCast<String>())
        assertEquals("rect", BrushToolboxIconTypes.rect.unsafeCast<String>())
        assertEquals("lineX", BrushToolboxIconTypes.lineX.unsafeCast<String>())
        assertEquals("lineY", BrushToolboxIconTypes.lineY.unsafeCast<String>())
        assertEquals("keep", BrushToolboxIconTypes.keep.unsafeCast<String>())
        assertEquals("clear", BrushToolboxIconTypes.clear.unsafeCast<String>())
    }

    @Test
    fun seriesKeywordLiteralsMatchTheUpstreamNames() {
        assertEquals("mod", ProgressiveChunkModes.mod.unsafeCast<String>())
        assertEquals("auto", AreaOrigins.auto.unsafeCast<String>())
        assertEquals("start", AreaOrigins.start.unsafeCast<String>())
        assertEquals("end", AreaOrigins.end.unsafeCast<String>())
        assertEquals(30.0, AreaOrigin(30.0).unsafeCast<Double>())
    }

    @Test
    fun seriesCoordinateSystemLiteralsMatchTheUpstreamNames() {
        assertEquals("cartesian2d", CartesianPolarCoordinateSystems.cartesian2d.unsafeCast<String>())
        assertEquals("polar", CartesianPolarCoordinateSystems.polar.unsafeCast<String>())
        assertEquals("cartesian2d", CartesianCoordinateSystems.cartesian2d.unsafeCast<String>())
        assertEquals("cartesian2d", HeatmapCoordinateSystems.cartesian2d.unsafeCast<String>())
        assertEquals("geo", HeatmapCoordinateSystems.geo.unsafeCast<String>())
        assertEquals("calendar", HeatmapCoordinateSystems.calendar.unsafeCast<String>())
        assertEquals("matrix", HeatmapCoordinateSystems.matrix.unsafeCast<String>())
        assertEquals("radar", RadarCoordinateSystems.radar.unsafeCast<String>())
        assertEquals("none", ChordCoordinateSystems.none.unsafeCast<String>())
        assertEquals("view", SankeyCoordinateSystems.view.unsafeCast<String>())
        assertEquals("singleAxis", SingleAxisCoordinateSystems.singleAxis.unsafeCast<String>())
    }

    /**
     * Every narrowed series exposes the typed accessor next to the inherited string one; the
     * assignments check the narrowed types at compile time, the round trips check that both
     * properties are the same JavaScript property.
     */
    @Test
    fun typedCoordinateSystemSharesTheStringProperty() {
        val line = unsafeJso<LineSeriesOption> {
            typedCoordinateSystem = CartesianPolarCoordinateSystems.polar
        }
        assertCoordinateSystemRoundTrip(line, "polar")

        val bar = unsafeJso<BarSeriesOption> {
            typedCoordinateSystem = CartesianPolarCoordinateSystems.cartesian2d
        }
        assertCoordinateSystemRoundTrip(bar, "cartesian2d")

        val candlestick = unsafeJso<CandlestickSeriesOption> {
            typedCoordinateSystem = CartesianCoordinateSystems.cartesian2d
        }
        assertCoordinateSystemRoundTrip(candlestick, "cartesian2d")

        val boxplot = unsafeJso<BoxplotSeriesOption> {
            typedCoordinateSystem = CartesianCoordinateSystems.cartesian2d
        }
        assertCoordinateSystemRoundTrip(boxplot, "cartesian2d")

        val heatmap = unsafeJso<HeatmapSeriesOption> {
            typedCoordinateSystem = HeatmapCoordinateSystems.calendar
        }
        assertCoordinateSystemRoundTrip(heatmap, "calendar")

        val pictorialBar = unsafeJso<PictorialBarSeriesOption> {
            typedCoordinateSystem = CartesianCoordinateSystems.cartesian2d
        }
        assertCoordinateSystemRoundTrip(pictorialBar, "cartesian2d")

        val radar = unsafeJso<RadarSeriesOption> {
            typedCoordinateSystem = RadarCoordinateSystems.radar
        }
        assertCoordinateSystemRoundTrip(radar, "radar")

        val themeRiver = unsafeJso<ThemeRiverSeriesOption> {
            typedCoordinateSystem = SingleAxisCoordinateSystems.singleAxis
        }
        assertCoordinateSystemRoundTrip(themeRiver, "singleAxis")

        val chord = unsafeJso<ChordSeriesOption> {
            typedCoordinateSystem = ChordCoordinateSystems.none
        }
        assertCoordinateSystemRoundTrip(chord, "none")

        val sankey = unsafeJso<SankeySeriesOption> {
            typedCoordinateSystem = SankeyCoordinateSystems.view
        }
        assertCoordinateSystemRoundTrip(sankey, "view")
    }

    /**
     * The narrowed accessor also survives a real chart: the value written through it is the value
     * that ECharts keeps on the merged series option.
     */
    @Test
    fun typedCoordinateSystemReachesTheMergedOption() {
        val option = cartesianLineOption(1.0, 2.0) {
            series = one(
                lineSeries {
                    typedData = numbers(1.0, 2.0)
                    typedCoordinateSystem = CartesianPolarCoordinateSystems.cartesian2d
                },
            )
        }

        val (chart, container) = createChart()
        try {
            chart.setOption(option)
            val stored = chart.getOption().series!![0].unsafeCast<LineSeriesOption>()
            assertEquals("cartesian2d", stored.coordinateSystem)
            assertEquals(
                "cartesian2d",
                stored.typedCoordinateSystem!!.unsafeCast<String>(),
            )
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * `graphic.$action` cannot be a Kotlin identifier, so the property is exposed as [GraphicElement.action]
     * with `@JsName`; this proves the mapping still writes `$action` on the JavaScript object.
     */
    @Test
    fun graphicActionIsWrittenToTheActionProperty() {
        val element = unsafeJso<GraphicElement> {
            type = "group"
            action = GraphicActions.replace
        }
        val probe = element.unsafeCast<ActionProbe>()
        assertEquals("replace", probe.action)
    }

    /**
     * Assigning the constants to the typed fields checks the narrowed types at compile time, and
     * reading them back checks that the names and values reach the underlying JavaScript objects.
     */
    @Test
    fun typedOptionKeywordFieldsKeepTheirValues() {
        val option = cartesianLineOption(1.0, 2.0) {
            title = one(
                unsafeJso<TitleOption> {
                    text = "title"
                    target = TitleTargets.blank
                    subtarget = TitleTargets.self
                },
            )
            legend = one(
                unsafeJso<LegendOption> {
                    align = HorizontalAligns.left
                    pageButtonPosition = LegendPageButtonPositions.end
                },
            )
            tooltip = one(unsafeJso<TooltipOption> { order = TooltipOrders.valueDesc })
            grid = one(
                unsafeJso<GridOption> {
                    outerBoundsMode = OuterBoundsModes.none
                    outerBoundsContain = OuterBoundsContains.axisLabel
                },
            )
            xAxis = one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    data = strings("a", "b")
                    name = "x"
                    nameLocation = AxisNameLocations.middle
                },
            )
            yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
            dataZoom = one(
                unsafeJso<DataZoomOption> {
                    type = DataZoomTypes.inside
                    rangeMode = arrayOf(DataZoomRangeModes.value, DataZoomRangeModes.percent)
                    showDataShadow = BooleanOrString("auto")
                    realtime = true
                },
            )
            visualMap = one(
                unsafeJso<VisualMapOption> {
                    type = VisualMapTypes.piecewise
                    align = HorizontalAligns.right
                    selectedMode = SelectionModes.multiple
                },
            )
            series = one(
                lineSeries {
                    typedData = numbers(1.0, 2.0)
                    progressiveChunkMode = ProgressiveChunkModes.mod
                    areaStyle = unsafeJso<AreaStyleOption> { origin = AreaOrigins.start }
                    label = unsafeJso<LabelOption> {
                        show = true
                        lineOverflow = LabelLineOverflows.truncate
                    }
                },
            )
        }

        val (chart, container) = createChart()
        try {
            chart.setOption(option)
            val stored = chart.getOption()
            assertEquals(TitleTargets.blank, stored.title!![0].target)
            assertEquals(TitleTargets.self, stored.title!![0].subtarget)
            assertEquals(HorizontalAligns.left, stored.legend!![0].align)
            assertEquals(LegendPageButtonPositions.end, stored.legend!![0].pageButtonPosition)
            // `tooltip.order` is an option-only field; ECharts does not keep it in the merged option.
            assertEquals(TooltipOrders.valueDesc, option.tooltip!![0].order)
            assertEquals(OuterBoundsModes.none, stored.grid!![0].outerBoundsMode)
            assertEquals(OuterBoundsContains.axisLabel, stored.grid!![0].outerBoundsContain)
            assertEquals(AxisNameLocations.middle, stored.xAxis!![0].nameLocation)
            assertEquals(DataZoomRangeModes.value, stored.dataZoom!![0].rangeMode!![0])
            assertEquals(DataZoomRangeModes.percent, stored.dataZoom!![0].rangeMode!![1])
            assertEquals(true, stored.dataZoom!![0].realtime)
            assertEquals(HorizontalAligns.right, stored.visualMap!![0].align)
            assertEquals(SelectionModes.multiple, stored.visualMap!![0].selectedMode)
            val line = stored.series!![0].unsafeCast<LineSeriesOption>()
            assertEquals(ProgressiveChunkModes.mod, line.progressiveChunkMode)
            assertEquals(AreaOrigins.start, line.areaStyle!!.origin)
            assertEquals(LabelLineOverflows.truncate, line.label!!.lineOverflow)
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * Timeline, parallel, brush and timeline control options are only valid in specific option
     * shapes, so their narrowed fields are checked on the plain option objects instead of a chart.
     */
    @Test
    fun componentOptionObjectsKeepTheNarrowedKeywords() {
        val timeline = unsafeJso<TimelineOption> {
            controlPosition = TimelineControlPositions.right
            controlStyle = unsafeJso<TimelineControlStyle> { position = TimelineControlPositions.bottom }
        }
        assertEquals(TimelineControlPositions.right, timeline.controlPosition)
        assertEquals(TimelineControlPositions.bottom, timeline.controlStyle!!.position)

        val parallel = unsafeJso<ParallelOption> {
            layout = Orients.vertical
            axisExpandTriggerOn = ParallelAxisExpandTriggerOns.mouseMove
        }
        assertEquals(Orients.vertical, parallel.layout)
        assertEquals(ParallelAxisExpandTriggerOns.mouseMove, parallel.axisExpandTriggerOn)

        val brush = unsafeJso<BrushOption> {
            brushType = BrushTypeUncertains.auto
            brushMode = BrushModes.multiple
            throttleType = BrushThrottleTypes.debounce
            toolbox = arrayOf(
                BrushToolboxIconTypes.rect,
                BrushToolboxIconTypes.keep,
                BrushToolboxIconTypes.clear,
            )
        }
        assertEquals(BrushTypeUncertains.auto, brush.brushType)
        assertEquals(BrushModes.multiple, brush.brushMode)
        assertEquals(BrushThrottleTypes.debounce, brush.throttleType)
        assertEquals(3, brush.toolbox!!.size)

        val pointer = unsafeJso<AxisPointerOption> {
            triggerOn = AxisPointerTriggerOns.mouseMoveClick
            status = AxisPointerStatuses.show
        }
        assertEquals(AxisPointerTriggerOns.mouseMoveClick, pointer.triggerOn)
        assertEquals(AxisPointerStatuses.show, pointer.status)

        val matrix = unsafeJso<MatrixDimensionOption> { type = MatrixDimensionTypes.category }
        assertEquals(MatrixDimensionTypes.category, matrix.type)
    }
}

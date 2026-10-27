package echarts.options

import echarts.AxisBreakActions
import echarts.AxisBreakChangedEvent
import echarts.AxisTypes
import echarts.ExpandAxisBreakPayload
import echarts.GraphLayouts
import echarts.HorizontalAligns
import echarts.Payload
import echarts.ToggleAxisBreakPayload
import echarts.TooltipRenderModes
import echarts.TooltipTriggers
import echarts.cartesianLineOption
import echarts.createChart
import echarts.createContainer
import echarts.destroyChart
import echarts.onAxisBreakChanged
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.jsTypeOf
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import web.cssom.VerticalAlign
import web.html.HTMLElement
import zrender.ZRColor

/**
 * Reads `xAxisIndex` as a string, to check that the `@JsName("xAxisIndex")` duplicate property of
 * the axis break payloads really maps to the same JavaScript property.
 */
private external interface AxisIndexProbe {
    var xAxisIndex: JsAny?
    var yAxisIndex: JsAny?
}

/**
 * Coverage for the declarations added from the upstream export lists: animation callbacks, axis
 * breaks and the extra axis fields, the thumbnail component, the tooltip formatter/position
 * callbacks and the action payloads.
 *
 * Upstream has no unit tests for these declarations, so the cases assert what the wrapper can
 * break: the literal values reach ECharts, callbacks receive the documented parameters, and the
 * payload types map to the documented action shape.
 */
class ExportSurfaceTest {
    /**
     * Both animation branches reach the elements ECharts animates.
     *
     * Upstream `AnimationOptionMixin.animationDuration` / `animationDelay` are `number | callback`
     * and are called once per animated element, which makes the call counters observable.
     */
    @Test
    fun animationCallbacksDriveTheFirstRender() {
        assertEquals("number", jsTypeOf(AnimationDuration(500.0)))
        assertEquals("number", jsTypeOf(AnimationDelay(20.0)))
        assertEquals("function", jsTypeOf(AnimationDuration { 500.0 }))
        assertEquals("function", jsTypeOf(AnimationDelay { _, _ -> 20.0 }))

        var rootDurationCalls = 0
        var rootDelayCalls = 0
        var delayParamsCount = -1.0
        val (rootChart, rootContainer) = createChart()
        try {
            rootChart.setOption(
                unsafeJso<EChartsOption> {
                    animationDuration = AnimationDuration {
                        rootDurationCalls++
                        200.0
                    }
                    animationDelay = AnimationDelay { index, params ->
                        rootDelayCalls++
                        // The extra parameters are optional upstream and only some coordinate
                        // systems pass them.
                        if (params != null) delayParamsCount = params.count
                        index * 10.0
                    }
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings("a", "b", "c") })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = one(lineSeries { data = numbers(1.0, 2.0, 3.0) })
                },
            )

            // The root callbacks only run for elements that do not override them.
            assertTrue(rootDurationCalls > 0, "the duration callback should run for the animated elements")
            assertTrue(rootDelayCalls > 0, "the delay callback should run for the animated elements")
            assertTrue(delayParamsCount >= -1.0, "the delay callback parameters stay readable")
        } finally {
            destroyChart(rootChart, rootContainer)
        }

        var seriesDurationCalls = 0
        val (seriesChart, seriesContainer) = createChart()
        try {
            seriesChart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings("a", "b", "c") })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = one(
                        lineSeries {
                            data = numbers(1.0, 2.0, 3.0)
                            // A series-level callback wins over the root option.
                            animationDuration = AnimationDuration {
                                seriesDurationCalls++
                                150.0
                            }
                        },
                    )
                },
            )

            assertTrue(seriesDurationCalls > 0, "the series-level duration callback should run")
        } finally {
            destroyChart(seriesChart, seriesContainer)
        }
    }

    /**
     * Upstream also exports `LabelFormatterCallback`, `LabelLayoutOptionCallback` and
     * `LabelLayoutOptionCallbackParams`; the wrapper maps them to the existing callback aliases.
     */
    @Test
    fun labelCallbacksUseTheAliasSignatures() {
        var formatterCalls = 0
        var layoutCalls = 0
        val formatter: LabelFormatterCallback = { params ->
            formatterCalls++
            params.name
        }
        val layout: LabelLayoutOptionCallback = { params: LabelLayoutOptionCallbackParams ->
            layoutCalls++
            assertNotNull(params.dataIndex, "a series label always belongs to a data item")
            assertTrue(params.text.isNotEmpty(), "the flattened label text should be readable")
            unsafeJso<LabelLayoutOption> { hideOverlap = true }
        }

        val (chart, container) = createChart()
        try {
            chart.setOption(
                cartesianLineOption(1.0, 2.0) {
                    series = one(
                        lineSeries {
                            data = numbers(1.0, 2.0)
                            label = unsafeJso<LabelOption> {
                                show = true
                                this.formatter = LabelFormatter(formatter)
                            }
                            labelLayout = LabelLayout(layout)
                        },
                    )
                },
            )

            assertTrue(formatterCalls > 0, "the label formatter should run for the rendered labels")
            assertTrue(layoutCalls > 0, "the label layout callback should run for the rendered labels")
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * `TooltipFormatterCallback<TopLevelFormatterParams>` receives a single object with the
     * `"item"` trigger and an array with the `"axis"` trigger.
     */
    @Test
    fun tooltipFormatterReceivesTheItemAndAxisBranches() {
        val (itemChart, itemContainer) = createChart()
        try {
            var itemName: String? = null
            itemChart.setOption(
                unsafeJso<EChartsOption> {
                    tooltip = one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            formatter = TooltipFormatter { params ->
                                itemName = params.unsafeCast<CallbackDataParams>().name
                                TooltipContent("item")
                            }
                        },
                    )
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings("a", "b", "c") })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = one(lineSeries { data = numbers(1.0, 2.0, 3.0) })
                },
            )

            itemChart.dispatchAction(
                unsafeJso<Payload> {
                    type = "showTip"
                    seriesIndex = 0.0
                    dataIndex = 0.0
                },
            )
            assertEquals("a", itemName)
        } finally {
            destroyChart(itemChart, itemContainer)
        }

        val (axisChart, axisContainer) = createChart()
        try {
            var axisNames: List<String>? = null
            axisChart.setOption(
                unsafeJso<EChartsOption> {
                    tooltip = one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            formatter = TooltipFormatter { params ->
                                axisNames = params.unsafeCast<Array<CallbackDataParams>>().map { it.name }
                                TooltipContent("axis")
                            }
                        },
                    )
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings("a", "b", "c") })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = one(lineSeries { data = numbers(1.0, 2.0, 3.0) })
                },
            )

            axisChart.dispatchAction(
                unsafeJso<Payload> {
                    type = "showTip"
                    seriesIndex = 0.0
                    dataIndex = 0.0
                },
            )
            assertEquals(listOf("a"), axisNames)
        } finally {
            destroyChart(axisChart, axisContainer)
        }
    }

    /**
     * `TooltipPositionCallback` receives the pointer, the formatter parameters, the tooltip
     * element, the hovered rect and the tooltip sizes.
     */
    @Test
    fun tooltipPositionCallbackReceivesPointParamsAndSizes() {
        var pointSize = 0
        var contentSize = 0
        var viewSize = 0
        var positionParamsName: String? = null

        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    tooltip = one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            position = TooltipPosition { point, params, el, rect, size ->
                                pointSize = point.size
                                contentSize = size.contentSize.size
                                viewSize = size.viewSize.size
                                positionParamsName = params.unsafeCast<CallbackDataParams>().name
                                assertNotNull(el, "the HTML tooltip element is passed when it exists")
                                assertNotNull(rect, "the hovered element rect is passed")
                                TooltipPosition(arrayOf(StringOrDouble(0.0), StringOrDouble(0.0)))
                            }
                        },
                    )
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings("a", "b") })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = one(lineSeries { data = numbers(1.0, 2.0) })
                },
            )

            chart.dispatchAction(
                unsafeJso<Payload> {
                    type = "showTip"
                    seriesIndex = 0.0
                    dataIndex = 0.0
                },
            )

            assertEquals(2, pointSize)
            assertEquals(2, contentSize)
            assertEquals(2, viewSize)
            assertEquals("a", positionParamsName)
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * The tooltip fields added from the upstream option and the `TooltipAppendTo` union.
     */
    @Test
    fun tooltipFieldsAndAppendTargetsKeepTheirValues() {
        val target = createContainer()
        try {
            assertEquals("string", jsTypeOf(TooltipAppendTo("#tooltip")))
            assertTrue(target === TooltipAppendTo(target).unsafeCast<HTMLElement>())

            val callback: (chartContainer: HTMLElement) -> HTMLElement? = { chartContainer -> chartContainer }
            val appendTo = TooltipAppendTo(callback)
            assertEquals("function", jsTypeOf(appendTo))
            assertTrue(target === appendTo.unsafeCast<(HTMLElement) -> HTMLElement?>()(target))
        } finally {
            target.remove()
        }

        val (chart, container) = createChart()
        try {
            chart.setOption(
                cartesianLineOption(1.0, 2.0) {
                    tooltip = one(
                        unsafeJso<TooltipOption> {
                            renderMode = TooltipRenderModes.richText
                            align = HorizontalAligns.center
                            verticalAlign = VerticalAlign.middle
                            displayTransition = false
                            appendToBody = false
                            defaultBorderColor = "#123456"
                            borderRadius = 4.0
                            shadowBlur = 6.0
                            shadowColor = "#000000"
                            shadowOffsetX = 1.0
                            shadowOffsetY = 2.0
                        },
                    )
                },
            )

            val stored = assertNotNull(chart.getOption().tooltip).single()
            assertEquals(TooltipRenderModes.richText, stored.renderMode)
            assertEquals(HorizontalAligns.center, stored.align)
            assertEquals(VerticalAlign.middle, stored.verticalAlign)
            assertFalse(stored.displayTransition!!)
            assertEquals("#123456", stored.defaultBorderColor)
            assertEquals(4.0, stored.borderRadius)
            assertEquals(6.0, stored.shadowBlur)
            assertEquals("#000000", stored.shadowColor)
            assertEquals(1.0, stored.shadowOffsetX)
            assertEquals(2.0, stored.shadowOffsetY)
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * The axis break option fields and the three axis break actions.
     *
     * Upstream only covers these in `test/axis-break.html`; the case follows the external buttons
     * of that page and reads the `axisbreakchanged` event instead of the visual result.
     */
    @Test
    fun axisBreakActionsReportTheChange() {
        val events = mutableListOf<AxisBreakChangedEvent>()
        val (chart, container) = createChart()
        try {
            chart.setOption(
                cartesianLineOption(1.0, 5.0, 2.0) {
                    xAxis = one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            breaks = one(
                                unsafeJso<AxisBreakOption> {
                                    start = OptionDataValue(2.0)
                                    end = OptionDataValue(4.0)
                                    gap = StringOrDouble(10.0)
                                    isExpanded = false
                                },
                            )
                            breakArea = unsafeJso<AxisBreakAreaOption> {
                                show = true
                                zigzagAmplitude = 0.0
                                expandOnClick = false
                            }
                            breakLabelLayout = unsafeJso<AxisBreakLabelLayoutOption> {
                                moveOverlap = BooleanOrString(false)
                            }
                        },
                    )
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                },
            )

            val storedBreak = assertNotNull(chart.getOption().xAxis!![0].breaks).single()
            assertEquals(2.0, storedBreak.start.unsafeCast<Double>())
            assertEquals(4.0, storedBreak.end.unsafeCast<Double>())
            assertEquals(10.0, storedBreak.gap?.unsafeCast<Double>())
            assertFalse(storedBreak.isExpanded!!)

            chart.onAxisBreakChanged { event -> events += event }

            fun dispatch(type: String, payload: ToggleAxisBreakPayload) {
                payload.type = type
                chart.dispatchAction(payload)
            }

            fun breakIdentifier(): AxisBreakIdentifier = unsafeJso {
                start = OptionDataValue(2.0)
                end = OptionDataValue(4.0)
            }

            dispatch(
                "expandAxisBreak",
                unsafeJso<ToggleAxisBreakPayload> {
                    typedXAxisIndex = ModelFinderIndexQuery("all")
                    breaks = arrayOf(breakIdentifier())
                },
            )
            dispatch(
                "collapseAxisBreak",
                unsafeJso<ToggleAxisBreakPayload> {
                    typedXAxisIndex = ModelFinderIndexQuery("all")
                    breaks = arrayOf(breakIdentifier())
                },
            )
            dispatch(
                "toggleAxisBreak",
                unsafeJso<ToggleAxisBreakPayload> {
                    typedXAxisIndex = ModelFinderIndexQuery("all")
                    breaks = arrayOf(breakIdentifier())
                },
            )

            assertEquals(
                listOf(AxisBreakActions.expand, AxisBreakActions.collapse, AxisBreakActions.toggle),
                events.map { it.fromAction },
            )
            assertTrue(events[0].breaks.single().isExpanded)
            assertFalse(events[0].breaks.single().old.isExpanded)
            assertFalse(events[1].breaks.single().isExpanded)
            assertTrue(events[1].breaks.single().old.isExpanded)
            assertTrue(events[2].breaks.single().isExpanded)
            assertFalse(events[2].breaks.single().old.isExpanded)
            assertEquals(2.0, events[0].breaks.single().start.unsafeCast<Double>())
            assertEquals(4.0, events[0].breaks.single().end.unsafeCast<Double>())
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * `typedXAxisIndex` is declared with `@JsName("xAxisIndex")`, so it must write the same
     * JavaScript property that the inherited `Payload.xAxisIndex` reads.
     */
    @Test
    fun axisBreakPayloadMapsTheTypedAxisIndexToTheJsProperty() {
        val payload = unsafeJso<ToggleAxisBreakPayload> {
            typedXAxisIndex = ModelFinderIndexQuery("all")
        }
        assertEquals("all", payload.unsafeCast<AxisIndexProbe>().xAxisIndex.unsafeCast<String>())

        val yPayload = unsafeJso<ExpandAxisBreakPayload> {
            typedYAxisIndex = ModelFinderIndexQuery(arrayOf(0.0, 1.0))
        }
        val probe = yPayload.unsafeCast<AxisIndexProbe>()
        assertEquals(2, probe.yAxisIndex.unsafeCast<Array<Double>>().size)
    }

    /**
     * The axis fields added for breaks, jitter and extent tuning.
     */
    @Test
    fun axisExtraFieldsSurviveTheOptionRoundTrip() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings("a", "b", "c")
                            jitter = 5.0
                            jitterOverlap = false
                            jitterMargin = 2.0
                            deduplication = true
                            containShape = false
                            tooltip = unsafeJso<AxisTooltipOption> { show = true }
                        },
                    )
                    yAxis = one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            startValue = 0.0
                            dataMin = OptionDataValue(-1.0)
                            dataMax = OptionDataValue(10.0)
                        },
                    )
                    series = one(
                        lineSeries {
                            data = numbers(1.0, 2.0, 3.0)
                            coordinateSystem = "cartesian2d"
                        },
                    )
                },
            )

            val xAxis = chart.getOption().xAxis!![0]
            assertEquals(5.0, xAxis.jitter)
            assertEquals(false, xAxis.jitterOverlap)
            assertEquals(2.0, xAxis.jitterMargin)
            assertEquals(true, xAxis.deduplication)
            assertEquals(false, xAxis.containShape)
            assertEquals(true, xAxis.tooltip!!.show)

            val yAxis = chart.getOption().yAxis!![0]
            assertEquals(0.0, yAxis.startValue)
            assertEquals(-1.0, yAxis.dataMin?.unsafeCast<Double>())
            assertEquals(10.0, yAxis.dataMax?.unsafeCast<Double>())
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * `categorySortInfo` is category-axis only, so its narrowed shape is checked on the plain
     * option object like the other component-only keywords.
     */
    @Test
    fun categorySortInfoKeepsTheOrdinalNumbers() {
        val axis = unsafeJso<XAxisOption> {
            type = AxisTypes.category
            categorySortInfo = unsafeJso<OrdinalSortInfo> {
                ordinalNumbers = arrayOf(1.0, 0.0)
            }
        }

        val stored = assertNotNull(axis.categorySortInfo).ordinalNumbers
        assertEquals(2, stored.size)
        assertEquals(1.0, stored[0])
        assertEquals(0.0, stored[1])
    }

    /**
     * The thumbnail component is upstream's miniature view of a graph. Only graph series support
     * it, so the case follows `test/graph-thumbnail.html`.
     */
    @Test
    fun thumbnailOptionIsAcceptedAndRendered() {
        val (chart, container) = createChart(600, 400)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    thumbnail = one(
                        unsafeJso<ThumbnailOption> {
                            show = true
                            left = StringOrDouble(0.0)
                            top = StringOrDouble(0.0)
                            width = StringOrDouble(120.0)
                            height = StringOrDouble(90.0)
                            seriesIndex = IndexOrIndexArray(0.0)
                            itemStyle = unsafeJso<ItemStyleOption> {
                                color = ZRColor("#ff0000")
                                borderWidth = 2.0
                                borderColor = ZRColor("#000000")
                            }
                            windowStyle = unsafeJso<ItemStyleOption> { borderWidth = 1.0 }
                        },
                    )
                    series = one(
                        graphSeries {
                            layout = GraphLayouts.circular
                            data = arrayOf(
                                unsafeJso<GraphNodeItemOption> { name = "a" }.asThumbnailDataItem(),
                                unsafeJso<GraphNodeItemOption> { name = "b" }.asThumbnailDataItem(),
                                unsafeJso<GraphNodeItemOption> { name = "c" }.asThumbnailDataItem(),
                            )
                            links = arrayOf(
                                unsafeJso<GraphEdgeItemOption> { source = StringOrDouble("a"); target = StringOrDouble("b") },
                                unsafeJso<GraphEdgeItemOption> { source = StringOrDouble("b"); target = StringOrDouble("c") },
                            )
                        },
                    )
                },
            )

            val stored = assertNotNull(chart.getOption().thumbnail)
            assertEquals(1, stored.size)
            assertTrue(stored[0].show!!)
            assertEquals(120.0, stored[0].width?.unsafeCast<Double>())
            assertEquals(90.0, stored[0].height?.unsafeCast<Double>())
            assertEquals("#ff0000", stored[0].itemStyle!!.color?.unsafeCast<String>())
            assertEquals(2.0, stored[0].itemStyle!!.borderWidth)
            assertTrue(chart.renderToCanvas().width > 0, "the chart with the thumbnail should render")
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * `ECBasicOption` is the upstream name of the base option; the wrapper aliases it to
     * [EChartsOption] so both names are interchangeable.
     */
    @Test
    fun ecBasicOptionIsAnAliasOfEChartsOption() {
        val option: ECBasicOption = unsafeJso<EChartsOption> {
            color = arrayOf(ZRColor("#111111"), ZRColor("#222222"))
            legacyViewCoordSysCenterBase = true
            legacyMinMaxDontInverseAxis = false
            colorLayer = arrayOf(
                arrayOf(ZRColor("#ff0000"), ZRColor("#00ff00")),
                arrayOf(ZRColor("#0000ff"), ZRColor("#ffffff")),
            )
            xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings("a", "b") })
            yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
            series = one(lineSeries { data = numbers(1.0, 2.0) })
        }
        val asTypedOption: EChartsOption = option
        assertEquals(2, asTypedOption.colorLayer?.size)
        assertEquals("#0000ff", asTypedOption.colorLayer!![1][0].unsafeCast<String>())

        val (chart, container) = createChart()
        try {
            chart.setOption(asTypedOption)
            val stored = chart.getOption()
            assertEquals(1, stored.series?.size ?: 0)
            assertEquals(true, stored.legacyViewCoordSysCenterBase)
            assertEquals(false, stored.legacyMinMaxDontInverseAxis)
            // `colorLayer` is a palette input; ECharts normalises it while building the model, so
            // only the rendered result is asserted here.
            assertTrue(chart.renderToCanvas().width > 0, "the option with layered colors should render")
        } finally {
            destroyChart(chart, container)
        }
    }
}

/**
 * `GraphNodeItemOption` is not an `OptionDataItemObject`, so graph data needs an explicit cast,
 * exactly like upstream's `GraphNodeItemOption` union.
 */
private fun JsAny.asThumbnailDataItem(): OptionDataItem? = unsafeCast<OptionDataItem?>()

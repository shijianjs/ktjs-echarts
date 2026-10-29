package echarts.options

import echarts.SeriesTypes
import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.JsName
import kotlin.js.jsTypeOf
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/**
 * Reads the `$mergeChildren` property, which cannot be written as a Kotlin identifier.
 */
private external interface MergeChildrenProbe {
    @JsName("\$mergeChildren")
    var mergeChildren: String?

    @JsName("\$mergeChildren")
    var mergeChildrenOff: Boolean
}

/**
 * Reads the two `during` callbacks, which share one JavaScript property name.
 */
private external interface DuringProbe {
    var during: ((TransitionBaseDuringAPI) -> Unit)?

    @JsName("during")
    var typedDuring: ((TransitionDuringAPI) -> Unit)?
}

/**
 * Tests for the element tree returned by a custom series `renderItem`.
 *
 * The upstream types are built from zrender's `PathProps` / `TextProps` / `ImageProps` /
 * `GroupProps` and from generic helpers; Kotlin cannot carry those parameters, so the tests check
 * the shared members, the `@JsName` mappings and that a real chart accepts the tree.
 */
class CustomElementOptionsTest {
    @Test
    fun groupWithChildrenKeepsTheElementTree() {
        val group = unsafeJso<CustomGroupOption> {
            type = "group"
            x = 10.0
            width = 120.0
            height = 80.0
            diffChildrenByName = true
            mergeChildren = CustomMergeChildrenOptions.byName
            transition = StringOrStringArray(arrayOf("x", "style"))
            enterFrom = unsafeJso<CustomElementProps> { x = -20.0 }
            enterAnimation = unsafeJso<ElementAnimationOption> { duration = 300.0 }
            children = arrayOf(
                unsafeJso<CustomPathOption> {
                    type = "rect"
                    shape = unsafeJso<ZRShapeProps> {
                        x = 0.0
                        y = 0.0
                        width = 40.0
                        height = 20.0
                    }
                    style = unsafeJso<ZRStyleProps> {
                        fill = ZRColor("#ff0000")
                        lineWidth = 2.0
                    }
                },
                unsafeJso<CustomTextOption> {
                    type = "text"
                    style = unsafeJso<ZRStyleProps> {
                        text = "cell"
                        fontSize = StringOrDouble(12.0)
                    }
                },
            )
        }

        assertEquals("group", group.type)
        assertEquals(2, group.children.size)
        assertTrue(group.diffChildrenByName!!)
        assertEquals("byName", group.unsafeCast<MergeChildrenProbe>().mergeChildren)
        assertEquals(-20.0, group.enterFrom!!.x)
        assertEquals(300.0, group.enterAnimation!!.duration)
        assertEquals("x", group.transition!!.unsafeCast<Array<String>>()[0])

        val path = group.children[0].unsafeCast<CustomPathOption>()
        assertEquals("rect", path.type)
        assertEquals(40.0, path.shape!!.width)
        assertEquals(2.0, path.style!!.lineWidth)
        assertEquals("cell", group.children[1].unsafeCast<CustomTextOption>().style!!.text)
    }

    @Test
    fun mergeChildrenAcceptsTheDisableValue() {
        val group = unsafeJso<CustomGroupOption> {
            type = "group"
            children = emptyArray()
            mergeChildren = CustomMergeChildrenOptions.none
        }

        assertFalse(group.unsafeCast<MergeChildrenProbe>().mergeChildrenOff)
        assertFalse(CustomMergeChildrenOption(false).unsafeCast<Boolean>())
        assertEquals("byIndex", CustomMergeChildrenOptions.byIndex.unsafeCast<String>())
    }

    @Test
    fun booleanAndStyleUnionsWrapTheirValues() {
        val text = unsafeJso<CustomTextOption> { type = "text" }
        assertSame(text, CustomTextContentOption(text).unsafeCast<CustomTextOption>())
        assertFalse(CustomTextContentOption(false).unsafeCast<Boolean>())

        val path = unsafeJso<CustomPathOption> { type = "rect" }
        assertSame(path, CustomClipPathOption(path).unsafeCast<CustomPathOption>())
        assertFalse(CustomClipPathOption(false).unsafeCast<Boolean>())

        val style = unsafeJso<ZRStyleProps> { fill = ZRColor("#00ff00") }
        assertSame(style, CustomStyleOption(style).unsafeCast<ZRStyleProps>())
        assertFalse(CustomStyleOption(false).unsafeCast<Boolean>())

        assertEquals("self", CustomElementFocuses.self.unsafeCast<String>())
        val indices = CustomElementFocus(arrayOf(0.0, 2.0)).unsafeCast<Array<Double>>()
        assertEquals(2, indices.size)
    }

    @Test
    fun duringCallbacksShareTheDuringProperty() {
        val group = unsafeJso<CustomGroupOption> {
            type = "group"
            children = emptyArray()
            during = { api -> api.setTransform(zrender.TransformProps.x, 1.0) }
        }
        val groupProbe = group.unsafeCast<DuringProbe>()
        assertEquals("function", jsTypeOf(groupProbe.during))
        assertEquals("function", jsTypeOf(groupProbe.typedDuring))

        val path = unsafeJso<CustomPathOption> {
            type = "circle"
            typedDuring = { api -> api.setStyle("fill", ZRColor("#0000ff").unsafeCast<JsAny>()) }
        }
        val pathProbe = path.unsafeCast<DuringProbe>()
        assertEquals("function", jsTypeOf(pathProbe.typedDuring))
        assertEquals("function", jsTypeOf(pathProbe.during))
    }

    @Test
    fun shapeAndStyleCanCarryTheirOwnTransitions() {
        val path = unsafeJso<CustomPathOption> {
            type = "sector"
            shape =
                unsafeJso<CustomElementShapeOption> {
                    cx = 10.0
                    cy = 20.0
                    r = 30.0
                    r0 = 10.0
                    startAngle = 0.0
                    endAngle = -1.5
                    transition = StringOrStringArray("endAngle")
                    enterFrom = unsafeJso<ZRShapeProps> { endAngle = 0.0 }
                    enterAnimation = unsafeJso<ElementAnimationOption> { duration = 300.0 }
                }
            style =
                unsafeJso<CustomElementStyleOption> {
                    fill = ZRColor("#5470c6")
                    transition = StringOrStringArray("opacity")
                    enterFrom = unsafeJso<ZRStyleProps> { opacity = 0.0 }
                }
        }

        val shape = path.shape!!.unsafeCast<CustomElementShapeOption>()
        assertEquals(-1.5, shape.endAngle)
        assertEquals("endAngle", shape.transition.unsafeCast<String>())
        assertEquals(0.0, shape.enterFrom!!.endAngle)
        assertEquals(300.0, shape.enterAnimation!!.duration)

        val style = path.style!!.unsafeCast<CustomElementStyleOption>()
        assertEquals("opacity", style.transition.unsafeCast<String>())
        assertEquals(0.0, style.enterFrom!!.opacity)
    }

    @Test
    fun keyframeAnimationKeepsTheFramesAndStates() {
        val animation = unsafeJso<ElementKeyframeAnimationOption> {
            duration = 500.0
            loop = true
            keyframes = arrayOf(
                unsafeJso<AnimationKeyframe> { percent = 0.0; style = unsafeJso<ZRStyleProps> { opacity = 0.0 } },
                unsafeJso<AnimationKeyframe> { percent = 100.0; style = unsafeJso<ZRStyleProps> { opacity = 1.0 } },
            )
        }
        val path = unsafeJso<CustomPathOption> {
            type = "rect"
            keyframeAnimation = ElementKeyframeAnimationOptionOrArray(animation)
            emphasis = unsafeJso<CustomDisplayableOptionOnState> {
                style = CustomStyleOption(false)
            }
            blur = unsafeJso<CustomDisplayableOptionOnState> { z2 = 3.0 }
        }

        val stored = path.keyframeAnimation!!.unsafeCast<ElementKeyframeAnimationOption>()
        assertEquals(500.0, stored.duration)
        assertTrue(stored.loop!!)
        assertEquals(2, stored.keyframes!!.size)
        assertEquals(100.0, stored.keyframes!![1].percent)
        assertFalse(path.emphasis!!.style!!.unsafeCast<Boolean>())
        assertEquals(3.0, path.blur!!.z2)

        val combined = ElementKeyframeAnimationOptionOrArray(arrayOf(animation, animation))
        assertEquals(2, combined.unsafeCast<Array<ElementKeyframeAnimationOption>>().size)
    }

    @Test
    fun rootElementCarriesTheFocusFields() {
        val root = unsafeJso<CustomRootElementOption> {
            type = "group"
            focus = CustomElementFocuses.series
            blurScope = echarts.BlurScopes.coordinateSystem
            emphasisDisabled = true
        }

        assertEquals("series", root.focus!!.unsafeCast<String>())
        assertEquals(echarts.BlurScopes.coordinateSystem, root.blurScope)
        assertTrue(root.emphasisDisabled!!)
    }

    @Test
    fun renderItemDrawsTheCustomElementTree() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = echarts.AxisTypes.category; data = strings("a") })
                    yAxis = one(unsafeJso<YAxisOption> { type = echarts.AxisTypes.value })
                    series = one(
                        customSeries {
                            typedData = points(doubleArrayOf(0.0, 1.0))
                            renderItem = { _, api ->
                                unsafeJso<CustomGroupOption> {
                                    type = "group"
                                    children = arrayOf(
                                        unsafeJso<CustomPathOption> {
                                            type = "rect"
                                            shape = unsafeJso<ZRShapeProps> {
                                                width = api.getWidth()
                                                height = 10.0
                                            }
                                            style = unsafeJso<ZRStyleProps> { fill = ZRColor("#5470c6") }
                                        },
                                        unsafeJso<CustomTextOption> {
                                            type = "text"
                                            style = unsafeJso<ZRStyleProps> { text = "cell" }
                                        },
                                    )
                                }
                            }
                        },
                    )
                },
            )

            assertEquals(SeriesTypes.custom, chart.getOption().series!![0].type)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun renderItemCanConvertCoordinates() {
        val (chart, container) = createChart()
        var point: Array<Double>? = null
        var size: DoubleOrDoubleArray? = null
        var coordWidth: Double? = null
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = echarts.AxisTypes.value })
                    yAxis = one(unsafeJso<YAxisOption> { type = echarts.AxisTypes.value })
                    series =
                        one(
                            customSeries {
                                typedData = points(doubleArrayOf(0.0, 1.0))
                                renderItem = { params, api ->
                                    val data = CoordinateSystemDataCoord(
                                        arrayOf(OptionDataValue(0.0), OptionDataValue(1.0)),
                                    )
                                    point = api.coord(data)
                                    size =
                                        api.size(
                                            OptionDataValueOrArray(
                                                arrayOf(OptionDataValue(1.0), OptionDataValue(0.0)),
                                            ),
                                        )
                                    coordWidth = params.coordSys.width
                                    unsafeJso<CustomPathOption> {
                                        type = "circle"
                                        shape =
                                            unsafeJso<ZRShapeProps> {
                                                cx = point!![0]
                                                cy = point!![1]
                                                r = 2.0
                                            }
                                    }
                                }
                            },
                        )
                },
            )
        } finally {
            destroyChart(chart, container)
        }

        assertEquals(2, point!!.size)
        assertEquals(2, size!!.unsafeCast<Array<Double>>().size)
        assertTrue(coordWidth!! > 0.0)
    }
}

package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.AxisTypes
import echarts.CustomSeriesRenderItemAPI
import echarts.CustomSeriesRenderItemParams
import echarts.CustomSeriesRenderItemReturn
import echarts.ECharts
import echarts.Orients
import echarts.VisualProps
import echarts.options.AxisMinMax
import echarts.options.CalendarDayLabelOption
import echarts.options.CalendarMonthLabelOption
import echarts.options.CalendarOption
import echarts.options.CalendarRange
import echarts.options.CalendarYearLabelOption
import echarts.options.CallbackDataParams
import echarts.options.CoordinateSystemDataCoord
import echarts.options.CustomDisplayableOptionOnState
import echarts.options.CustomElementOption
import echarts.options.CustomGroupOption
import echarts.options.CustomPathOption
import echarts.options.CustomSeriesFontOption
import echarts.options.CustomSeriesOption
import echarts.options.CustomStyleOption
import echarts.options.CustomTextContentOption
import echarts.options.CustomTextOption
import echarts.options.DataZoomValue
import echarts.options.DimensionDefinition
import echarts.options.DimensionDefinitionLoose
import echarts.options.DimensionLoose
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.OptionDataItem
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.OptionEncodeValue
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.StringOrStringArray
import echarts.options.TextStyleOption
import echarts.options.TitleOption
import echarts.options.ToolboxFeature
import echarts.options.ToolboxFeatureOption
import echarts.options.ToolboxOption
import echarts.options.TooltipContent
import echarts.options.TooltipFormatter
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.customSeries
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ElementTextConfig
import zrender.TextOverflows
import zrender.ZRColor
import zrender.ZRFontWeights
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import zrender.gradientColorStop
import zrender.linearGradient

/*
 * Two custom series pages that build their elements from data rather than from a fixed table:
 * `custom-calendar-icon` paints a few small SVG icons inside every day cell of a calendar, and
 * `flame-graph` turns a stack trace into a block per frame.
 *
 * `custom-calendar-icon` generates its data with [Lcg] instead of `Math.random()`, so every visit
 * draws the same icons. Upstream generates the whole year and shows March; the port generates the
 * March days only, which is everything the `2017-03` range renders.
 *
 * `flame-graph` reads `demo/src/jsMain/resources/data/stack-trace.json` (see the `SOURCE.md` of that
 * folder). Clicking a frame re-roots the graph on it, like the original: the trace is deep-copied,
 * cut down to the clicked node and flattened again.
 */

private val customMore = Category(Groups.CUSTOM, "examples/CustomMore.kt")

/** Edge of the SVG icons of `custom-calendar-icon`, in pixels. */
private const val ICON_EDGE = 16.0

/** Offsets of up to four icons inside a calendar cell; one layout per icon count. */
private val ICON_LAYOUTS: Array<Array<DoubleArray>> =
    arrayOf(
        arrayOf(doubleArrayOf(0.0, 0.0)),
        arrayOf(doubleArrayOf(-0.25, 0.0), doubleArrayOf(0.25, 0.0)),
        arrayOf(doubleArrayOf(0.0, -0.2), doubleArrayOf(-0.2, 0.2), doubleArrayOf(0.2, 0.2)),
        arrayOf(
            doubleArrayOf(-0.25, -0.25),
            doubleArrayOf(-0.25, 0.25),
            doubleArrayOf(0.25, -0.25),
            doubleArrayOf(0.25, 0.25),
        ),
    )

/** The four upstream SVG icons: a cloud, speech bubbles, a photo stack and a heart card. */
private val ICON_PATHS: Array<String> =
    arrayOf(
        "M936.857805 523.431322c0 0-42.065715-68.89513-88.786739-68.89513-46.68416 0-95.732122 " +
            "71.223091-95.732122 71.223091s-44.28544-72.503296-93.440922-71.152538c-35.565466 " +
            "0.977306-62.89705 30.882406-79.124275 64.06615L579.773747 790.800797c-3.253248 " +
            "37.391565-5.677568 50.904371-12.002816 69.63497-6.651802 19.698688-19.544883 " +
            "35.227341-31.650099 45.909606-14.30231 12.621414-29.59831 22.066586-45.854208 " +
            "27.424563-16.28969 5.362074-30.098739 6.496973-51.536794 6.496973-19.498906 " +
            "0-36.95104-2.963456-52.395418-8.850534-15.410586-5.887078-28.420403-14.313984-39.034573" +
            "-25.246003-10.613146-10.930995-18.757939-24.08151-24.435507-39.525171-5.676544-15.443763" +
            "-8.532685-40.195482-8.532685-59.270963l0-26.232454 74.435273 0c0 24.644301-0.17705 " +
            "64.452915 8.81408 77.006848 9.02697 12.515021 22.756147 18.092032 41.148826 " +
            "18.791014 16.728678 0.636518 30.032179-8.061645 30.032179-8.061645s11.922022-10.5472 " +
            "14.992077-19.756954c2.674995-8.025805 3.565363-22.180147 3.565363-22.180147s2.080461" +
            "-21.789286 2.080461-34.234675L489.399906 514.299369c-16.678502-18.827776-43.801395" +
            "-61.938688-82.756096-60.927693-54.699008 1.419366-100.422144 70.059622-100.422144 " +
            "70.059622s-56.065126-70.059622-93.440922-70.059622c-37.376717 0-91.077939 " +
            "70.059622-91.077939 70.059622S105.343488 156.737741 476.742042 119.363584l53.70327" +
            "-74.714624 51.373056 74.714624C964.889395 142.740992 936.857805 523.431322 936.857805 523.431322z",
        "M533.504 268.288q33.792-41.984 71.68-75.776 32.768-27.648 74.24-50.176t86.528-19.456q63.488 " +
            "5.12 105.984 30.208t67.584 63.488 34.304 87.04 6.144 99.84-17.92 97.792-36.864 " +
            "87.04-48.64 74.752-53.248 61.952q-40.96 41.984-85.504 78.336t-84.992 62.464-73.728 " +
            "41.472-51.712 15.36q-20.48 1.024-52.224-14.336t-69.632-41.472-79.872-61.952-82.944" +
            "-75.776q-26.624-25.6-57.344-59.392t-57.856-74.24-46.592-87.552-21.504-100.352 11.264" +
            "-99.84 39.936-83.456 65.536-61.952 88.064-35.328q24.576-5.12 49.152-1.536t48.128 " +
            "12.288 45.056 22.016 40.96 27.648q45.056 33.792 86.016 80.896z",
        "M741.06368 733.310464c8.075264-29.262438 20.615373-40.632422 14.64105-162.810061C966.089728 " +
            "361.789952 967.93897 72.37847 967.855002 54.693683c0.279347-0.279347 0.418509-0.419533 " +
            "0.418509-0.419533s-0.17705-0.00512-0.428749-0.00512c0-0.251699 0-0.428749 " +
            "0-0.428749s-0.139162 0.14633-0.418509 0.425677c-17.695744-0.083866-307.10784 " +
            "1.760051-515.833958 212.142592-122.181632-5.984256-133.55305 6.563533-162.815693 " +
            "14.644531C235.35063 295.798886 103.552614 436.975309 90.630758 486.076621c-12.921856 " +
            "49.105408 39.634227 56.859034 58.579558 58.581197 18.953421 1.724314 121.471386" +
            "-9.475789 130.09111 4.309094 0 0 16.367411 11.200102 17.226035 41.346662 0.850432 " +
            "29.796659 15.173222 71.354163 37.123994 97.267302-0.028672 0.027648-0.05632 " +
            "0.054272-0.083866 0.074752 0.158618 0.13097 0.316211 0.261939 0.474829 0.390861 " +
            "0.129946 0.149402 0.261939 0.319283 0.393011 0.468685 0.019456-0.019456 0.04608" +
            "-0.049152 0.075776-0.075674 25.918362 21.961216 67.477504 36.272128 97.269248 " +
            "37.122458 30.149837 0.859546 41.354547 17.234534 41.354547 17.234534 13.779354 " +
            "8.608051 2.583962 111.122842 4.302131 130.075546 1.727386 18.95168 9.477222 " +
            "71.498445 58.579558 58.576077C585.12896 918.526771 726.311117 786.734182 741.06368 " +
            "733.310464zM595.893555 426.206003c-39.961702-39.965184-39.961702-104.75991 " +
            "0-144.720077 39.970918-39.96928 104.768307-39.96928 144.730112 0 39.970918 " +
            "39.960064 39.970918 104.75479 0 144.720077C700.661862 466.171187 635.864474 " +
            "466.171187 595.893555 426.206003zM358.53312 769.516032c-31.923302-4.573184-54.890394" +
            "-18.410291-71.41847-35.402342-16.984474-16.526438-30.830387-39.495475-35.405824" +
            "-71.420621-4.649062-28.082586-20.856832-41.167565-38.76649-38.763827-17.906586 " +
            "2.40681-77.046886 66.714419-80.857805 89.475891-3.80887 22.752154 29.271859 " +
            "12.081152 46.424166 27.654861 17.151283 15.590093-2.139853 61.93664-14.733107 " +
            "86.845952-6.441984 12.735078-10.289766 26.42176-4.22953 33.76087 7.346586 " +
            "6.070272 21.03593 2.222592 33.769472-4.220109 24.912384-12.585677 71.258829" +
            "-31.872922 86.842368-14.731469 15.583539 17.160806 4.911002 50.229965 27.674419 " +
            "46.419251 22.754099-3.807744 87.065395-62.946611 89.466163-80.85248C399.70857 " +
            "790.374093 386.627072 774.166938 358.53312 769.516032z",
        "M848.794624 939.156685 571.780416 939.156685 571.780416 653.17123l341.897539 0 0 " +
            "221.100654C913.677926 909.960704 884.482867 939.156685 848.794624 939.156685zM571.780403 " +
            "318.743552c-11.861606-3.210138-31.443354-8.36864-39.829709-16.176435-0.596582" +
            "-0.561766-1.016218-1.246413-1.613824-1.841971-0.560845 0.596582-1.016218 " +
            "1.280205-1.613824 1.841971-8.386355 7.807795-15.96631 12.965274-27.827917 " +
            "16.176435l0 263.544325L141.030675 582.287877 141.030675 355.202884c0-35.687834 " +
            "29.195059-64.882688 64.883302-64.882688l150.649125 0c-16.984474-9.525965-32.846438" +
            "-20.56233-46.111027-32.932045-60.250624-56.144691-71.129907-137.062605-24.283034" +
            "-180.767027 19.615539-18.264986 46.252237-27.124736 75.026739-27.124736 39.933133 " +
            "0 83.972915 17.070797 118.995968 49.706086 20.353331 18.983322 37.722624 43.405619 " +
            "50.145075 69.056819 12.457267-25.6512 29.791744-50.074419 50.180915-69.056819 " +
            "35.022029-32.63529 79.062835-49.706086 118.994944-49.706086 28.74071 0 55.410176 " +
            "8.860774 75.025715 27.124736 46.882611 43.704422 35.96759 124.622336-24.283034 " +
            "180.767027-13.264589 12.368691-29.127578 23.40608-46.111027 32.932045l144.649234 " +
            "0c35.688243 0 64.882278 29.195981 64.882278 64.882688l0 227.084948L571.780416 " +
            "582.287833 571.780416 318.743508zM435.064218 147.625267c-21.476966-19.965747-49.094144" +
            "-31.913882-73.868288-31.913882-7.404954 0-21.125018 1.211597-29.863322 9.386803" +
            "-2.000691 1.824563-8.070144 7.439462-8.070144 21.369754 0 15.650406 8.492749 " +
            "40.24873 32.319386 62.477926 29.124506 27.12576 77.202432 47.601152 111.76704 " +
            "47.601152 12.176794 0 16.492237-2.666701 16.527053-2.702541C489.524736 242.54505 " +
            "475.664486 185.453773 435.064218 147.625267zM577.78135 254.790963c0 0 0.034816" +
            "-0.034816 0.069632-0.034816 0.807424 0 5.50871 1.790771 15.509914 1.790771 34.564608 " +
            "0 82.64151-20.47529 111.76704-47.601152 23.826637-22.229299 32.283546-46.810112 " +
            "32.283546-62.442189 0-13.930291-6.033613-19.562496-8.035328-21.404467-8.77312" +
            "-8.17623-22.457344-9.386803-29.864346-9.386803-24.808038 0-52.390298 11.948134" +
            "-73.867264 31.913882C585.325466 185.208218 571.358822 241.73865 577.78135 254.790963z" +
            "M500.89513 939.156685 205.914017 939.156685c-35.688243 0-64.883302-29.195981-64.883302" +
            "-64.883712L141.030714 653.17123l359.864462 0L500.895177 939.15666z",
    )

/** One colour per icon, in the upstream order. */
private val ICON_COLORS: Array<String> = arrayOf("#c4332b", "#16B644", "#6862FD", "#FDC763")

/** The March days of `custom-calendar-icon`: `[date, "icon|icon|..."]`. */
private fun marchIconData(): Array<OptionDataItem?> {
    val random = Lcg(2017)
    return Array(31) { index ->
        val count = kotlin.math.round(random.next() * ICON_PATHS.size).toInt()
        val icons = Array(count) { kotlin.math.round(random.next() * (ICON_PATHS.size - 1)).toInt() }
        OptionDataItem(row("2017-03-" + (index + 1).toString().padStart(2, '0'), icons.joinToString("|")))
    }
}

/** One icon of `custom-calendar-icon`, placed with the offsets of its layout slot. */
private fun calendarIcon(
    path: String,
    color: String,
    x: Double,
    y: Double,
    api: CustomSeriesRenderItemAPI,
): CustomPathOption =
    unsafeJso {
        type = "path"
        shape =
            unsafeJso<ZRShapeProps> {
                pathData = path
                this.x = -ICON_EDGE / 2
                this.y = -ICON_EDGE / 2
                width = ICON_EDGE
                height = ICON_EDGE
            }
        // `position: [x, y]` upstream; zrender folds it into the element position.
        this.x = x
        this.y = y
        style = api.style(unsafeJso<ZRStyleProps> { fill = ZRColor(color) })
    }

/** The `renderItem` of `custom-calendar-icon`: the icons of a day plus its day number. */
private fun calendarIconItem(
    params: CustomSeriesRenderItemParams,
    api: CustomSeriesRenderItemAPI,
): CustomSeriesRenderItemReturn {
    val cellPoint = api.coord(CoordinateSystemDataCoord(arrayOf(api.value(DimensionLoose(0.0)))))
    val cellWidth = params.coordSys.cellWidth ?: 0.0
    val cellHeight = params.coordSys.cellHeight ?: 0.0
    if (cellPoint[0].isNaN() || cellPoint[1].isNaN()) {
        return null
    }

    val raw = textOf(api.value(DimensionLoose(1.0)))
    val icons = if (raw.isEmpty()) emptyArray() else raw.split("|").toTypedArray()
    val layout = ICON_LAYOUTS.getOrNull(icons.size - 1) ?: emptyArray()
    val children = mutableListOf<CustomElementOption>()
    layout.forEachIndexed { index, offset ->
        val icon = icons[index].toInt()
        val x =
            cellPoint[0] +
                ECharts.number.linearMap(offset[0], arrayOf(-0.5, 0.5), arrayOf(-cellWidth / 2, cellWidth / 2))
        val y =
            cellPoint[1] +
                ECharts.number.linearMap(
                    offset[1],
                    arrayOf(-0.5, 0.5),
                    arrayOf(-cellHeight / 2 + 20, cellHeight / 2),
                )
        children += calendarIcon(ICON_PATHS[icon], ICON_COLORS[icon], x, y, api)
    }
    children +=
        unsafeJso<CustomTextOption> {
            type = "text"
            style =
                unsafeJso<ZRStyleProps> {
                    x = cellPoint[0]
                    y = cellPoint[1] - cellHeight / 2 + 15
                    text = ECharts.format.formatTime("dd", api.value(DimensionLoose(0.0))!!)
                    fill = ZRColor("#777")
                    font = api.font(unsafeJso<CustomSeriesFontOption> { fontSize = StringOrDouble(14.0) })
                }
        }
    return unsafeJso<CustomGroupOption> {
        type = "group"
        this.children = children.toTypedArray()
    }
}

/** One node of `stack-trace.json`. */
private external interface StackTraceNode {
    /** Id of the frame; it is also the data item name the click handler reads back. */
    var id: String

    /** Function name of the frame. */
    var name: String

    /** Number of samples taken in this frame and below it. */
    var value: Double

    /** Callees of the frame. */
    var children: Array<StackTraceNode>?
}

/** Colour of one frame, keyed by the module prefix of its function name. */
private val FLAME_COLORS: Map<String, String> =
    mapOf(
        "root" to "#8fd3e8",
        "genunix" to "#d95850",
        "unix" to "#eb8146",
        "ufs" to "#ffb248",
        "FSS" to "#f2d643",
        "namefs" to "#ebdba4",
        "doorfs" to "#fcce10",
        "lofs" to "#b5c334",
        "zfs" to "#1bca93",
    )

/** A deep copy of [node]: [filterTrace] rewrites the children of the copy, not of the source. */
private fun copyTrace(node: StackTraceNode): StackTraceNode =
    unsafeJso {
        id = node.id
        name = node.name
        value = node.value
        children = node.children?.let { kids -> Array(kids.size) { copyTrace(kids[it]) } }
    }

/**
 * The copy of [node] rooted at the frame [id], or [node] itself when the id is unknown.
 *
 * The path down to the match keeps only the matching branch; every node on it takes the value of the
 * match, the way upstream narrows the graph.
 */
private fun filterTrace(node: StackTraceNode, id: String): StackTraceNode = findTrace(node, id) ?: node

/** The subtree rooted at [id] inside [node], with [node] rewritten to hold only that branch. */
private fun findTrace(node: StackTraceNode, id: String): StackTraceNode? {
    if (node.id == id) {
        return node
    }
    for (child in node.children ?: emptyArray()) {
        val found = findTrace(child, id)
        if (found != null) {
            node.children = arrayOf(found)
            node.value = found.value
            return node
        }
    }
    return null
}

/** One flattened frame: `[level, start, end, name, percentage]` plus its colour. */
private external interface FlameFrame {
    /** Id of the frame. */
    var name: String

    /** `[level, start, end, name, percentage]`. */
    var value: Array<OptionDataValue?>

    /** Colour of the frame. */
    var itemStyle: ItemStyleOption
}

/** Flattens the trace into one rectangle per frame, in pre-order. */
private fun flameData(root: StackTraceNode): Array<OptionDataItem?> {
    val frames = mutableListOf<OptionDataItem?>()
    val rootValue = root.value

    fun recur(node: StackTraceNode, start: Double, level: Double) {
        frames +=
            unsafeJso<FlameFrame> {
                name = node.id
                value =
                    arrayOf(
                        OptionDataValue(level),
                        OptionDataValue(start),
                        OptionDataValue(start + node.value),
                        OptionDataValue(node.name),
                        OptionDataValue(node.value / rootValue * 100),
                    )
                itemStyle =
                    unsafeJso<ItemStyleOption> {
                        color = ZRColor(FLAME_COLORS[node.name.substringBefore(' ')] ?: "#ccc")
                    }
            }.unsafeCast<OptionDataItem>()
        var next = start
        for (child in node.children ?: emptyArray()) {
            recur(child, next, level + 1)
            next += child.value
        }
    }

    recur(root, 0.0, 0.0)
    return frames.toTypedArray()
}

/** Depth of the trace, the `yAxis.max` of `flame-graph`. */
private fun traceDepth(node: StackTraceNode, level: Double = 0.0): Double {
    val children = node.children
    if (children.isNullOrEmpty()) {
        return level
    }
    var maxLevel = level
    for (child in children) {
        maxLevel = kotlin.math.max(maxLevel, traceDepth(child, level + 1))
    }
    return maxLevel
}

/** The `renderItem` of `flame-graph`: one rounded block per frame, the name clipped inside it. */
private fun flameItem(
    params: CustomSeriesRenderItemParams,
    api: CustomSeriesRenderItemAPI,
): CustomSeriesRenderItemReturn {
    val level = api.value(DimensionLoose(0.0))
    val start = api.coord(CoordinateSystemDataCoord(arrayOf(api.value(DimensionLoose(1.0)), level)))
    val end = api.coord(CoordinateSystemDataCoord(arrayOf(api.value(DimensionLoose(2.0)), level)))
    val height =
        numberArray(
            api.size(OptionDataValueOrArray(arrayOf(OptionDataValue(0.0), OptionDataValue(1.0)))),
        )[1]
    val width = end[0] - start[0]

    return unsafeJso<CustomPathOption> {
        type = "rect"
        transition = StringOrStringArray(arrayOf("shape"))
        shape =
            unsafeJso<ZRShapeProps> {
                x = start[0]
                y = start[1] - height / 2
                this.width = width
                this.height = height - 2
                r = 2.0
            }
        style = unsafeJso<ZRStyleProps> { fill = api.visual(VisualProps.color).unsafeCast<ZRColor>() }
        emphasis =
            unsafeJso<CustomDisplayableOptionOnState> {
                style = CustomStyleOption(unsafeJso<ZRStyleProps> { stroke = ZRColor("#000") })
            }
        textConfig = unsafeJso<ElementTextConfig> { position = StringOrDoubleOrArray("insideLeft") }
        textContent =
            CustomTextContentOption(
                unsafeJso<CustomTextOption> {
                    style =
                        unsafeJso<ZRStyleProps> {
                            text = textOf(api.value(DimensionLoose(3.0)))
                            fontFamily = "Verdana"
                            fill = ZRColor("#000")
                            this.width = StringOrDouble(width - 4)
                            overflow = TextOverflows.truncate
                            ellipsis = ".."
                            truncateMinChar = 1.0
                        }
                    emphasis =
                        unsafeJso<CustomDisplayableOptionOnState> {
                            style =
                                CustomStyleOption(
                                    unsafeJso<ZRStyleProps> {
                                        stroke = ZRColor("#000")
                                        lineWidth = 0.5
                                    },
                                )
                        }
                },
            )
    }
}

/** `[marker] name: (N samples, P%)`, the tooltip of `flame-graph`. */
private fun flameTooltip(params: Any?): TooltipContent {
    val item = params.unsafeCast<CallbackDataParams>()
    val value = item.value
    val samples = numberAt(value, 2) - numberAt(value, 1)
    return TooltipContent(
        textOf(item.marker) + " " + textAt(value, 3) + ": (" +
            ECharts.format.addCommas(StringOrDouble(samples)) + " samples, " +
            textOf(numeric(fixed(numberAt(value, 4), 2))) + "%)",
    )
}

/** The option of `flame-graph` for the trace [root]. */
private fun flameOption(root: StackTraceNode): EChartsOption =
    unsafeJso {
        backgroundColor =
            linearGradient(
                0.0,
                0.0,
                0.0,
                1.0,
                arrayOf(gradientColorStop(0.05, "#eee"), gradientColorStop(0.95, "#eeeeb0")),
            )
        tooltip = one(unsafeJso<TooltipOption> { formatter = TooltipFormatter { params -> flameTooltip(params) } })
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "Flame Graph"
                    left = StringOrDouble("center")
                    top = StringOrDouble(10.0)
                    textStyle =
                        unsafeJso<TextStyleOption> {
                            fontFamily = "Verdana"
                            fontWeight = ZRFontWeights.normal
                            fontSize = StringOrDouble(20.0)
                        }
                },
            )
        toolbox =
            one(
                unsafeJso<ToolboxOption> {
                    right = StringOrDouble(20.0)
                    top = StringOrDouble(10.0)
                    feature = unsafeJso<ToolboxFeature> { restore = unsafeJso<ToolboxFeatureOption> {} }
                },
            )
        xAxis = one(unsafeJso<XAxisOption> { show = false })
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    show = false
                    max = AxisMinMax(traceDepth(root))
                },
            )
        series =
            one<SeriesOption>(
                customSeries {
                    renderItem = { itemParams, api -> flameItem(itemParams, api) }
                    encode =
                        unsafeJso {
                            x =
                                OptionEncodeValue(
                                    arrayOf(DimensionLoose(0.0), DimensionLoose(1.0), DimensionLoose(2.0)),
                                )
                            y = OptionEncodeValue(DimensionLoose(0.0))
                        }
                    typedData = flameData(root)
                },
            )
    }

val customMoreEntries: List<CatalogEntry> =
    listOf(
        customMore.chart(
            "custom-calendar-icon",
            "日历图自定义图标",
            "Custom Calendar Icon",
            summaryZh = "日历的每个格子按当天的“事件数”摆放 1～4 个 SVG 图标，图标位置来自固定的布局表。",
            summaryEn = "Each calendar cell places one to four SVG icons according to the event count of that day, at the offsets of a fixed layout table.",
        ) {
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                calendar =
                    one(
                        unsafeJso<CalendarOption> {
                            left = StringOrDouble("center")
                            top = StringOrDouble("middle")
                            cellSize =
                                StringOrDoubleOrArray(
                                    arrayOf(StringOrDouble(70.0), StringOrDouble(70.0)),
                                )
                            yearLabel = unsafeJso<CalendarYearLabelOption> { show = false }
                            orient = Orients.vertical
                            dayLabel =
                                unsafeJso<CalendarDayLabelOption> {
                                    firstDay = 1.0
                                    nameMap = StringOrStringArray("cn")
                                }
                            monthLabel = unsafeJso<CalendarMonthLabelOption> { show = false }
                            range = CalendarRange(DataZoomValue("2017-03"))
                        },
                    )
                series =
                    one<SeriesOption>(
                        customSeries {
                            coordinateSystem = "calendar"
                            renderItem = { itemParams, api -> calendarIconItem(itemParams, api) }
                            dimensions =
                                arrayOf<DimensionDefinitionLoose?>(
                                    null,
                                    DimensionDefinitionLoose(unsafeJso<DimensionDefinition> { type = "ordinal" }),
                                ).unsafeCast<Array<DimensionDefinitionLoose>>()
                            typedData = marchIconData()
                        },
                    )
            }
        },
        customMore.interactive(
            "flame-graph",
            "火焰图",
            "Flame graph",
            summaryZh = "堆栈采样数据按层级铺成火焰图；点击某个格子会以该帧为根重新展开。",
            summaryEn = "A sampled stack trace laid out as a flame graph; clicking a frame re-roots the graph on it.",
        ) { chart ->
            loadJson("data/stack-trace.json").then { json ->
                val root = json.unsafeCast<StackTraceNode>()
                chart.setOption(flameOption(root))
                chart.on("click") { event ->
                    val data = flameData(filterTrace(copyTrace(root), event.name))
                    val rootValue = numberAt(data[0]!!.unsafeCast<FlameFrame>().value, 2)
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            xAxis = one(unsafeJso<XAxisOption> { max = AxisMinMax(rootValue) })
                            series = one<SeriesOption>(unsafeJso<CustomSeriesOption> { typedData = data })
                        },
                    )
                }
                null
            }
        },
    )

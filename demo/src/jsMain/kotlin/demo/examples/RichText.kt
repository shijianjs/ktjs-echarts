package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import zrender.ZRColor
import zrender.ImageLike
import zrender.ImagePatternObject
import zrender.PatternObject

/*
 * The two rich-text pages of the gallery: `bar-rich-text` (weather statistics with an icon per row of
 * the category axis) and `pie-rich-text` (a table drawn inside the label of one slice).
 *
 * Both originals point `backgroundColor.image` at PNG icons of the examples site; the three 128px
 * weather icons are shipped under `demo/src/jsMain/resources/data/img/weather/` (see the `SOURCE.md`
 * of that folder), so the labels build an [ImagePatternObject] around an `Image` of the same file and
 * the cell that shows it declares the icon width: a rich-text tag with no text of its own would
 * otherwise fill a zero-width box.
 *
 * `pie-rich-text` also uses `width: '100%'` inside its rich text, which the wrapper keeps as the
 * upstream string; the `{Sunny|}`-style keys are plain rich-text tags.
 */

private val barRichText = Category(Groups.BAR, "examples/RichText.kt")

private val pieRichText = Category(Groups.PIE, "examples/RichText.kt")

/** Edge of the square weather icons, the 128px files of the examples site. */
private const val ICON_SIZE = 128.0

/** Width of the icon cell of the two label tables; narrower than the icon so the table keeps its shape. */
private const val ICON_CELL = 44.0

/** Icon URL of one weather row, the `ROOT_PATH + '/data/asset/img/weather/xxx_128.png'` of upstream. */
private fun weatherIcon(name: String): String = "data/img/weather/" + name.lowercase() + "_128.png"

/** `backgroundColor` of a rich-text block: an image pattern of [weatherIcon]. */
internal fun iconBackground(name: String): PatternObject =
    PatternObject(
        unsafeJso<ImagePatternObject> {
            image =
                ImageLike(
                    js("(url) => { const img = new Image(); img.src = url; return img; }")(weatherIcon(name))
                        as web.html.HTMLImageElement,
                )
            repeat = "no-repeat"
            imageWidth = ICON_SIZE
            imageHeight = ICON_SIZE
        },
    )

val richTextEntries: List<CatalogEntry> =
    listOf(
        barRichText.chart("bar-rich-text", "天气统计（富文本）", "Weather Statistics") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Weather Statistics" })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("City Alpha", "City Beta", "City Gamma")
                        },
                    )
                grid = one(unsafeJso<GridOption> { left = StringOrDouble(100.0) })
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            show = true
                            feature = unsafeJso<ToolboxFeature> { saveAsImage = unsafeJso<SaveAsImageOption> {} }
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            name = "Days"
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value}") }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            inverse = true
                            data = strings("Sunny", "Cloudy", "Showers")
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    formatter =
                                        LabelFormatter(
                                            js("(value) => '{' + value + '| }' + String.fromCharCode(10) + '{value|' + value + '}'")
                                                as LabelFormatterCallback,
                                        )
                                    margin = 20.0
                                    rich =
                                        richOf(
                                            "value" to unsafeJso<TextStyleOption> {
                                                lineHeight = 30.0
                                                align = TextAlign.center
                                            },
                                            "Sunny" to iconRow("Sunny"),
                                            "Cloudy" to iconRow("Cloudy"),
                                            "Showers" to iconRow("Showers"),
                                        )
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "City Alpha"
                            typedData = numbers(165.0, 170.0, 30.0)
                            label = unsafeJso<LabelOption> { show = true }
                            markPoint =
                                unsafeJso<MarkPointOption> {
                                    symbolSize = SymbolSize(1.0)
                                    symbolOffset = SymbolOffset(arrayOf<StringOrDouble>(StringOrDouble(0.0), StringOrDouble("50%")))
                                    label =
                                        unsafeJso<LabelOption> {
                                            formatter = LabelFormatter("{a|{a}\n}{b|{b} }{c|{c}}")
                                            backgroundColor = ZRColor("rgb(242,242,242)")
                                            borderColor = "#aaa"
                                            borderWidth = 1.0
                                            borderRadius = DoubleOrDoubleArray(4.0)
                                            padding = DoubleOrDoubleArray(arrayOf(4.0, 10.0))
                                            lineHeight = 26.0
                                            position = LabelPosition("right")
                                            distance = 20.0
                                            rich =
                                                richOf(
                                                    "a" to unsafeJso<TextStyleOption> {
                                                        align = TextAlign.center
                                                        color = "#fff"
                                                        fontSize = StringOrDouble(18.0)
                                                        textShadowBlur = 2.0
                                                        textShadowColor = "#000"
                                                        textShadowOffsetX = 0.0
                                                        textShadowOffsetY = 1.0
                                                        textBorderColor = "#333"
                                                        textBorderWidth = 2.0
                                                    },
                                                    "b" to unsafeJso<TextStyleOption> { color = "#333" },
                                                    "c" to unsafeJso<TextStyleOption> {
                                                        color = "#ff8811"
                                                        textBorderColor = "#000"
                                                        textBorderWidth = 1.0
                                                        fontSize = StringOrDouble(22.0)
                                                    },
                                                )
                                        }
                                    data =
                                        arrayOf(
                                            unsafeJso<MarkPointDataItemOption> {
                                                type = MarkerStatisticTypes.max
                                                name = "max days: "
                                            },
                                            unsafeJso<MarkPointDataItemOption> {
                                                type = MarkerStatisticTypes.min
                                                name = "min days: "
                                            },
                                        )
                                }
                        },
                        barSeries {
                            name = "City Beta"
                            label = unsafeJso<LabelOption> { show = true }
                            typedData = numbers(150.0, 105.0, 110.0)
                        },
                        barSeries {
                            name = "City Gamma"
                            label = unsafeJso<LabelOption> { show = true }
                            typedData = numbers(220.0, 82.0, 63.0)
                        },
                    )
            }
        },
        pieRichText.chart("pie-rich-text", "富文本标签", "Pie Special Label") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Weather Statistics"
                            subtext = "Fake Data"
                            left = StringOrDouble("center")
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            formatter = TooltipFormatter("{a} <br/>{b} : {c} ({d}%)")
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            bottom = StringOrDouble(10.0)
                            left = StringOrDouble("center")
                            data = legendNames("CityA", "CityB", "CityD", "CityC", "CityE")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            radius = StringOrDoubleOrArray("65%")
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                            selectedMode = SelectionModes.single
                            typedData =
                                arrayOf(
                                    pieItemWithLabel("CityE", 1548.0, richTableLabel()),
                                    pieItem("CityC", 735.0),
                                    pieItem("CityD", 510.0),
                                    pieItem("CityB", 434.0),
                                    pieItem("CityA", 335.0),
                                )
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    itemStyle =
                                        unsafeJso<ItemStyleOption> {
                                            shadowBlur = 10.0
                                            shadowOffsetX = 0.0
                                            shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                        }
                                }
                        },
                    )
            }
        },
    )

/** A rich-text block with the weather icon of [name] as its background. */
private fun iconRow(name: String): TextStyleOption =
    unsafeJso {
        height = 40.0
        align = TextAlign.center
        backgroundColor = ZRColor(iconBackground(name))
        // The rich-text block is as wide as its (empty) text, so the icon needs the width the 128px
        // file asks for; without it the pattern is drawn into a zero-width box.
        width = StringOrDouble(ICON_SIZE)
    }

/** A `{ name: style }` record, the shape of `rich`. */
internal fun richOf(vararg pairs: Pair<String, TextStyleOption>): Record<String, TextStyleOption> =
    js("(keys, values) => { const o = {}; for (let i = 0; i < keys.length; i++) { o[keys[i]] = values[i]; } return o; }")(
        Array(pairs.size) { pairs[it].first },
        Array(pairs.size) { pairs[it].second },
    ) as Record<String, TextStyleOption>

/** One pie item with a custom label, used by `pie-rich-text`. */
private fun pieItemWithLabel(name: String, value: Double, label: PieLabelOption): PieDataItem =
    PieDataItem(
        unsafeJso<PieDataItemOption> {
            this.name = OptionId(name)
            this.value = OptionDataValue(value)
            this.label = label
        },
    )

/** The label of the `CityE` slice: a three row table of weather icons and numbers. */
private fun richTableLabel(): PieLabelOption =
    unsafeJso {
        formatter =
            LabelFormatter(
                "{title|{b}}{abg|}\n" +
                    "  {weatherHead|Weather}{valueHead|Days}{rateHead|Percent}\n" +
                    "{hr|}\n" +
                    "  {Sunny|}{value|202}{rate|55.3%}\n" +
                    "  {Cloudy|}{value|142}{rate|38.9%}\n" +
                    "  {Showers|}{value|21}{rate|5.8%}",
            )
        backgroundColor = ZRColor("#eee")
        borderColor = "#777"
        borderWidth = 1.0
        borderRadius = DoubleOrDoubleArray(4.0)
        rich =
            richOf(
                "title" to unsafeJso<TextStyleOption> { color = "#eee"; align = TextAlign.center },
                "abg" to unsafeJso<TextStyleOption> {
                    backgroundColor = ZRColor("#333")
                    width = StringOrDouble("100%")
                    align = TextAlign.right
                    height = 25.0
                    borderRadius = DoubleOrDoubleArray(arrayOf(4.0, 4.0, 0.0, 0.0))
                },
                "Sunny" to tableRow("Sunny"),
                "Cloudy" to tableRow("Cloudy"),
                "Showers" to tableRow("Showers"),
                "weatherHead" to unsafeJso<TextStyleOption> {
                    color = "#333"
                    height = 24.0
                    align = TextAlign.left
                },
                "hr" to unsafeJso<TextStyleOption> {
                    borderColor = "#777"
                    width = StringOrDouble("100%")
                    borderWidth = 0.5
                    height = 0.0
                },
                "value" to unsafeJso<TextStyleOption> {
                    width = StringOrDouble(20.0)
                    padding = DoubleOrDoubleArray(arrayOf(0.0, 20.0, 0.0, 30.0))
                    align = TextAlign.left
                },
                "valueHead" to unsafeJso<TextStyleOption> {
                    color = "#333"
                    width = StringOrDouble(20.0)
                    padding = DoubleOrDoubleArray(arrayOf(0.0, 20.0, 0.0, 30.0))
                    align = TextAlign.center
                },
                "rate" to unsafeJso<TextStyleOption> {
                    width = StringOrDouble(40.0)
                    align = TextAlign.right
                    padding = DoubleOrDoubleArray(arrayOf(0.0, 10.0, 0.0, 0.0))
                },
                "rateHead" to unsafeJso<TextStyleOption> {
                    color = "#333"
                    width = StringOrDouble(40.0)
                    align = TextAlign.center
                    padding = DoubleOrDoubleArray(arrayOf(0.0, 10.0, 0.0, 0.0))
                },
            )
    }

/** One icon cell of [richTableLabel]. */
private fun tableRow(name: String): TextStyleOption =
    unsafeJso {
        height = 30.0
        align = TextAlign.left
        backgroundColor = ZRColor(iconBackground(name))
        // An empty rich-text tag has no width of its own, so the icon cell has to declare one.
        width = StringOrDouble(ICON_CELL)
    }

package demo.examples.shared

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.JsAny
import web.cssom.TextAlign
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ImageLike
import io.github.shijianjs.ktjs.zrender.ImagePatternObject
import io.github.shijianjs.ktjs.zrender.PatternObject
import demo.examples.gauge.center
import demo.examples.gl.square
import demo.examples.matrix.cell

/** Edge of the square weather icons, the 128px files of the examples site. */
internal const val ICON_SIZE = 128.0

/** Width of the icon cell of the two label tables; narrower than the icon so the table keeps its shape. */
internal const val ICON_CELL = 44.0

/** Icon URL of one weather row, the `ROOT_PATH + '/data/asset/img/weather/xxx_128.png'` of upstream. */
internal fun weatherIcon(name: String): String = "data/img/weather/" + name.lowercase() + "_128.png"

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

/** A rich-text block with the weather icon of [name] as its background. */
internal fun iconRow(name: String): TextStyleOption =
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
internal fun pieItemWithLabel(name: String, value: Double, label: PieLabelOption): PieDataItem =
    PieDataItem(
        unsafeJso<PieDataItemOption> {
            this.name = OptionId(name)
            this.value = OptionDataValue(value)
            this.label = label
        },
    )

/** The label of the `CityE` slice: a three row table of weather icons and numbers. */
internal fun richTableLabel(): PieLabelOption =
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
internal fun tableRow(name: String): TextStyleOption =
    unsafeJso {
        height = 30.0
        align = TextAlign.left
        backgroundColor = ZRColor(iconBackground(name))
        // An empty rich-text tag has no width of its own, so the icon cell has to declare one.
        width = StringOrDouble(ICON_CELL)
    }

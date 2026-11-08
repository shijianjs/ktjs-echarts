package demo.examples.pie

import io.github.shijianjs.ktjs.echarts.Payload
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.PieDataItem
import io.github.shijianjs.ktjs.echarts.options.PieLabelOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.TooltipFormatter
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.pieSeries
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.ceil
import kotlin.math.round
import io.github.shijianjs.ktjs.zrender.ZRFontWeights
import demo.examples.gauge.center
import demo.examples.matrix.names
import demo.examples.shared.Lcg
import demo.examples.shared.legendNames
import demo.examples.shared.pieItem
import demo.examples.shared.row
import demo.examples.sunburst.radius

/** The `{ name, value }` set shared by the doughnut-family examples. */
internal fun accessFrom(): Array<PieDataItem?> =
    dataItems(
        pieItem("Search Engine", 1048.0),
        pieItem("Direct", 735.0),
        pieItem("Email", 580.0),
        pieItem("Union Ads", 484.0),
        pieItem("Video Ads", 300.0),
    )

/** Inner ring of `pie-nest`. */
internal fun nestInner(): Array<PieDataItem?> =
    dataItems(pieItem("Search Engine", 1548.0), pieItem("Direct", 775.0), pieItem("Marketing", 679.0))

/** Outer ring of `pie-nest`. */
internal fun nestOuter(): Array<PieDataItem?> =
    dataItems(
        pieItem("Baidu", 1048.0),
        pieItem("Direct", 335.0),
        pieItem("Email", 310.0),
        pieItem("Google", 251.0),
        pieItem("Union Ads", 234.0),
        pieItem("Bing", 147.0),
        pieItem("Video Ads", 135.0),
        pieItem("Others", 102.0),
    )

/** Eight rose values, labelled `rose 1` .. `rose 8`. */
internal fun roseData(vararg values: Double): Array<PieDataItem?> =
    dataItems(*Array(values.size) { index -> pieItem("rose ${index + 1}", values[index]) })

/** Legend entries named after [names]. */
internal fun legendOf(vararg names: String): Array<LegendOption> = one(unsafeJso<LegendOption> { data = legendNames(*names) })

internal val tooltipItem = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })

internal val centerLegend =
    one(
        unsafeJso<LegendOption> {
            top = StringOrDouble("5%")
            left = StringOrDouble("center")
        },
    )

internal val doughnutRadius = StringOrDoubleOrArray(arrayOf(StringOrDouble("40%"), StringOrDouble("70%")))

/** The surname pool of `pie-legend`, copied from the upstream `nameList`. */
internal val SURNAME_POOL =
    arrayOf(
        "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨",
        "朱", "秦", "尤", "许", "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜",
        "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
        "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐",
        "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常",
        "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄",
        "和", "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧",
        "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒", "屈", "项", "祝", "董", "梁",
        "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危",
    )

/**
 * The generated surnames of `pie-legend`.
 *
 * Upstream builds each name and value with `Math.random()`; one [Lcg] run produces both, so a visit
 * always shows the same 50 names. Each round draws the name first and then its value, like the
 * upstream loop.
 */
internal fun surnames(count: Int): List<Pair<String, Double>> {
    val random = Lcg(20261028)
    return List(count) {
        val word = surnameWord(random, 2, 1)
        val name = if (random.next() > 0.65) "$word·${surnameWord(random, 3, 0)}" else word
        name to round(random.next() * 100000.0)
    }
}

/** `makeWord(max, min)`: between `min + 1` and `min + max` surnames joined together. */
internal fun surnameWord(
    random: Lcg,
    max: Int,
    min: Int,
): String =
    buildString {
        repeat(ceil(random.next() * max + min).toInt()) {
            // Upstream rounds `random() * length - 1`, which can index -1; the floor form stays in range.
            append(SURNAME_POOL[(random.next() * SURNAME_POOL.size).toInt()])
        }
    }

/** One shelf of `pie-labelLine-adjust`: a row of books with their reading hours. */
internal fun bookShelf(vararg entries: Pair<String, Double>): Array<PieDataItem?> =
    dataItems(*Array(entries.size) { index -> pieItem(entries[index].first, entries[index].second) })

/** The three shelves of `pie-labelLine-adjust`. */
internal fun bookShelves(): Array<Array<PieDataItem?>> =
    arrayOf(
        bookShelf(
            "圣彼得堡来客" to 5.6,
            "陀思妥耶夫斯基全集" to 1.0,
            "史记精注全译（全6册）" to 0.8,
            "加德纳艺术通史" to 0.5,
            "表象与本质" to 0.5,
            "其它" to 3.8,
        ),
        bookShelf(
            "银河帝国5：迈向基地" to 3.8,
            "俞军产品方法论" to 2.3,
            "艺术的逃难" to 2.2,
            "第一次世界大战回忆录（全五卷）" to 1.3,
            "Scrum 精髓" to 1.2,
            "其它" to 5.7,
        ),
        bookShelf(
            "克莱因壶" to 3.5,
            "投资最重要的事" to 2.8,
            "简读中国史" to 1.7,
            "你当像鸟飞往你的山" to 1.4,
            "表象与本质" to 0.5,
            "其它" to 3.8,
        ),
    )

/** The hover label of the doughnut family: a large percentage in the middle of the ring. */

/** Action payload that targets one data item of the first series. */
internal external interface SeriesActionPayload {
    var type: String?
    var seriesIndex: Double
    var dataIndex: Double
}

/** Builds a `dispatchAction` payload for the given series-0 data index. */
internal fun seriesAction(actionType: String, index: Int): Payload =
    unsafeJso<SeriesActionPayload> { type = actionType; seriesIndex = 0.0; dataIndex = index.toDouble() }
        .unsafeCast<Payload>()

internal fun centerLabelEmphasis(): EmphasisOption =
    unsafeJso {
        label =
            unsafeJso<LabelOption> {
                show = true
                fontSize = StringOrDouble(40.0)
                fontWeight = ZRFontWeights.bold
            }
    }

/** `{a} <br/>{b} : {c} ({d}%)`, the tooltip of the rose and nested pies. */
internal fun tooltipWithPercent(): TooltipOption =
    unsafeJso {
        trigger = TooltipTriggers.item
        formatter = TooltipFormatter("{a} <br/>{b} : {c} ({d}%)")
    }

/** One of the three widths of `pie-alignTo`. */
internal fun pieAlignToSeries(
    data: Array<PieDataItem?>,
    alignTo: io.github.shijianjs.ktjs.echarts.options.PieLabelAlignTo,
    left: StringOrDouble,
    right: StringOrDouble,
    bleedMargin: Double? = null,
    margin: Double? = null,
): SeriesOption =
    pieSeries {
        radius = StringOrDoubleOrArray("25%")
        center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
        typedData = data
        typedLabel =
            unsafeJso<PieLabelOption> {
                position = LabelPosition("outer")
                this.alignTo = alignTo
                this.bleedMargin = bleedMargin
                this.margin = margin?.let { StringOrDouble(it) }
            }
        this.left = left
        this.right = right
        top = StringOrDouble(0.0)
        bottom = StringOrDouble(0.0)
    }

package demo.json.examples.pie

import demo.json.namedValues
import io.github.shijianjs.kmpechartsjson.*

/** The `{ name, value }` set shared by the doughnut family. */
internal val ACCESS_FROM =
    namedValues(
        "Search Engine" to 1048,
        "Direct" to 735,
        "Email" to 580,
        "Union Ads" to 484,
        "Video Ads" to 300,
    )

/** Legend entries named after [names]. */
internal fun legendOf(vararg names: String): List<String> = names.toList()

/** Eight rose values, labelled `rose 1` .. `rose 8`. */
internal fun roseData(vararg values: Number): List<io.github.shijianjs.kmpechartsjson.DataValue> =
    namedValues(*Array(values.size) { index -> "rose ${index + 1}" to values[index] })

/** Label of the doughnut family: the hovered percentage in the middle of the ring. */
internal fun centerEmphasis(): io.github.shijianjs.kmpechartsjson.EmphasisOption.() -> Unit = {
    label {
        show = true
        fontSize = 40.0
        fontWeight = str("bold")
    }
}

internal val doughnutRadius = NumberOrStringOrNumbers.of(str("40%"), str("70%"))

/** `TextStyleOption` alias: the rich styles above are plain text styles. */
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
 * Upstream draws names and values with `Math.random()`; a fixed linear congruential sequence keeps
 * the page reproducible while producing the same shape of data.
 */
internal fun surnames(count: Int): List<Pair<String, Double>> {
    var state = 20261028L
    fun next(): Double {
        state = state * 1103515245L + 12345L
        return ((state ushr 16) and 0x7FFF).toDouble() / 0x8000
    }
    return List(count) {
        val word =
            buildString {
                repeat((next() * 2 + 1).toInt()) { append(SURNAME_POOL[(next() * SURNAME_POOL.size).toInt()]) }
            }
        val name = if (next() > 0.65) "$word·${SURNAME_POOL[(next() * SURNAME_POOL.size).toInt()]}" else word
        name to (next() * 100000).toLong().toDouble()
    }
}

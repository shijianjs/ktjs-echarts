package demo.examples.line

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.math.round
import demo.examples.shared.Lcg

internal class DynamicDataWindow(seed: Int) {
    private val random = Lcg(seed)
    private var time = Date(1997, 9, 3).getTime()
    private var walkingValue = random.next() * 1000
    private val items = MutableList(ITEM_COUNT) { nextItem() }

    /** Drops the [count] oldest points and appends [count] new ones. */
    fun advance(count: Int) {
        repeat(count) {
            items.removeAt(0)
            items.add(nextItem())
        }
    }

    /** The window as series data, rebuilt so `setOption` sees a fresh array. */
    fun data(): Array<LineDataItem?> = items.toTypedArray()

    private fun nextItem(): LineDataItem {
        time += ONE_DAY
        walkingValue += random.next() * 21 - 10
        val now = Date(time)
        val label = "${now.getFullYear()}/${now.getMonth() + 1}/${now.getDate()}"
        val rounded = round(walkingValue)
        return LineDataItem(
            unsafeJso<LineDataItemOption> {
                name = OptionId(now.toString())
                typedValue = OptionDataValueOrArray(arrayOf(OptionDataValue(label), OptionDataValue(rounded)))
            },
        )
    }

    private companion object {
        /** Number of points of the window; the original keeps a thousand. */
        const val ITEM_COUNT = 1000
    }
}

package demo.examples.bar

import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import demo.examples.shared.row

/** One level of the drilldown, identified by the `groupId` shared by its rows. */
internal class DrillLevel(val id: String, val rows: List<DrillRow>) {
    /** The data row at [index] as `[name, value, groupId, childGroupId?]`. */
    fun columns(index: Int): Array<OptionDataValue?> {
        val item = rows[index]
        return if (item.child == null) {
            row(item.name, item.value, id)
        } else {
            row(item.name, item.value, id, item.child)
        }
    }
}

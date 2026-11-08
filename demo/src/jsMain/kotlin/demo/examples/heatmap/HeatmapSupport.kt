package demo.examples.heatmap

import io.github.shijianjs.ktjs.echarts.options.HeatmapDataItem
import io.github.shijianjs.ktjs.echarts.options.dataItems
import demo.examples.custom.punchCardValues
import demo.examples.shared.every
import demo.examples.shared.row

/** The original maps every row to `[hour, day, value || "-"]`. */
internal fun punchCard(): Array<HeatmapDataItem?> =
    dataItems(
        *punchCardValues
            .flatMapIndexed { day, values ->
                values.mapIndexed { hour, value ->
                    HeatmapDataItem(row(hour.toDouble(), day.toDouble(), if (value > 0) value else "-"))
                }
            }.toTypedArray(),
    )

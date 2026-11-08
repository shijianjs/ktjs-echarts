package demo.json

import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * One page of the "KMP JSON model" group.
 *
 * Everything except the DOM lives here: the bilingual copy, the model option and the Kotlin source
 * shown by the code panel. The JS side only performs the presentation — it feeds [json] to
 * `setOption` and prints [json] or [kotlinSource] in the code panel.
 */
class ModelExample(
    val id: String,
    val titleZh: String,
    val titleEn: String,
    val summaryZh: String,
    val summaryEn: String,
    /** The exact builder shown in Kotlin mode. */
    val kotlinSource: String,
    val option: EChartsOption,
) {
    /** Pretty JSON of [option]: the payload the chart is actually created from. */
    val json: String = option.toJsonString(prettyPrint = true)
}

private fun lineSmoothOption(): EChartsOption =
    echartsOption {
        title { text = "Weekly sales" }
        tooltip { trigger = TooltipTrigger.Axis }
        xAxis {
            type = AxisType.Category
            categories("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            boundaryGap = BoundaryGap.of(false)
        }
        yAxis { type = AxisType.Value }
        series {
            line {
                name = "Sales"
                smooth = BooleanOrNumber.of(true)
                values(120, 200, 150, 80, 70, 110, 130)
            }
        }
    }

private fun barStackOption(): EChartsOption =
    echartsOption {
        tooltip { trigger = TooltipTrigger.Axis }
        legend { entries("Direct", "Email") }
        xAxis {
            type = AxisType.Category
            categories("Mon", "Tue", "Wed", "Thu", "Fri")
        }
        yAxis { type = AxisType.Value }
        series {
            bar {
                name = "Direct"
                stack = "total"
                values(320, 302, 301, 334, 390)
            }
            bar {
                name = "Email"
                stack = "total"
                values(120, 132, 101, 134, 90)
            }
        }
    }

private fun pieDoughnutOption(): EChartsOption =
    echartsOption {
        tooltip { trigger = TooltipTrigger.Item }
        series {
            pie {
                radius = NumberOrStringOrNumbers.of(str("42%"), str("70%"))
                label { formatter = "{b}: {c} ({d}%)" }
                points(
                    dataItem { name = "Search engine"; value = valueOf(1048) },
                    dataItem { name = "Direct"; value = valueOf(735) },
                    dataItem { name = "Email"; value = valueOf(580) },
                    dataItem { name = "Union ads"; value = valueOf(484) },
                    dataItem { name = "Video ads"; value = valueOf(300) },
                )
            }
        }
    }

private fun scatterOption(): EChartsOption =
    echartsOption {
        tooltip { trigger = TooltipTrigger.Item }
        xAxis { type = AxisType.Value; scale = true }
        yAxis { type = AxisType.Value; scale = true }
        series {
            scatter {
                symbolSize = NumberOrStringOrNumbers.of(12)
                points(
                    point(10, 8.04),
                    point(8, 6.95),
                    point(13, 7.58),
                    point(9, 8.81),
                    point(11, 8.33),
                    point(14, 7.66),
                )
            }
        }
    }

private fun radarOption(): EChartsOption =
    echartsOption {
        legend { entries("Allocated budget", "Actual spending") }
        radar {
            radius = NumberOrStringOrNumbers.of("60%")
            indicator =
                listOf(
                    RadarIndicator().apply { name = "Sales"; max = 6500.0 },
                    RadarIndicator().apply { name = "Administration"; max = 16000.0 },
                    RadarIndicator().apply { name = "Technology"; max = 30000.0 },
                    RadarIndicator().apply { name = "Customer support"; max = 38000.0 },
                    RadarIndicator().apply { name = "Development"; max = 52000.0 },
                    RadarIndicator().apply { name = "Marketing"; max = 25000.0 },
                )
        }
        series {
            radar {
                symbolSize = NumberOrStringOrNumbers.of(4)
                points(
                    dataItem {
                        name = "Allocated budget"
                        value =
                            arrayValue(
                                valueOf(4200),
                                valueOf(3000),
                                valueOf(20000),
                                valueOf(35000),
                                valueOf(50000),
                                valueOf(18000),
                            )
                    },
                    dataItem {
                        name = "Actual spending"
                        value =
                            arrayValue(
                                valueOf(5000),
                                valueOf(14000),
                                valueOf(28000),
                                valueOf(26000),
                                valueOf(42000),
                                valueOf(21000),
                            )
                    },
                )
            }
        }
    }

private fun datasetBarOption(): EChartsOption =
    echartsOption {
        tooltip { trigger = TooltipTrigger.Axis }
        legend { entries("2024", "2025") }
        dataset {
            rows(
                listOf(textValue("product"), textValue("2024"), textValue("2025")),
                listOf(textValue("Matcha"), valueOf(43.3), valueOf(85.8)),
                listOf(textValue("Black tea"), valueOf(60.8), valueOf(73.4)),
                listOf(textValue("Milk tea"), valueOf(93.5), valueOf(68.4)),
            )
        }
        xAxis { type = AxisType.Category }
        yAxis { type = AxisType.Value }
        series {
            bar {
                name = "2024"
                encode =
                    buildJsonObject {
                        put("x", "product")
                        put("y", "2024")
                    }
            }
            bar {
                name = "2025"
                encode =
                    buildJsonObject {
                        put("x", "product")
                        put("y", "2025")
                    }
            }
        }
    }

private val LINE_SMOOTH_SOURCE =
    """
    echartsOption {
        title { text = "Weekly sales" }
        tooltip { trigger = TooltipTrigger.Axis }
        xAxis {
            type = AxisType.Category
            categories("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            boundaryGap = BoundaryGap.of(false)
        }
        yAxis { type = AxisType.Value }
        series {
            line {
                name = "Sales"
                smooth = BooleanOrNumber.of(true)
                values(120, 200, 150, 80, 70, 110, 130)
            }
        }
    }
    """.trimIndent()

private val BAR_STACK_SOURCE =
    """
    echartsOption {
        tooltip { trigger = TooltipTrigger.Axis }
        legend { entries("Direct", "Email") }
        xAxis {
            type = AxisType.Category
            categories("Mon", "Tue", "Wed", "Thu", "Fri")
        }
        yAxis { type = AxisType.Value }
        series {
            bar { name = "Direct"; stack = "total"; values(320, 302, 301, 334, 390) }
            bar { name = "Email"; stack = "total"; values(120, 132, 101, 134, 90) }
        }
    }
    """.trimIndent()

private val PIE_DOUGHNUT_SOURCE =
    """
    echartsOption {
        tooltip { trigger = TooltipTrigger.Item }
        series {
            pie {
                radius = NumberOrStringOrNumbers.of(str("42%"), str("70%"))
                label { formatter = "{b}: {c} ({d}%)" }
                points(
                    dataItem { name = "Search engine"; value = valueOf(1048) },
                    dataItem { name = "Direct"; value = valueOf(735) },
                    dataItem { name = "Email"; value = valueOf(580) },
                    dataItem { name = "Union ads"; value = valueOf(484) },
                    dataItem { name = "Video ads"; value = valueOf(300) },
                )
            }
        }
    }
    """.trimIndent()

private val SCATTER_SOURCE =
    """
    echartsOption {
        tooltip { trigger = TooltipTrigger.Item }
        xAxis { type = AxisType.Value; scale = true }
        yAxis { type = AxisType.Value; scale = true }
        series {
            scatter {
                symbolSize = NumberOrStringOrNumbers.of(12)
                points(
                    point(10, 8.04),
                    point(8, 6.95),
                    point(13, 7.58),
                    point(9, 8.81),
                    point(11, 8.33),
                    point(14, 7.66),
                )
            }
        }
    }
    """.trimIndent()

private val RADAR_SOURCE =
    """
    echartsOption {
        legend { entries("Allocated budget", "Actual spending") }
        radar {
            radius = NumberOrStringOrNumbers.of("60%")
            indicator = listOf(
                RadarIndicator().apply { name = "Sales"; max = 6500.0 },
                RadarIndicator().apply { name = "Administration"; max = 16000.0 },
                RadarIndicator().apply { name = "Technology"; max = 30000.0 },
                RadarIndicator().apply { name = "Customer support"; max = 38000.0 },
                RadarIndicator().apply { name = "Development"; max = 52000.0 },
                RadarIndicator().apply { name = "Marketing"; max = 25000.0 },
            )
        }
        series {
            radar {
                symbolSize = NumberOrStringOrNumbers.of(4)
                points(
                    dataItem {
                        name = "Allocated budget"
                        value = arrayValue(
                            valueOf(4200), valueOf(3000), valueOf(20000),
                            valueOf(35000), valueOf(50000), valueOf(18000),
                        )
                    },
                    dataItem {
                        name = "Actual spending"
                        value = arrayValue(
                            valueOf(5000), valueOf(14000), valueOf(28000),
                            valueOf(26000), valueOf(42000), valueOf(21000),
                        )
                    },
                )
            }
        }
    }
    """.trimIndent()

private val DATASET_BAR_SOURCE =
    """
    echartsOption {
        tooltip { trigger = TooltipTrigger.Axis }
        legend { entries("2024", "2025") }
        dataset {
            rows(
                listOf(textValue("product"), textValue("2024"), textValue("2025")),
                listOf(textValue("Matcha"), valueOf(43.3), valueOf(85.8)),
                listOf(textValue("Black tea"), valueOf(60.8), valueOf(73.4)),
                listOf(textValue("Milk tea"), valueOf(93.5), valueOf(68.4)),
            )
        }
        xAxis { type = AxisType.Category }
        yAxis { type = AxisType.Value }
        series {
            bar {
                name = "2024"
                encode = buildJsonObject { put("x", "product"); put("y", "2024") }
            }
            bar {
                name = "2025"
                encode = buildJsonObject { put("x", "product"); put("y", "2025") }
            }
        }
    }
    """.trimIndent()

/**
 * The model pages, built with the pure-KMP `kmp-echarts-json` API instead of the JS wrapper.
 *
 * Each page exists to show one modelling decision (union types, typed data items, datasets, …);
 * [ModelExample.json] is the serialized result of the very same builder the page prints.
 */
val kmpJsonExamples: List<ModelExample> =
    listOf(
        ModelExample(
            id = "kmp-json/line-smooth",
            titleZh = "平滑折线图",
            titleEn = "Smooth line",
            summaryZh = "smooth 是 BooleanOrNumber、boundaryGap 是 BoundaryGap，模型用联合类型表达这些可选项。",
            summaryEn = "smooth is a BooleanOrNumber and boundaryGap a BoundaryGap: the model expresses " +
                "these mixed shapes with union types.",
            kotlinSource = LINE_SMOOTH_SOURCE,
            option = lineSmoothOption(),
        ),
        ModelExample(
            id = "kmp-json/bar-stack",
            titleZh = "堆叠柱状图",
            titleEn = "Stacked bar",
            summaryZh = "同名的 stack 字段把两个柱状系列叠在一起，图例条目由 legend { entries(...) } 声明。",
            summaryEn = "A shared stack field folds two bar series together; the legend entries come from " +
                "legend { entries(...) }.",
            kotlinSource = BAR_STACK_SOURCE,
            option = barStackOption(),
        ),
        ModelExample(
            id = "kmp-json/pie-doughnut",
            titleZh = "环形饼图",
            titleEn = "Doughnut pie",
            summaryZh = "radius 的数组分支由 NumberOrStringOrNumbers 表达，每个扇区用 dataItem 构造。",
            summaryEn = "The array branch of radius is a NumberOrStringOrNumbers, and each slice is built " +
                "with dataItem.",
            kotlinSource = PIE_DOUGHNUT_SOURCE,
            option = pieDoughnutOption(),
        ),
        ModelExample(
            id = "kmp-json/scatter",
            titleZh = "散点图",
            titleEn = "Scatter",
            summaryZh = "二维点位用 point(x, y) 生成，symbolSize 走联合工厂而不是裸数字。",
            summaryEn = "Two-dimensional points come from point(x, y), and symbolSize goes through the " +
                "union factory instead of a bare number.",
            kotlinSource = SCATTER_SOURCE,
            option = scatterOption(),
        ),
        ModelExample(
            id = "kmp-json/radar",
            titleZh = "雷达图",
            titleEn = "Radar",
            summaryZh = "雷达坐标系与系列数据都用模型类构造，数组值由 arrayValue 拼出。",
            summaryEn = "The radar coordinate system and its series data are model classes; array values " +
                "come from arrayValue.",
            kotlinSource = RADAR_SOURCE,
            option = radarOption(),
        ),
        ModelExample(
            id = "kmp-json/dataset-bar",
            titleZh = "数据集柱状图",
            titleEn = "Dataset bar",
            summaryZh = "dataset 行数据与 encode 映射都在模型里声明，免去逐系列复制数组。",
            summaryEn = "The dataset rows and the encode mapping are declared in the model, so no series " +
                "repeats the arrays.",
            kotlinSource = DATASET_BAR_SOURCE,
            option = datasetBarOption(),
        ),
    )

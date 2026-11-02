package demo.json.examples.dataset

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.*

private val dataTransformSortBarJsonCategory = JsonCategory("json/examples/dataset/DataTransformSortBarJson.kt")

val dataTransformSortBarJsonExample: JsonExample =
    dataTransformSortBarJsonCategory.example("data-transform-sort-bar") {
            dataset {
                dimensions = dimensionsOf("name", "age", "profession", "score", "date")
                rows(
                    // The trailing space of `Jasmin Krause ` is in the upstream table.
                    row(
                        textValue("Hannah Krause"),
                        valueOf(41),
                        textValue("Engineer"),
                        valueOf(314),
                        textValue("2011-02-12"),
                    ),
                    row(
                        textValue("Zhao Qian"),
                        valueOf(20),
                        textValue("Teacher"),
                        valueOf(351),
                        textValue("2011-03-01"),
                    ),
                    row(
                        textValue("Jasmin Krause "),
                        valueOf(52),
                        textValue("Musician"),
                        valueOf(287),
                        textValue("2011-02-14"),
                    ),
                    row(
                        textValue("Li Lei"),
                        valueOf(37),
                        textValue("Teacher"),
                        valueOf(219),
                        textValue("2011-02-18"),
                    ),
                    row(
                        textValue("Karle Neumann"),
                        valueOf(25),
                        textValue("Engineer"),
                        valueOf(253),
                        textValue("2011-04-02"),
                    ),
                    // `-` is the score of the row the sort transform pushes to the end.
                    row(
                        textValue("Adrian Groß"),
                        valueOf(19),
                        textValue("Teacher"),
                        textValue("-"),
                        textValue("2011-01-16"),
                    ),
                    row(
                        textValue("Mia Neumann"),
                        valueOf(71),
                        textValue("Engineer"),
                        valueOf(165),
                        textValue("2011-03-19"),
                    ),
                    row(
                        textValue("Böhm Fuchs"),
                        valueOf(36),
                        textValue("Musician"),
                        valueOf(318),
                        textValue("2011-02-24"),
                    ),
                    row(
                        textValue("Han Meimei"),
                        valueOf(67),
                        textValue("Engineer"),
                        valueOf(366),
                        textValue("2011-03-12"),
                    ),
                )
            }
            dataset { transform = transforms(SORT_BY_SCORE) }
            xAxis {
                type = AxisType.Category
                axisLabel {
                    interval = num(0)
                    rotate = 30.0
                }
            }
            yAxis { }
            series {
                bar {
                    encode = encodeOf("x" to "name", "y" to "score")
                    datasetIndex = 1
                }
            }
        }

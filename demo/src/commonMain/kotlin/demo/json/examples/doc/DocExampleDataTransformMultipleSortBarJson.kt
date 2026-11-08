package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import demo.json.*

private val docExampleDataTransformMultipleSortBarJsonCategory = JsonCategory("json/examples/doc/DocExampleDataTransformMultipleSortBarJson.kt")

val docExampleDataTransformMultipleSortBarJsonExample: JsonExample =
    docExampleDataTransformMultipleSortBarJsonCategory.example("doc-example/data-transform-multiple-sort-bar") {
            dataset {
                dimensions =
                    listOf(
                        JsonPrimitive("name"),
                        JsonPrimitive("age"),
                        JsonPrimitive("profession"),
                        JsonPrimitive("score"),
                        JsonPrimitive("date"),
                    )
                source =
                    datasetRows(
                        listOf(
                            textValue(" Hannah Krause "),
                            valueOf(41),
                            textValue("Engineer"),
                            valueOf(314),
                            textValue("2011-02-12"),
                        ),
                        listOf(
                            textValue("Zhao Qian "),
                            valueOf(20),
                            textValue("Teacher"),
                            valueOf(351),
                            textValue("2011-03-01"),
                        ),
                        listOf(
                            textValue(" Jasmin Krause "),
                            valueOf(52),
                            textValue("Musician"),
                            valueOf(287),
                            textValue("2011-02-14"),
                        ),
                        listOf(
                            textValue("Li Lei"),
                            valueOf(37),
                            textValue("Teacher"),
                            valueOf(219),
                            textValue("2011-02-18"),
                        ),
                        listOf(
                            textValue(" Karle Neumann "),
                            valueOf(25),
                            textValue("Engineer"),
                            valueOf(253),
                            textValue("2011-04-02"),
                        ),
                        listOf(
                            textValue(" Adrian Groß"),
                            valueOf(19),
                            textValue("Teacher"),
                            DataValue.Null,
                            textValue("2011-01-16"),
                        ),
                        listOf(
                            textValue("Mia Neumann"),
                            valueOf(71),
                            textValue("Engineer"),
                            valueOf(165),
                            textValue("2011-03-19"),
                        ),
                        listOf(
                            textValue(" Böhm Fuchs"),
                            valueOf(36),
                            textValue("Musician"),
                            valueOf(318),
                            textValue("2011-02-24"),
                        ),
                        listOf(
                            textValue("Han Meimei "),
                            valueOf(67),
                            textValue("Engineer"),
                            valueOf(366),
                            textValue("2011-03-12"),
                        ),
                    )
            }
            dataset {
                transform =
                    buildJsonObject {
                        put("type", "sort")
                        put(
                            "config",
                            buildJsonArray {
                                add(
                                    buildJsonObject {
                                        put("dimension", "profession")
                                        put("order", "desc")
                                    },
                                )
                                add(
                                    buildJsonObject {
                                        put("dimension", "score")
                                        put("order", "desc")
                                    },
                                )
                            },
                        )
                    }
            }
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
                    label {
                        show = true
                        rotate = 90.0
                        position = "insideBottom"
                        align = "left"
                        verticalAlign = "middle"
                    }
                    encode =
                        buildJsonObject {
                            put("x", "name")
                            put("y", "score")
                            put("label", buildJsonArray { add(JsonPrimitive("profession")) })
                        }
                    datasetIndex = 1
                }
            }
        }

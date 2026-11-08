package io.github.shijianjs.ktjs.echarts.json

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant

class ValuesTest {
    @Test
    fun encodesNumberOrString() {
        val axis = echartsOption {
            xAxis {
                min = num(0)
                max = str("dataMax")
            }
        }.toJsonElement()["xAxis"]!!.jsonArray[0].jsonObject
        assertEquals(0.0, axis["min"]!!.jsonPrimitive.double)
        assertEquals("dataMax", axis["max"]!!.jsonPrimitive.content)
    }

    @Test
    fun encodesBooleanOrString() {
        val element = echartsOption { darkMode = BooleanOrString.of("auto") }.toJsonElement()
        assertEquals("auto", element["darkMode"]!!.jsonPrimitive.content)
    }

    @Test
    fun encodesBooleanOrNumber() {
        val series = echartsOption {
            series {
                line { smooth = BooleanOrNumber.of(0.3) }
                bar { smooth = BooleanOrNumber.of(true) }
            }
        }.toJsonElement()["series"]!!.jsonArray
        assertEquals(0.3, series[0].jsonObject["smooth"]!!.jsonPrimitive.double)
        assertTrue(series[1].jsonObject["smooth"]!!.jsonPrimitive.boolean)
    }

    @Test
    fun encodesNumberOrNumbers() {
        val series = echartsOption {
            series {
                pictorialBar { symbolBoundingData = NumberOrNumbers.of(40) }
                pictorialBar { symbolBoundingData = NumberOrNumbers.of(-60, 40) }
            }
        }.toJsonElement()["series"]!!.jsonArray
        assertEquals(40.0, series[0].jsonObject["symbolBoundingData"]!!.jsonPrimitive.double)
        assertEquals(
            listOf(-60.0, 40.0),
            series[1].jsonObject["symbolBoundingData"]!!.jsonArray.map { it.jsonPrimitive.double },
        )
    }

    @Test
    fun encodesBooleanOrNumberOrString() {
        val series = echartsOption {
            series {
                pictorialBar { symbolRepeat = BooleanOrNumberOrString.of("fixed") }
                pictorialBar { symbolRepeat = BooleanOrNumberOrString.of(true) }
                pictorialBar { symbolRepeat = BooleanOrNumberOrString.of(3) }
            }
        }.toJsonElement()["series"]!!.jsonArray
        assertEquals("fixed", series[0].jsonObject["symbolRepeat"]!!.jsonPrimitive.content)
        assertTrue(series[1].jsonObject["symbolRepeat"]!!.jsonPrimitive.boolean)
        assertEquals(3.0, series[2].jsonObject["symbolRepeat"]!!.jsonPrimitive.double)
    }

    @Test
    fun encodesSingleAndMultipleNumbers() {
        val series = echartsOption {
            series {
                scatter { symbolSize = NumberOrStringOrNumbers.of(10) }
                scatter { symbolSize = NumberOrStringOrNumbers.numbers(10, 20) }
                scatter { symbolSize = NumberOrStringOrNumbers.of("50%") }
            }
        }.toJsonElement()["series"]!!.jsonArray
        assertEquals(10.0, series[0].jsonObject["symbolSize"]!!.jsonPrimitive.double)
        assertEquals(
            listOf(10.0, 20.0),
            series[1].jsonObject["symbolSize"]!!.jsonArray.map { it.jsonPrimitive.double },
        )
        assertEquals("50%", series[2].jsonObject["symbolSize"]!!.jsonPrimitive.content)
    }

    @Test
    fun encodesBoundaryGapForms() {
        fun boundaryGap(gap: BoundaryGap) = echartsOption {
            xAxis { boundaryGap = gap }
        }.toJsonElement()["xAxis"]!!.jsonArray[0].jsonObject["boundaryGap"]!!

        assertFalse(boundaryGap(BoundaryGap.of(false)).jsonPrimitive.boolean)
        val range = boundaryGap(BoundaryGap.of(str("20%"), str("10%"))).jsonArray
        assertEquals(listOf("20%", "10%"), range.map { it.jsonPrimitive.content })
    }

    @Test
    fun encodesDataValuePrimitives() {
        assertEquals(JsonPrimitive(1.0), valueOf(1).toJsonElement())
        assertEquals(JsonPrimitive("a"), textValue("a").toJsonElement())
        assertEquals(JsonPrimitive(true), DataValue.Bool(true).toJsonElement())
        assertEquals(JsonNull, DataValue.Null.toJsonElement())
        assertEquals(
            JsonArray(listOf(JsonPrimitive(1.0), JsonPrimitive(2.0))),
            point(1, 2).toJsonElement(),
        )
    }

    @Test
    fun encodesTimeValues() {
        assertEquals(JsonPrimitive(0L), Instant.fromEpochMilliseconds(0).asDataValue().toJsonElement())
        assertEquals(
            JsonPrimitive("2024-04-05 06:07:08"),
            LocalDateTime.parse("2024-04-05T06:07:08").asDataValue().toJsonElement(),
        )
        assertEquals(
            JsonPrimitive("2024-04-05"),
            LocalDate.parse("2024-04-05").asDataValue().toJsonElement(),
        )
    }

    @Test
    fun formatsTimeWithPadding() {
        assertEquals(
            "2024-04-05 06:07:08",
            LocalDateTime.parse("2024-04-05T06:07:08").toEChartsTime(),
        )
        assertEquals("2024-04-05", LocalDate.parse("2024-04-05").toEChartsTime())
    }

    @Test
    fun parsesJsonBackToDataValue() {
        assertEquals(DataValue.Number(1.5), JsonPrimitive(1.5).toDataValue())
        assertEquals(DataValue.Text("a"), JsonPrimitive("a").toDataValue())
        assertEquals(DataValue.Bool(true), JsonPrimitive(true).toDataValue())
        assertEquals(DataValue.Null, JsonNull.toDataValue())
        assertEquals(
            DataValue.Array(listOf(DataValue.Number(1.0), DataValue.Text("a"))),
            JsonArray(listOf(JsonPrimitive(1), JsonPrimitive("a"))).toDataValue(),
        )
        val obj = JsonObject(mapOf("x" to JsonPrimitive(1)))
        assertEquals(DataValue.Object(obj), obj.toDataValue())
    }

    @Test
    fun buildsDataItem() {
        val item = dataItem {
            name = "A"
            value = valueOf(3)
            itemStyle { color = "red".asColor() }
        }
        val json = item.toJsonElement().jsonObject
        assertEquals("A", json["name"]!!.jsonPrimitive.content)
        assertEquals(3.0, json["value"]!!.jsonPrimitive.double)
        assertEquals(
            "red",
            json["itemStyle"]!!.jsonObject["color"]!!.jsonPrimitive.content,
        )
    }

    @Test
    fun omitsUnsetDataItemFields() {
        val json = DataItem().apply { name = "A" }.asDataValue().toJsonElement().jsonObject
        assertEquals(listOf("name"), json.keys.toList())
    }

    @Test
    fun dataItemsNestChildren() {
        val item = DataItem().apply {
            name = "root"
            children = listOf(DataItem().apply { name = "leaf"; value = valueOf(1) })
        }
        val child = item.asDataValue().toJsonElement()
            .jsonObject["children"]!!.jsonArray[0].jsonObject
        assertEquals("leaf", child["name"]!!.jsonPrimitive.content)
    }

    @Test
    fun buildsValueLists() {
        assertEquals(
            listOf(DataValue.Number(1.0), DataValue.Number(2.0)),
            dataValues(1, 2),
        )
        assertEquals(
            listOf(DataValue.Text("a"), DataValue.Text("b")),
            dataValues("a", "b"),
        )
    }

    @Test
    fun axisCategoriesAreStrings() {
        val axis = echartsOption { xAxis { categories("Mon", "Tue") } }
            .toJsonElement()["xAxis"]!!.jsonArray[0].jsonObject
        assertEquals(
            listOf("Mon", "Tue"),
            axis["data"]!!.jsonArray.map { it.jsonPrimitive.content },
        )
    }

    @Test
    fun numbersAndStringsBuildersWrapBothVariants() {
        assertEquals(NumberOrString.Number(1.0), num(1))
        assertEquals(NumberOrString.Text("50%"), str("50%"))
        assertEquals(listOf(num(1), num(2)), numbers(1, 2))
        assertEquals(listOf(str("a")), strings("a"))
    }

    @Test
    fun multivalueHelperWrapsArgumentsAsArray() {
        val multiple = NumberOrStringOrNumbers.of(num(1), str("2"))
        assertIs<NumberOrStringOrNumbers.Multiple>(multiple)
        assertEquals(listOf(num(1), str("2")), multiple.values)
    }

    @Test
    fun arrayValuePacksVariadicValues() {
        assertEquals(
            DataValue.Array(listOf(DataValue.Number(1.0), DataValue.Text("a"))),
            arrayValue(valueOf(1), textValue("a")),
        )
    }
}

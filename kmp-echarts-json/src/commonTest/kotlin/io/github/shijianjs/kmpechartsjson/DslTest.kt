package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class DslTest {
    @Test
    fun dslMatchesDirectConstruction() {
        val viaDsl = echartsOption {
            animation = true
            title { text = "Sales" }
            xAxis {
                type = AxisType.Category
                categories("Mon", "Tue")
            }
            series {
                line {
                    name = "Revenue"
                    values(1, 2)
                }
            }
        }

        val direct = EChartsOption().apply {
            animation = true
            title = listOf(TitleOption().apply { text = "Sales" })
            xAxis = listOf(
                AxisOption().apply {
                    type = AxisType.Category
                    data = dataValues("Mon", "Tue")
                },
            )
            series = listOf(
                SeriesOption(SeriesType.Line).apply {
                    name = "Revenue"
                    data = dataValues(1, 2)
                },
            )
        }

        assertEquals(direct.toJsonString(), viaDsl.toJsonString())
    }

    @Test
    fun seriesBuilderAssignsEveryBundledType() {
        val option = echartsOption {
            series {
                line { }
                bar { }
                pie { }
                scatter { }
                effectScatter { }
                radar { }
                map { }
                treemap { }
                graph { }
                lines { }
                tree { }
                sunburst { }
                boxplot { }
                candlestick { }
                heatmap { }
                custom { }
                sankey { }
                funnel { }
                gauge { }
                pictorialBar { }
                themeRiver { }
                parallel { }
            }
        }
        assertEquals(
            listOf(
                "line", "bar", "pie", "scatter", "effectScatter", "radar", "map", "treemap",
                "graph", "lines", "tree", "sunburst", "boxplot", "candlestick", "heatmap",
                "custom", "sankey", "funnel", "gauge", "pictorialBar", "themeRiver", "parallel",
            ),
            option.series!!.map { it.type.value },
        )
    }

    @Test
    fun seriesBuilderAcceptsThirdPartyTypes() {
        val type = SeriesType("scatter3D")
        val option = echartsOption {
            series {
                add(type) {
                    name = "3D"
                    coordinateSystem = "geo3D"
                }
                add(SeriesOption(SeriesType.Bar).apply { values(1) })
            }
        }
        assertEquals("scatter3D", option.series!![0].type.value)
        assertEquals("geo3D", option.series!![0].coordinateSystem)
        assertEquals(SeriesType.Bar, option.series!![1].type)
    }

    @Test
    fun componentBuildersAddEveryComponent() {
        val element = echartsOption {
            title { text = "t" }
            tooltip { trigger = TooltipTrigger.Axis }
            legend { entries("A") }
            grid { left = str("10%") }
            xAxis { type = AxisType.Category }
            yAxis { type = AxisType.Value }
            polar { radius = NumberOrStringOrNumbers.of("75%") }
            radiusAxis { type = AxisType.Category }
            angleAxis { type = AxisType.Value }
            radar { indicator = listOf(RadarIndicator().apply { name = "a"; max = 10.0 }) }
            singleAxis { type = AxisType.Value }
            parallel { left = str("5%") }
            parallelAxis { type = AxisType.Value }
            calendar { range = JsonPrimitive("2024") }
            geo { map = "china" }
            dataZoom { type = DataZoomType.Inside }
            visualMap { type = VisualMapType.Continuous }
            toolbox { feature = ToolboxFeatureOption().apply { saveAsImage = ToolboxSaveAsImageFeature() } }
            dataset { rows(listOf(valueOf(1))) }
            media {
                query = buildJsonObject { put("maxWidth", 500) }
                option = EChartsOption().apply { animation = false }
            }
            axisPointer { type = "line" }
            brush { brushType = "rect" }
            timeline { labels("a", "b") }
            graphic { elements = listOf(GraphicElement().apply { type = "text"; textContent = "x" }) }
            aria { enabled = true }
        }.toJsonElement()

        val arrayKeys = listOf(
            "title", "tooltip", "legend", "grid", "xAxis", "yAxis", "polar", "radiusAxis",
            "angleAxis", "radar", "singleAxis", "parallel", "parallelAxis", "calendar", "geo",
            "dataZoom", "visualMap", "toolbox", "dataset", "media",
        )
        for (key in arrayKeys) {
            assertEquals(1, element[key]!!.jsonArray.size, "Expected one entry for $key")
        }
        for (key in listOf("axisPointer", "brush", "timeline", "graphic", "aria")) {
            assertNotNull(element[key]?.jsonObject, "Expected an object for $key")
        }
        assertEquals(
            "2024",
            element["calendar"]!!.jsonArray[0].jsonObject["range"]!!.jsonPrimitive.content,
        )
        assertEquals(
            "china",
            element["geo"]!!.jsonArray[0].jsonObject["map"]!!.jsonPrimitive.content,
        )
        assertEquals(
            "inside",
            element["dataZoom"]!!.jsonArray[0].jsonObject["type"]!!.jsonPrimitive.content,
        )
        assertEquals(
            "continuous",
            element["visualMap"]!!.jsonArray[0].jsonObject["type"]!!.jsonPrimitive.content,
        )
        assertEquals(
            listOf("a", "b"),
            element["timeline"]!!.jsonObject["data"]!!.jsonArray.map { it.jsonPrimitive.content },
        )
        assertEquals(
            true,
            element["aria"]!!.jsonObject["enabled"]!!.jsonPrimitive.boolean,
        )
        assertEquals(
            500,
            element["media"]!!.jsonArray[0].jsonObject["query"]!!.jsonObject["maxWidth"]!!
                .jsonPrimitive.int,
        )
    }

    @Test
    fun appendingBuildersKeepEveryCall() {
        val option = echartsOption {
            title { text = "one" }
            title { text = "two" }
            series { line { } }
            series { bar { } }
        }
        assertEquals(2, option.title!!.size)
        // series() replaces the list; the last builder wins.
        assertEquals(listOf(SeriesType.Bar), option.series!!.map { it.type })
    }

    @Test
    fun seriesDataHelpers() {
        val option = echartsOption {
            series {
                line { values(1, 2) }
                bar { values("a", "b") }
                scatter { points(point(1, 2)) }
                pie { items(DataItem().apply { name = "A"; value = valueOf(1) }) }
                candlestick { numberRows(listOf(1, 2, 3, 4)) }
                boxplot { rows(listOf(valueOf(1), valueOf(2))) }
            }
        }
        val series = option.series!!
        assertEquals(dataValues(1, 2), series[0].data)
        assertEquals(listOf("a", "b"), series[1].data!!.map { (it as DataValue.Text).value })
        assertEquals(DataValue.Array(listOf(valueOf(1), valueOf(2))), series[2].data!![0])
        assertEquals(DataValue.Array(listOf(valueOf(1), valueOf(2), valueOf(3), valueOf(4))), series[4].data!![0])
        assertEquals(DataValue.Array(listOf(valueOf(1), valueOf(2))), series[5].data!![0])
    }

    @Test
    fun styleHelpersBuildNestedStyles() {
        val option = echartsOption {
            series {
                line {
                    itemStyle { color = "red".asColor() }
                    label { show = true }
                    lineStyle { width = 2.0 }
                    areaStyle { opacity = 0.3 }
                    emphasis { label { show = false } }
                }
            }
        }
        val series = option.series!![0]
        assertEquals("red", (series.itemStyle!!.color as EChartsColor.Css).value)
        assertEquals(true, series.label!!.show)
        assertEquals(2.0, series.lineStyle!!.width)
        assertEquals(0.3, series.areaStyle!!.opacity)
        assertEquals(false, series.emphasis!!.label!!.show)
    }

    @Test
    fun styleHelpersReuseTheExistingObject() {
        val series = SeriesOption(SeriesType.Line).apply {
            label = LabelOption().apply { show = true }
        }
        val existing = series.label
        series.label { color = "blue".asColor() }
        assertSame(existing, series.label)
        assertEquals(true, series.label!!.show)
    }

    @Test
    fun legendEntriesAndVisualMapPieces() {
        val option = echartsOption {
            legend {
                data = listOf(
                    LegendItem.Name("A"),
                    LegendItem.Entry(
                        name = "B",
                        icon = "circle",
                        textStyle = TextStyleOption().apply { color = "red".asColor() },
                    ),
                )
            }
            visualMap {
                piece { min = 0.0; max = 10.0; label = "low" }
                piece { gt = 10.0; label = "high" }
            }
        }
        val legend = option.toJsonElement()["legend"]!!.jsonArray[0].jsonObject
        assertEquals(
            listOf("A", "B"),
            legend["data"]!!.jsonArray.map {
                if (it is JsonPrimitive) it.content else it.jsonObject["name"]!!.jsonPrimitive.content
            },
        )
        val entry = legend["data"]!!.jsonArray[1].jsonObject
        assertEquals("circle", entry["icon"]!!.jsonPrimitive.content)
        assertNull(entry["itemStyle"])
        val pieces = option.toJsonElement()["visualMap"]!!.jsonArray[0].jsonObject["pieces"]!!.jsonArray
        assertEquals("low", pieces[0].jsonObject["label"]!!.jsonPrimitive.content)
        assertEquals(10.0, pieces[1].jsonObject["gt"]!!.jsonPrimitive.double)
    }

    @Test
    fun dataItemStyleHelpers() {
        val item = dataItem {
            name = "A"
            label { show = true }
            itemStyle { opacity = 0.5 }
        }
        val json = item.toJsonElement().jsonObject
        assertEquals(true, json["label"]!!.jsonObject["show"]!!.jsonPrimitive.boolean)
        assertEquals(0.5, json["itemStyle"]!!.jsonObject["opacity"]!!.jsonPrimitive.double)
    }

    @Test
    fun datasetHelpersAndRoundTrip() {
        val option = echartsOption {
            dataset {
                rows(listOf(valueOf(1), textValue("a")), listOf(valueOf(2), textValue("b")))
            }
        }
        val source = option.dataset!![0].source!!
        assertIs<DatasetSource.Rows>(source)
        val encoded = EChartsJson.format.encodeToString(DatasetSourceSerializer, source)
        assertEquals(source, EChartsJson.format.decodeFromString(DatasetSourceSerializer, encoded))
        val rows = EChartsJson.format.parseToJsonElement(encoded).jsonArray
        assertEquals(2, rows.size)
        assertEquals("a", rows[0].jsonArray[1].jsonPrimitive.content)

        val columns = datasetColumns(mapOf("x" to listOf(valueOf(1), valueOf(2))))
        val encodedColumns = EChartsJson.format.encodeToString(DatasetSourceSerializer, columns)
        assertEquals(
            columns,
            EChartsJson.format.decodeFromString(DatasetSourceSerializer, encodedColumns),
        )
        assertEquals(
            2,
            EChartsJson.format.parseToJsonElement(encodedColumns).jsonObject["x"]!!.jsonArray.size,
        )

        val objectRows = DatasetSource.ObjectRows(
            listOf(mapOf("x" to valueOf(1), "y" to textValue("a"))),
        )
        assertEquals(
            objectRows,
            EChartsJson.format.decodeFromString(
                DatasetSourceSerializer,
                EChartsJson.format.encodeToString(DatasetSourceSerializer, objectRows),
            ),
        )

        val raw = DatasetSource.Raw(JsonPrimitive(7))
        assertEquals(
            raw,
            EChartsJson.format.decodeFromString(
                DatasetSourceSerializer,
                EChartsJson.format.encodeToString(DatasetSourceSerializer, raw),
            ),
        )
    }

    @Test
    fun datasetNumbersHelper() {
        val source = datasetNumbers(listOf(1, 2), listOf(3, 4)) as DatasetSource.Rows
        assertEquals(
            listOf(
                listOf(valueOf(1), valueOf(2)),
                listOf(valueOf(3), valueOf(4)),
            ),
            source.rows,
        )
    }

    @Test
    fun timelineAndMediaHelpers() {
        val option = echartsOption {
            timeline { currentIndex = 1 }
            media {
                query = buildJsonObject { put("minWidth", 800) }
                option = EChartsOption().apply { title { text = "wide" } }
            }
        }
        assertEquals(1, option.timeline!!.currentIndex)
        assertEquals("wide", option.media!![0].option!!.title!![0].text)
    }

    @Test
    fun textStyleHelperBuildsRichEntries() {
        val rich =
            mapOf(
                "value" to textStyleOf { fontWeight = str("bold") },
                "unit" to textStyleOf { fontSize = 12.0 },
            )
        assertEquals(str("bold"), rich.getValue("value").fontWeight)
        assertEquals(12.0, rich.getValue("unit").fontSize)
    }
}

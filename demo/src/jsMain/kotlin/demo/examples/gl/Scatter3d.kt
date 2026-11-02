package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.AxisTypes
import echarts.options.*
import echartsgl.GlColor
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor
import demo.examples.Category
import demo.examples.matrix.dimension
import demo.examples.shared.numberAt

private val scatter3dCategory = Category(Groups.GL, "examples/gl/Scatter3d.kt")

val scatter3dEntry: CatalogEntry =
    scatter3dCategory.interactive(
            "scatter3d",
            "三维散点图",
            "Scatter3D",
            "食品营养成分表：蛋白质、纤维、钠三轴定位，纤维含量决定颜色、维生素 C 决定点的大小，两个滑杆可调阈值。",
            "A nutrient table drawn in three dimensions: fiber drives the colour and vitamin C the " +
                "symbol size, both adjustable with the two sliders.",
        ) { chart ->
            loadJson("data/nutrients.json").then { json ->
                val rows = json.unsafeCast<Array<JsAny?>>()
                val colorMax = rows.maxOf { numberAt(it, FIBER) } / 2
                val symbolSizeMax = rows.maxOf { numberAt(it, VITAMIN_C) } / 2
                chart.setOption(
                    unsafeJso<EChartsGlOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        visualMap =
                            arrayOf(
                                unsafeJso<VisualMapOption> {
                                    top = StringOrDouble(10.0)
                                    calculable = true
                                    dimension = 3.0
                                    max = colorMax
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            color =
                                                StringOrStringArray(
                                                    arrayOf(
                                                        "#1710c0",
                                                        "#0b9df0",
                                                        "#00fea8",
                                                        "#00ff0d",
                                                        "#f5f811",
                                                        "#f09a09",
                                                        "#fe0300",
                                                    ),
                                                )
                                        }
                                },
                                unsafeJso<VisualMapOption> {
                                    bottom = StringOrDouble(10.0)
                                    calculable = true
                                    dimension = 4.0
                                    max = symbolSizeMax
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            symbolSize = DoubleOrDoubleArray(arrayOf(10.0, 40.0))
                                        }
                                },
                            )
                        xAxis3D =
                            one(
                                unsafeJso<Axis3DOption> {
                                    name = "protein"
                                    type = AxisTypes.value
                                },
                            )
                        yAxis3D =
                            one(
                                unsafeJso<Axis3DOption> {
                                    name = "fiber"
                                    type = AxisTypes.value
                                },
                            )
                        zAxis3D =
                            one(
                                unsafeJso<Axis3DOption> {
                                    name = "sodium"
                                    type = AxisTypes.value
                                },
                            )
                        grid3D = one(unsafeJso<Grid3DOption> {})
                        series =
                            one(
                                scatter3DSeries {
                                    symbolSize = SymbolSize(12.0)
                                    typedItemStyle =
                                        unsafeJso<GlItemStyleOption> {
                                            borderWidth = 1.0
                                            borderColor = GlColor(ZRColor("rgba(255,255,255,0.8)"))
                                        }
                                    typedEmphasis =
                                        unsafeJso<GlEmphasisOption> {
                                            itemStyle = unsafeJso<GlItemStyleOption> { color = GlColor(ZRColor("#fff")) }
                                        }
                                    dimensions =
                                        arrayOf(
                                            DimensionDefinitionLoose("protein"),
                                            DimensionDefinitionLoose("fiber"),
                                            DimensionDefinitionLoose("sodium"),
                                            DimensionDefinitionLoose("fiber"),
                                            DimensionDefinitionLoose("vitaminc"),
                                            DimensionDefinitionLoose("index"),
                                        )
                                    typedData = Array(rows.size) { index -> nutrientItem(rows[index], index) }
                                },
                            )
                    },
                )
                null
            }
        }

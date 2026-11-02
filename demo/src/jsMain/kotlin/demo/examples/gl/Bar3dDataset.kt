package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.AxisTypes
import echarts.options.*
import echartsgl.ShadingTypes
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category

private val bar3dDatasetCategory = Category(Groups.GL, "examples/gl/Bar3dDataset.kt")

val bar3dDatasetEntry: CatalogEntry =
    bar3dDatasetCategory.interactive(
            "bar3d-dataset",
            "使用 dataset 为三维柱状图设置数据",
            "3D Bar with Dataset",
            "dataset 的列名同时当作三个坐标轴与配色维度：年份、国家、预期寿命与人口。",
            "The columns of a dataset feed all three axes at once: year, country, life expectancy " +
                "and population.",
        ) { chart ->
            loadJson("data/life-expectancy-table.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsGlOption> {
                        grid3D = one(unsafeJso<Grid3DOption> {})
                        tooltip = one(unsafeJso<TooltipOption> {})
                        xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.category })
                        yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.category })
                        zAxis3D = one(unsafeJso<Axis3DOption> {})
                        visualMap =
                            one(
                                unsafeJso<VisualMapOption> {
                                    max = 1e8
                                    typedDimension = DimensionLoose("Population")
                                },
                            )
                        dataset =
                            one(
                                unsafeJso<DatasetOption> {
                                    dimensions =
                                        arrayOf(
                                            DimensionDefinitionLoose("Income"),
                                            DimensionDefinitionLoose("Life Expectancy"),
                                            DimensionDefinitionLoose("Population"),
                                            DimensionDefinitionLoose("Country"),
                                            DimensionDefinitionLoose(
                                                unsafeJso<DimensionDefinition> {
                                                    name = "Year"
                                                    type = "ordinal"
                                                },
                                            ),
                                        )
                                    source = json.unsafeCast<DatasetSource>()
                                },
                            )
                        series =
                            one(
                                bar3DSeries {
                                    shading = ShadingTypes.lambert
                                    encode =
                                        unsafeJso {
                                            x = OptionEncodeValue(DimensionLoose("Year"))
                                            y = OptionEncodeValue(DimensionLoose("Country"))
                                            z = OptionEncodeValue(DimensionLoose("Life Expectancy"))
                                            tooltip =
                                                OptionEncodeValue(
                                                    arrayOf<DimensionLoose>(
                                                        DimensionLoose(0.0),
                                                        DimensionLoose(1.0),
                                                        DimensionLoose(2.0),
                                                        DimensionLoose(3.0),
                                                        DimensionLoose(4.0),
                                                    ),
                                                )
                                        }
                                },
                            )
                    },
                )
                null
            }
        }

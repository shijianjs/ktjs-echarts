package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.*
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category

private val scatter3DDatasetCategory = Category(Groups.GL, "examples/gl/Scatter3DDataset.kt")

val scatter3DDatasetEntry: CatalogEntry =
    scatter3DDatasetCategory.interactive(
            "scatter3D-dataset",
            "使用 dataset 为三维散点图设置数据",
            "3D Scatter with Dataset",
            "国家、预期寿命、收入三列直接当作三个坐标轴，人口与年份只出现在 tooltip 里。",
            "Country, life expectancy and income feed the three axes; population and year stay in " +
                "the tooltip.",
        ) { chart ->
            loadJson("data/life-expectancy-table.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsGlOption> {
                        grid3D = one(unsafeJso<Grid3DOption> {})
                        xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.category })
                        yAxis3D = one(unsafeJso<Axis3DOption> {})
                        zAxis3D = one(unsafeJso<Axis3DOption> {})
                        dataset = one(lifeExpectancyDataset(json))
                        series =
                            one(
                                scatter3DSeries {
                                    symbolSize = SymbolSize(2.5)
                                    encode = encode3("Country", "Life Expectancy", "Income")
                                },
                            )
                    },
                )
                null
            }
        }

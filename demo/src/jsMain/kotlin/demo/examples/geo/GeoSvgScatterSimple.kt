package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadText
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor
import demo.examples.Category
import demo.examples.matrix.dimension
import demo.examples.shared.numberAt

private val geoSvgScatterSimpleCategory = Category(Groups.GEO, "examples/geo/GeoSvgScatterSimple.kt")

val geoSvgScatterSimpleEntry: CatalogEntry =
    geoSvgScatterSimpleCategory.interactive("geo-svg-scatter-simple", "散点图（SVG 地图）", "GEO SVG Scatter") { chart ->
            loadText("data/Map_of_Iceland.svg").then { svg ->
                ECharts.registerMap(
                    "iceland_svg",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    tooltip = unsafeJso<TooltipOption> { show = true }
                                    map = "iceland_svg"
                                    roam = BooleanOrString(true)
                                },
                            )
                        series =
                            one(
                                effectScatterSeries {
                                    coordinateSystem = "geo"
                                    geoIndex = 0.0
                                    // The third dimension drives the symbol size.
                                    symbolSize =
                                        SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 2) / 100 * 15 + 5) }
                                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#b02a02") }
                                    encode =
                                        unsafeJso<OptionEncode> {
                                            tooltip = OptionEncodeValue(DimensionLoose(2.0))
                                        }
                                    typedData =
                                        dataItems(
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(488.2358421078053),
                                                    OptionDataValue(459.70913833075736),
                                                    OptionDataValue(100.0),
                                                ),
                                            ),
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(770.3415644319939),
                                                    OptionDataValue(757.9672194986475),
                                                    OptionDataValue(30.0),
                                                ),
                                            ),
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(1180.0329284196291),
                                                    OptionDataValue(743.6141808346214),
                                                    OptionDataValue(80.0),
                                                ),
                                            ),
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(894.03790632245),
                                                    OptionDataValue(1188.1985153835008),
                                                    OptionDataValue(61.0),
                                                ),
                                            ),
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(1372.98925630313),
                                                    OptionDataValue(477.3839988649537),
                                                    OptionDataValue(70.0),
                                                ),
                                            ),
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(1378.62251255796),
                                                    OptionDataValue(935.6708486282843),
                                                    OptionDataValue(81.0),
                                                ),
                                            ),
                                        ).unsafeCast<Array<EffectScatterDataItem?>>()
                                },
                            )
                    },
                )
                null
            }
        }

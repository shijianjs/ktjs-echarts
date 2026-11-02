package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox
import demo.examples.sunburst.radius

private val docExamplePieMediaCategory = Category(Groups.DOC, "examples/doc/DocExamplePieMedia.kt")

val docExamplePieMediaEntry: CatalogEntry =
    docExamplePieMediaCategory.chart(
            "doc-example/pie-media",
            "按容器尺寸切换布局的玫瑰图",
            "Nightingale chart with media queries",
        ) {
            unsafeJso<EChartsOption> {
                baseOption =
                    unsafeJso {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "南丁格尔玫瑰图"
                                    subtext = "纯属虚构"
                                    left = StringOrDouble("center")
                                },
                            )
                        tooltip =
                            one(
                                unsafeJso<TooltipOption> {
                                    trigger = TooltipTriggers.item
                                    formatter = TooltipFormatter("{a} <br/>{b} : {c} ({d}%)")
                                },
                            )
                        legend = one(unsafeJso<LegendOption> { data = legendNames(*roseLegendNames) })
                        toolbox =
                            one(
                                unsafeJso<ToolboxOption> {
                                    feature =
                                        unsafeJso<ToolboxFeature> {
                                            dataView = unsafeJso<DataViewOption> { readOnly = false }
                                            magicType = unsafeJso<MagicTypeOption> { type = arrayOf("pie", "funnel") }
                                            restore = unsafeJso<ToolboxFeatureOption> {}
                                            saveAsImage = unsafeJso<SaveAsImageOption> {}
                                        }
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                roseSeries("半径模式", RoseTypes.radius),
                                roseSeries("面积模式", RoseTypes.area),
                            )
                    }
                media =
                    arrayOf(
                        // The unit without a query is the fallback; the container of this site is
                        // wide, so it is also what the first paint shows.
                        roseMedia(
                            null,
                            StringOrDouble("center"),
                            null,
                            StringOrDouble(0.0),
                            Orients.horizontal,
                            "25%/50%",
                            "75%/50%",
                        ),
                        roseMedia(
                            unsafeJso { minAspectRatio = 1.0 },
                            StringOrDouble("center"),
                            null,
                            StringOrDouble(0.0),
                            Orients.horizontal,
                            "25%/50%",
                            "75%/50%",
                        ),
                        roseMedia(
                            unsafeJso { maxAspectRatio = 1.0 },
                            StringOrDouble("center"),
                            null,
                            StringOrDouble(0.0),
                            Orients.horizontal,
                            "50%/30%",
                            "50%/70%",
                        ),
                        roseMedia(
                            unsafeJso { maxWidth = 500.0 },
                            StringOrDouble(10.0),
                            StringOrDouble("15%"),
                            null,
                            Orients.vertical,
                            "50%/30%",
                            "50%/75%",
                        ),
                    )
            }
        }

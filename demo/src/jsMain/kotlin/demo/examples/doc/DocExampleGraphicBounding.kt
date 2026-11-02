package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.math.PI
import demo.examples.Category
import demo.examples.dataset.transform

private val docExampleGraphicBoundingCategory = Category(Groups.DOC, "examples/doc/DocExampleGraphicBounding.kt")

val docExampleGraphicBoundingEntry: CatalogEntry =
    docExampleGraphicBoundingCategory.interactive(
            "doc-example/graphic-bounding",
            "graphic 组件的包围盒",
            "Graphic: bounding",
        ) { chart, holder ->
            val rawText =
                "这个文本框的 bounding 为 \"raw\"\n" +
                    "表示定位时仅仅取 group 自己的\n" +
                    "未经过 transform 的包围盒。"
            val allText =
                "这个文本框的 bounding 为 \"all\"\n" +
                    "表示定位时取整个 group 的\n" +
                    "经过 transform 后的包围盒。"
            chart.setOption(
                unsafeJso<EChartsOption> {
                    graphic =
                        arrayOf(
                            GraphicComponentLooseOption(
                                boundingGroup(
                                    id = "textGroup1",
                                    raw = true,
                                    left = StringOrDouble(30.0),
                                    top = StringOrDouble(30.0),
                                    bottom = null,
                                    text = rawText,
                                ),
                            ),
                            GraphicComponentLooseOption(
                                boundingGroup(
                                    id = "textGroup2",
                                    raw = false,
                                    left = StringOrDouble(0.0),
                                    top = null,
                                    bottom = StringOrDouble(0.0),
                                    text = allText,
                                ),
                            ),
                        )
                },
            )
            var rotation = 0.0
            holder.setInterval({
                rotation = (rotation + PI / 300.0) % (PI * 2.0)
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        // Elements are merged by id, so only the rotation is updated.
                        graphic =
                            arrayOf(
                                GraphicComponentLooseOption(
                                    unsafeJso<GraphicElement> {
                                        id = "textGroup1"
                                        this.rotation = rotation
                                    },
                                ),
                                GraphicComponentLooseOption(
                                    unsafeJso<GraphicElement> {
                                        id = "textGroup2"
                                        this.rotation = rotation
                                    },
                                ),
                            )
                    },
                )
            }, 20)
        }

package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.chord.column
import demo.examples.shared.every

private val scatterLabelAlignRightCategory = Category(Groups.SCATTER, "examples/scatter/ScatterLabelAlignRight.kt")

val scatterLabelAlignRightEntry: CatalogEntry =
    scatterLabelAlignRightCategory.interactive(
            "scatter-label-align-right",
            "散点图标签右侧对齐",
            "Align Label on the Right",
            "同一份数据，标签排到图表右侧的一列上：`labelLayout` 是回调，位置取自 `chart.getWidth()`。",
            "The same data with every label parked in a column on the right; `labelLayout` is a " +
                "callback that reads the chart width.",
        ) { chart ->
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { splitLine = unsafeJso<SplitLineOption> { show = false } })
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                splitLine = unsafeJso<SplitLineOption> { show = false }
                                scale = true
                            },
                        )
                    grid =
                        one(
                            unsafeJso<GridOption> {
                                left = StringOrDouble(40.0)
                                right = StringOrDouble(130.0)
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            scatterSeries {
                                name = "1990"
                                typedData = LIFE_ROWS_1990
                                symbolSize = lifeSymbolSize
                                emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.self }
                                labelLayout =
                                    LabelLayout(
                                        { _: LabelLayoutCallbackParams ->
                                            unsafeJso<LabelLayoutOption> {
                                                x = StringOrDouble(chart.getWidth() - 100)
                                                moveOverlap = MoveOverlaps.shiftY
                                            }
                                        } as LabelLayoutCallback,
                                    )
                                labelLine = lifeLabelLine()
                                label =
                                    unsafeJso<LabelOption> {
                                        show = true
                                        formatter = LabelFormatter(countryLabel)
                                        position = LabelPosition("right")
                                        minMargin = 2.0
                                    }
                            },
                        )
                },
            )
        }

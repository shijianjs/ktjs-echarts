package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.lineSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val lineStepCategory = Category(Groups.LINE, "examples/line/LineStep.kt")

val lineStepEntry: CatalogEntry =
    lineStepCategory.chart("line-step", "阶梯折线图", "Step Line") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Step Line" })
                tooltip = axisTooltip
                legend = one(unsafeJso<LegendOption> { data = legendNames("Step Start", "Step Middle", "Step End") })
                grid = one(unsafeJso<GridOption> { left = StringOrDouble("3%"); right = StringOrDouble("4%"); bottom = StringOrDouble("3%"); containLabel = true })
                toolbox = toolbox(saveAsImage = true)
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        lineSeries { name = "Step Start"; step = io.github.shijianjs.ktjs.echarts.options.BooleanOrString("start"); typedData = numbers(120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0) },
                        lineSeries { name = "Step Middle"; step = io.github.shijianjs.ktjs.echarts.options.BooleanOrString("middle"); typedData = numbers(220.0, 282.0, 201.0, 234.0, 290.0, 430.0, 410.0) },
                        lineSeries { name = "Step End"; step = io.github.shijianjs.ktjs.echarts.options.BooleanOrString("end"); typedData = numbers(450.0, 432.0, 401.0, 454.0, 590.0, 530.0, 510.0) },
                    )
            }
        }

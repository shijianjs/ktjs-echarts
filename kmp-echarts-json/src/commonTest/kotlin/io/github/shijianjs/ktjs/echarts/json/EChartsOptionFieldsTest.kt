package io.github.shijianjs.ktjs.echarts.json

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonObject

/** Field-level serialization coverage for [EChartsOption]. */
class EChartsOptionFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun eChartsOptionFields() {
        val value = EChartsOption()
        value.animation = true
        value.animationThreshold = 1.0
        value.animationDuration = 1.0
        value.animationEasing = "s"
        value.animationDelay = 1.0
        value.animationDurationUpdate = 1.0
        value.animationEasingUpdate = "s"
        value.animationDelayUpdate = 1.0
        value.backgroundColor = "red".asColor()
        value.color = listOf("red".asColor())
        value.textStyle = TextStyleOption()
        value.darkMode = BooleanOrString.of(true)
        value.useUTC = true
        value.hoverLayerThreshold = 1.0
        value.blendMode = "s"
        value.title = listOf(TitleOption())
        value.tooltip = listOf(TooltipOption())
        value.axisPointer = AxisPointerOption()
        value.legend = listOf(LegendOption())
        value.grid = listOf(GridOption())
        value.xAxis = listOf(AxisOption())
        value.yAxis = listOf(AxisOption())
        value.polar = listOf(PolarOption())
        value.radiusAxis = listOf(AxisOption())
        value.angleAxis = listOf(AxisOption())
        value.radar = listOf(RadarOption())
        value.singleAxis = listOf(AxisOption())
        value.parallel = listOf(ParallelOption())
        value.parallelAxis = listOf(AxisOption())
        value.calendar = listOf(CalendarOption())
        value.geo = listOf(GeoOption())
        value.dataZoom = listOf(DataZoomOption())
        value.visualMap = listOf(VisualMapOption())
        value.toolbox = listOf(ToolboxOption())
        value.brush = BrushOption()
        value.dataset = listOf(DatasetOption())
        value.timeline = TimelineOption()
        value.graphic = GraphicOption()
        value.aria = AriaOption()
        value.series = listOf(SeriesOption(SeriesType.Line))
        value.options = listOf(EChartsOption())
        value.baseOption = EChartsOption()
        value.media = listOf(MediaOption())
        assertEquals(
            setOf(
                "animation",
                "animationThreshold",
                "animationDuration",
                "animationEasing",
                "animationDelay",
                "animationDurationUpdate",
                "animationEasingUpdate",
                "animationDelayUpdate",
                "backgroundColor",
                "color",
                "textStyle",
                "darkMode",
                "useUTC",
                "hoverLayerThreshold",
                "blendMode",
                "title",
                "tooltip",
                "axisPointer",
                "legend",
                "grid",
                "xAxis",
                "yAxis",
                "polar",
                "radiusAxis",
                "angleAxis",
                "radar",
                "singleAxis",
                "parallel",
                "parallelAxis",
                "calendar",
                "geo",
                "dataZoom",
                "visualMap",
                "toolbox",
                "brush",
                "dataset",
                "timeline",
                "graphic",
                "aria",
                "series",
                "options",
                "baseOption",
                "media",
            ),
            value.encodeWith(EChartsOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun mediaOptionFields() {
        val value = MediaOption()
        value.query = JsonObject(emptyMap())
        value.option = EChartsOption()
        assertEquals(
            setOf(
                "query",
                "option",
            ),
            value.encodeWith(MediaOption.serializer()).keys,
        )
    }
}

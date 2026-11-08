package io.github.shijianjs.ktjs.echarts.json

import kotlin.test.Test
import kotlin.test.assertEquals

/** Field-level serialization coverage for [DataZooms]. */
class DataZoomsFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun dataBackgroundOptionFields() {
        val value = DataBackgroundOption()
        value.lineStyle = LineStyleOption()
        value.areaStyle = AreaStyleOption()
        assertEquals(
            setOf(
                "lineStyle",
                "areaStyle",
            ),
            value.encodeWith(DataBackgroundOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun dataZoomOptionFields() {
        val value = DataZoomOption()
        value.type = DataZoomType.Inside
        value.id = "s"
        value.show = true
        value.backgroundColor = "red".asColor()
        value.dataBackground = DataBackgroundOption()
        value.selectedDataBackground = DataBackgroundOption()
        value.fillerColor = "red".asColor()
        value.borderColor = "red".asColor()
        value.handleIcon = "s"
        value.handleSize = NumberOrStringOrNumbers.of(1.0)
        value.handleStyle = ItemStyleOption()
        value.moveHandleIcon = "s"
        value.moveHandleSize = 1.0
        value.moveHandleStyle = ItemStyleOption()
        value.labelPrecision = num(1.0)
        value.labelFormatter = "s"
        value.showDetail = true
        value.showDataShadow = BooleanOrString.of(true)
        value.realtime = true
        value.zoomLock = true
        value.textStyle = TextStyleOption()
        value.brushSelect = true
        value.brushStyle = ItemStyleOption()
        value.orient = Orient.Horizontal
        value.xAxisIndex = listOf(1)
        value.yAxisIndex = listOf(1)
        value.radiusAxisIndex = listOf(1)
        value.angleAxisIndex = listOf(1)
        value.filterMode = "s"
        value.start = 1.0
        value.end = 1.0
        value.startValue = num(1.0)
        value.endValue = num(1.0)
        value.minValueSpan = num(1.0)
        value.maxValueSpan = num(1.0)
        value.throttle = 1.0
        value.zoomOnMouseWheel = BooleanOrString.of(true)
        value.moveOnMouseMove = BooleanOrString.of(true)
        value.moveOnMouseWheel = BooleanOrString.of(true)
        value.preventDefaultMouseMove = true
        value.z = 1.0
        value.zlevel = 1.0
        value.left = num(1.0)
        value.top = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        value.width = num(1.0)
        value.height = num(1.0)
        value.emphasis = EmphasisOption()
        assertEquals(
            setOf(
                "type",
                "id",
                "show",
                "backgroundColor",
                "dataBackground",
                "selectedDataBackground",
                "fillerColor",
                "borderColor",
                "handleIcon",
                "handleSize",
                "handleStyle",
                "moveHandleIcon",
                "moveHandleSize",
                "moveHandleStyle",
                "labelPrecision",
                "labelFormatter",
                "showDetail",
                "showDataShadow",
                "realtime",
                "zoomLock",
                "textStyle",
                "brushSelect",
                "brushStyle",
                "orient",
                "xAxisIndex",
                "yAxisIndex",
                "radiusAxisIndex",
                "angleAxisIndex",
                "filterMode",
                "start",
                "end",
                "startValue",
                "endValue",
                "minValueSpan",
                "maxValueSpan",
                "throttle",
                "zoomOnMouseWheel",
                "moveOnMouseMove",
                "moveOnMouseWheel",
                "preventDefaultMouseMove",
                "z",
                "zlevel",
                "left",
                "top",
                "right",
                "bottom",
                "width",
                "height",
                "emphasis",
            ),
            value.encodeWith(DataZoomOption.serializer()).keys,
        )
    }
}

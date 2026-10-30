package io.github.shijianjs.kmpechartsjson

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/** Field-level serialization coverage for [Series]. */
class SeriesFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun seriesOptionFields() {
        val value = SeriesOption(SeriesType.Line)
        value.type = SeriesType.Line
        value.id = "s"
        value.name = "s"
        value.colorBy = "s"
        value.legendHoverLink = true
        value.coordinateSystem = "s"
        value.xAxisIndex = 1
        value.yAxisIndex = 1
        value.polarIndex = 1
        value.radiusAxisIndex = 1
        value.angleAxisIndex = 1
        value.geoIndex = 1
        value.calendarIndex = 1
        value.parallelIndex = 1
        value.singleAxisIndex = 1
        value.datasetIndex = 1
        value.datasetId = "s"
        value.data = listOf(valueOf(1.0))
        value.dimensions = listOf(JsonPrimitive("x"))
        value.encode = JsonObject(emptyMap())
        value.seriesLayoutBy = "s"
        value.links = listOf(valueOf(1.0))
        value.nodes = listOf(valueOf(1.0))
        value.categories = listOf(DataItem())
        value.levels = listOf(JsonPrimitive("x"))
        value.dataGroupId = "s"
        value.layout = "s"
        value.center = listOf(num(1.0))
        value.radius = NumberOrStringOrNumbers.of(1.0)
        value.startAngle = 1.0
        value.endAngle = 1.0
        value.clockwise = true
        value.roseType = BooleanOrString.of(true)
        value.minAngle = 1.0
        value.padAngle = 1.0
        value.minShowLabelAngle = 1.0
        value.avoidLabelOverlap = true
        value.stillShowZeroSum = true
        value.percentPrecision = 1
        value.selectedMode = BooleanOrString.of(true)
        value.selectedOffset = 1.0
        value.orient = Orient.Horizontal
        value.top = num(1.0)
        value.left = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        value.width = num(1.0)
        value.height = num(1.0)
        value.smooth = BooleanOrNumber.of(1.0)
        value.smoothMonotonicity = "s"
        value.step = BooleanOrString.of(true)
        value.connectNulls = true
        value.clip = true
        value.sampling = "s"
        value.stack = "s"
        value.stackStrategy = "s"
        value.showSymbol = true
        value.showAllSymbol = BooleanOrString.of(true)
        value.symbol = "s"
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.symbolRotate = 1.0
        value.symbolKeepAspect = true
        value.symbolOffset = listOf(num(1.0))
        value.lineStyle = LineStyleOption()
        value.areaStyle = AreaStyleOption()
        value.endLabel = LabelOption()
        value.barWidth = num(1.0)
        value.barMaxWidth = num(1.0)
        value.barMinWidth = num(1.0)
        value.barGap = "s"
        value.barCategoryGap = "s"
        value.barMinHeight = 1.0
        value.roundCap = true
        value.showBackground = true
        value.backgroundStyle = ItemStyleOption()
        value.realtimeSort = true
        value.symbolPosition = "s"
        value.symbolRepeat = BooleanOrNumber.of(1.0)
        value.symbolRepeatDirection = "s"
        value.symbolMargin = num(1.0)
        value.symbolClip = true
        value.symbolBoundingData = 1.0
        value.symbolPatternSize = 1.0
        value.large = true
        value.largeThreshold = 1.0
        value.progressive = BooleanOrNumber.of(1.0)
        value.progressiveThreshold = 1.0
        value.progressiveChunkMode = "s"
        value.rippleEffect = RippleEffectOption()
        value.showEffectOn = "s"
        value.hoverAnimation = true
        value.roam = BooleanOrString.of(true)
        value.draggable = true
        value.nodeScaleRatio = 1.0
        value.edgeSymbol = listOf("s")
        value.edgeSymbolSize = NumberOrStringOrNumbers.of(1.0)
        value.edgeLabel = LabelOption()
        value.force = ForceOption()
        value.circular = JsonPrimitive("x")
        value.nodeClick = BooleanOrString.of(true)
        value.autoCurveness = BooleanOrNumber.of(1.0)
        value.edgeShape = "s"
        value.initialTreeDepth = 1
        value.expandAndCollapse = true
        value.leaves = LabelOption()
        value.leafDepth = 1
        value.visibleMin = 1.0
        value.childrenVisibleMin = 1.0
        value.squareRatio = 1.0
        value.zoomToNodeRatio = 1.0
        value.upperLabel = LabelOption()
        value.breadcrumb = JsonPrimitive("x")
        value.sort = BooleanOrString.of(true)
        value.nodeWidth = 1.0
        value.nodeGap = 1.0
        value.nodeAlign = "s"
        value.layoutIterations = 1
        value.min = 1.0
        value.max = 1.0
        value.splitNumber = 1
        value.axisLine = AxisLineOption()
        value.axisTick = AxisTickOption()
        value.splitLine = SplitLineOption()
        value.axisLabel = AxisLabelOption()
        value.pointer = JsonPrimitive("x")
        value.anchor = JsonPrimitive("x")
        value.title = LabelOption()
        value.detail = LabelOption()
        value.progress = GaugeProgressOption()
        value.minSize = num(1.0)
        value.maxSize = num(1.0)
        value.gap = 1.0
        value.funnelAlign = "s"
        value.pointSize = 1.0
        value.blurSize = 1.0
        value.minOpacity = 1.0
        value.maxOpacity = 1.0
        value.boxWidth = NumberOrStringOrNumbers.of(1.0)
        value.boxGap = num(1.0)
        value.map = "s"
        value.nameMap = mapOf("k" to "s")
        value.scaleLimit = ScaleLimit()
        value.regions = listOf(GeoRegion())
        value.aspectScale = 1.0
        value.boundingCoords = listOf(listOf(1.0))
        value.layoutCenter = listOf(num(1.0))
        value.layoutSize = num(1.0)
        value.polyline = true
        value.effect = LinesEffectOption()
        value.itemStyle = ItemStyleOption()
        value.label = LabelOption()
        value.labelLine = LabelLineOption()
        value.labelLayout = JsonPrimitive("x")
        value.emphasis = EmphasisOption()
        value.blur = EmphasisOption()
        value.select = EmphasisOption()
        value.markPoint = MarkPointOption()
        value.markLine = MarkLineOption()
        value.markArea = MarkAreaOption()
        value.z = 1.0
        value.zlevel = 1.0
        value.silent = true
        value.cursor = "s"
        value.blendMode = "s"
        value.tooltip = TooltipOption()
        value.universalTransition = JsonPrimitive("x")
        value.stateAnimation = JsonPrimitive("x")
        value.animation = true
        value.animationThreshold = 1.0
        value.animationDuration = 1.0
        value.animationEasing = "s"
        value.animationDelay = 1.0
        value.animationDurationUpdate = 1.0
        value.animationEasingUpdate = "s"
        value.animationDelayUpdate = 1.0
        assertEquals(
            setOf(
                "type",
                "id",
                "name",
                "colorBy",
                "legendHoverLink",
                "coordinateSystem",
                "xAxisIndex",
                "yAxisIndex",
                "polarIndex",
                "radiusAxisIndex",
                "angleAxisIndex",
                "geoIndex",
                "calendarIndex",
                "parallelIndex",
                "singleAxisIndex",
                "datasetIndex",
                "datasetId",
                "data",
                "dimensions",
                "encode",
                "seriesLayoutBy",
                "links",
                "nodes",
                "categories",
                "levels",
                "dataGroupId",
                "layout",
                "center",
                "radius",
                "startAngle",
                "endAngle",
                "clockwise",
                "roseType",
                "minAngle",
                "padAngle",
                "minShowLabelAngle",
                "avoidLabelOverlap",
                "stillShowZeroSum",
                "percentPrecision",
                "selectedMode",
                "selectedOffset",
                "orient",
                "top",
                "left",
                "right",
                "bottom",
                "width",
                "height",
                "smooth",
                "smoothMonotonicity",
                "step",
                "connectNulls",
                "clip",
                "sampling",
                "stack",
                "stackStrategy",
                "showSymbol",
                "showAllSymbol",
                "symbol",
                "symbolSize",
                "symbolRotate",
                "symbolKeepAspect",
                "symbolOffset",
                "lineStyle",
                "areaStyle",
                "endLabel",
                "barWidth",
                "barMaxWidth",
                "barMinWidth",
                "barGap",
                "barCategoryGap",
                "barMinHeight",
                "roundCap",
                "showBackground",
                "backgroundStyle",
                "realtimeSort",
                "symbolPosition",
                "symbolRepeat",
                "symbolRepeatDirection",
                "symbolMargin",
                "symbolClip",
                "symbolBoundingData",
                "symbolPatternSize",
                "large",
                "largeThreshold",
                "progressive",
                "progressiveThreshold",
                "progressiveChunkMode",
                "rippleEffect",
                "showEffectOn",
                "hoverAnimation",
                "roam",
                "draggable",
                "nodeScaleRatio",
                "edgeSymbol",
                "edgeSymbolSize",
                "edgeLabel",
                "force",
                "circular",
                "nodeClick",
                "autoCurveness",
                "edgeShape",
                "initialTreeDepth",
                "expandAndCollapse",
                "leaves",
                "leafDepth",
                "visibleMin",
                "childrenVisibleMin",
                "squareRatio",
                "zoomToNodeRatio",
                "upperLabel",
                "breadcrumb",
                "sort",
                "nodeWidth",
                "nodeGap",
                "nodeAlign",
                "layoutIterations",
                "min",
                "max",
                "splitNumber",
                "axisLine",
                "axisTick",
                "splitLine",
                "axisLabel",
                "pointer",
                "anchor",
                "title",
                "detail",
                "progress",
                "minSize",
                "maxSize",
                "gap",
                "funnelAlign",
                "pointSize",
                "blurSize",
                "minOpacity",
                "maxOpacity",
                "boxWidth",
                "boxGap",
                "map",
                "nameMap",
                "scaleLimit",
                "regions",
                "aspectScale",
                "boundingCoords",
                "layoutCenter",
                "layoutSize",
                "polyline",
                "effect",
                "itemStyle",
                "label",
                "labelLine",
                "labelLayout",
                "emphasis",
                "blur",
                "select",
                "markPoint",
                "markLine",
                "markArea",
                "z",
                "zlevel",
                "silent",
                "cursor",
                "blendMode",
                "tooltip",
                "universalTransition",
                "stateAnimation",
                "animation",
                "animationThreshold",
                "animationDuration",
                "animationEasing",
                "animationDelay",
                "animationDurationUpdate",
                "animationEasingUpdate",
                "animationDelayUpdate",
            ),
            value.encodeWith(SeriesOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun gaugeProgressOptionFields() {
        val value = GaugeProgressOption()
        value.show = true
        value.width = 1.0
        value.roundCap = true
        value.itemStyle = ItemStyleOption()
        assertEquals(
            setOf(
                "show",
                "width",
                "roundCap",
                "itemStyle",
            ),
            value.encodeWith(GaugeProgressOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun linesEffectOptionFields() {
        val value = LinesEffectOption()
        value.show = true
        value.period = 1.0
        value.trailLength = 1.0
        value.symbol = "s"
        value.symbolSize = NumberOrStringOrNumbers.of(1.0)
        value.color = "red".asColor()
        value.constantSpeed = 1.0
        assertEquals(
            setOf(
                "show",
                "period",
                "trailLength",
                "symbol",
                "symbolSize",
                "color",
                "constantSpeed",
            ),
            value.encodeWith(LinesEffectOption.serializer()).keys,
        )
    }
}

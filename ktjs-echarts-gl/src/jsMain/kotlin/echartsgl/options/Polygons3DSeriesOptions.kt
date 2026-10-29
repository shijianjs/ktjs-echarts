package echartsgl.options

import echarts.options.OptionDataItemObject
import echarts.options.OptionDataValueOrArray
import echarts.options.SeriesOption
import echartsgl.CoordinateSystem3D
import kotlin.js.JsName
import kotlin.js.unsafeCast

/**
 * Coordinates of one polygon of a [Polygons3DSeriesOption].
 *
 * A polygon is a list of rings: the first ring is the outline, the others are holes. What the series
 * accepts depends on [Polygons3DSeriesOption.multiPolygon]: one polygon, or a list of polygons when
 * it is `true`.
 */
external interface Polygons3DCoords

/** Wraps one polygon: its outline ring followed by its holes. */
fun Polygons3DCoords(rings: Array<Array<Array<Double>>>): Polygons3DCoords =
    rings.unsafeCast<Polygons3DCoords>()

/** Wraps several polygons, for a series with [Polygons3DSeriesOption.multiPolygon] set. */
fun Polygons3DCoords(polygons: Array<Array<Array<Array<Double>>>>): Polygons3DCoords =
    polygons.unsafeCast<Polygons3DCoords>()

/**
 * One polygon of a [Polygons3DSeriesOption].
 */
external interface Polygons3DDataItemOption : OptionDataItemObject {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `OptionDataValue[]`; the value is what a `visualMap` component maps to the color.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Coordinates of the polygon, or of the polygons of the item. */
    var coords: Polygons3DCoords?

    /** Height the polygon is extruded to; `regionHeight` of the parent component by default. */
    var height: Double?

    /** Style of the polygon. */
    var itemStyle: GlItemStyleOption?

    /** Label of the polygon. */
    var label: GlLabelOption?

    /** Emphasis state of the polygon. */
    var emphasis: Geo3DEmphasisOption?
}

/**
 * One entry of [Polygons3DSeriesOption.typedData].
 *
 * Upstream `Polygons3DDataItemOption`; the coordinates alone are accepted as well, but an item
 * without a `height` and a style is rarely useful.
 */
external interface Polygons3DDataItem

/** Wraps a data item object. */
fun Polygons3DDataItem(value: Polygons3DDataItemOption): Polygons3DDataItem =
    value.unsafeCast<Polygons3DDataItem>()

/**
 * Extruded polygons series, `series.type = "polygons3D"`.
 *
 * Every item is a polygon of the GeoJSON of a `geo3D` component, or of the map service behind a
 * `mapbox3D` / `maptalks3D` component, extruded to its own height. It is how large sets of
 * buildings are drawn.
 *
 * The upstream default of [multiPolygon] is `false`, although the option documentation says `true`.
 */
external interface Polygons3DSeriesOption : SeriesOption, ShadingOption {
    /** Coordinate system: `"geo3D"`, `"mapbox3D"` or `"maptalks3D"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [Bar3DSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CoordinateSystem3D?

    /** Index of the `geo3D` component the series is drawn in. */
    var geo3DIndex: Double?

    /** Index of the `mapbox3D` component the series is drawn in. */
    var mapbox3DIndex: Double?

    /** Index of the `maptalks3D` component the series is drawn in. */
    var maptalks3DIndex: Double?

    /** Whether the coordinates of an item are a list of polygons instead of a single polygon. */
    var multiPolygon: Boolean?

    /** Label of the polygons. */
    @JsName("label")
    var typedLabel: GlLabelOption?

    /** Style of the polygons. */
    @JsName("itemStyle")
    var typedItemStyle: GlItemStyleOption?

    /** Emphasis state of the polygons. */
    @JsName("emphasis")
    var typedEmphasis: Geo3DEmphasisOption?

    /** Wireframe debug overlay. */
    var debug: GlDebugOption?

    /** Typed view of [SeriesOption.data] with the item type of an extruded polygon. */
    @JsName("data")
    var typedData: Array<Polygons3DDataItem?>?
}

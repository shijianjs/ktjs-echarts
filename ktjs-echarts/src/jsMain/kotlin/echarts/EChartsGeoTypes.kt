package echarts

import echarts.options.OptionId
import js.objects.Record
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import web.dom.Document
import web.svg.SVGElement
import zrender.ZRBoundingRect

/**
 * Mapping from a region name in the map data to the name shown on the chart.
 */
typealias NameMap = Record<String, String>

/**
 * Offset and scale of one region inside a composed map.
 */
external interface GeoSpecialArea {
    /** Horizontal offset of the region. */
    var left: Double

    /** Vertical offset of the region. */
    var top: Double

    /** Width of the region. */
    var width: Double?

    /** Height of the region. */
    var height: Double?
}

/**
 * Special areas of a composed map, keyed by region name.
 */
typealias GeoSpecialAreas = Record<String, GeoSpecialArea>

/**
 * Properties of a GeoJSON feature.
 *
 * Upstream allows arbitrary additional keys; access them through a JavaScript cast.
 */
external interface GeoJSONProperties {
    /** Name of the region. */
    var name: String?

    /** Coordinates of the label anchor point. */
    var cp: Array<Double>?
}

/**
 * One feature of a GeoJSON collection.
 */
external interface GeoJSONFeature {
    /** Always `"Feature"`. */
    var type: String?

    /** Feature id. */
    var id: OptionId?

    /** Properties of the feature. */
    var properties: GeoJSONProperties?

    /** Geometry of the feature. */
    var geometry: GeoJSONGeometry?
}

/**
 * Geometry of a GeoJSON feature.
 *
 * `coordinates` can be a flat array, an array of arrays or a compressed string depending on [type].
 */
external interface GeoJSONGeometry {
    /** Geometry type, e.g. `"Polygon"` or `"MultiPolygon"`. */
    var type: String?

    /** Coordinates of the geometry. */
    var coordinates: JsAny?

    /** Offsets of a compressed geometry. */
    var encodeOffsets: JsAny?
}

/**
 * A GeoJSON feature collection accepted by `registerMap`.
 */
external interface GeoJSON {
    /** Always `"FeatureCollection"`. */
    var type: String?

    /** Features of the collection. */
    var features: Array<GeoJSONFeature>?

    /** Whether the coordinates are UTF-8 encoded. */
    var UTF8Encoding: Boolean?

    /** Scale of the UTF-8 encoded coordinates. */
    var UTF8Scale: Double?
}

/**
 * Source of a GeoJSON map: an URL or a parsed GeoJSON object.
 */
external interface GeoJSONSourceInput

/** Wraps an URL or a raw JSON string. */
fun GeoJSONSourceInput(value: String): GeoJSONSourceInput = value.unsafeCast<GeoJSONSourceInput>()

/** Wraps a parsed GeoJSON object. */
fun GeoJSONSourceInput(value: GeoJSON): GeoJSONSourceInput = value.unsafeCast<GeoJSONSourceInput>()

/**
 * Source of an SVG map: an URL, a document or an SVG element.
 */
external interface GeoSVGSourceInput

/** Wraps an URL or a raw SVG string. */
fun GeoSVGSourceInput(value: String): GeoSVGSourceInput = value.unsafeCast<GeoSVGSourceInput>()

/** Wraps a document. */
fun GeoSVGSourceInput(value: Document): GeoSVGSourceInput = value.unsafeCast<GeoSVGSourceInput>()

/** Wraps an SVG element. */
fun GeoSVGSourceInput(value: SVGElement): GeoSVGSourceInput = value.unsafeCast<GeoSVGSourceInput>()

/**
 * One region produced by [ECharts.parseGeoJSON].
 */
external interface GeoJSONRegion {
    /** Name of the region. */
    val name: String?

    /** Coordinates of the label anchor point. */
    val cp: Array<Double>?

    /** Bounding rectangle of the region. */
    fun getBoundingRect(): ZRBoundingRect
}

/**
 * Map definition accepted by [ECharts.registerMap]: a GeoJSON or SVG payload with the extra
 * information the geo coordinate system needs.
 */
external interface MapInput

/** Wraps a GeoJSON map definition. */
fun MapInput(value: GeoJSONMapInput): MapInput = value.unsafeCast<MapInput>()

/** Wraps an SVG map definition. */
fun MapInput(value: SVGMapInput): MapInput = value.unsafeCast<MapInput>()

/**
 * GeoJSON map definition: the GeoJSON itself plus the offsets of the composed areas.
 */
external interface GeoJSONMapInput {
    /** GeoJSON object or the URL of one. */
    var geoJSON: GeoJSONSourceInput

    /** Offsets and scales of the areas of a composed map. */
    var specialAreas: GeoSpecialAreas?
}

/**
 * SVG map definition.
 */
external interface SVGMapInput {
    /** SVG element, document or the URL of one. */
    var svg: GeoSVGSourceInput
}

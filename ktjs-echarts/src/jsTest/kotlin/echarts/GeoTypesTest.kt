package echarts

import js.objects.recordOf
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import web.dom.Document
import web.svg.SVGElement

/**
 * Covers the Geo/GeoJSON/SVG map types in `EChartsGeoTypes.kt`: the compressed-coordinate fields of
 * a [GeoJSON], the label anchor of a feature, the composed-map offsets, and the three
 * [GeoSVGSourceInput] factory overloads.
 */
class GeoTypesTest {

    @Test
    fun geoJsonCompressedFieldsAndSpecialAreasRoundTrip() {
        val properties = unsafeJso<GeoJSONProperties> {
            name = "Afghanistan"
            cp = arrayOf(66.0, 33.0)
        }
        val geometry = unsafeJso<GeoJSONGeometry> {
            type = "Polygon"
            coordinates = arrayOf(
                arrayOf(
                    arrayOf(0.0, 0.0),
                    arrayOf(0.0, 1.0),
                    arrayOf(1.0, 1.0),
                    arrayOf(0.0, 0.0),
                ),
            ).unsafeCast<JsAny>()
            encodeOffsets = arrayOf(arrayOf(0.0, 0.0)).unsafeCast<JsAny>()
        }
        val feature = unsafeJso<GeoJSONFeature> {
            type = "Feature"
            this.properties = properties
            this.geometry = geometry
        }
        val geoJson = unsafeJso<GeoJSON> {
            type = "FeatureCollection"
            features = arrayOf(feature)
            UTF8Encoding = false
            UTF8Scale = 1024.0
        }

        assertEquals(false, geoJson.UTF8Encoding)
        assertEquals(1024.0, geoJson.UTF8Scale)
        assertEquals("Afghanistan", properties.name)
        assertNotNull(properties.cp)
        assertEquals(66.0, properties.cp!![0])
        assertNotNull(geometry.encodeOffsets)

        val specialArea = unsafeJso<GeoSpecialArea> {
            left = 0.1
            top = 0.2
            width = 0.5
            height = 0.6
        }
        val mapInput = unsafeJso<GeoJSONMapInput> {
            geoJSON = GeoJSONSourceInput(geoJson)
            specialAreas = recordOf("Afghanistan" to specialArea)
        }
        ECharts.registerMap("wrapper-geo-types-map", MapInput(mapInput))

        assertNotNull(ECharts.getMap("wrapper-geo-types-map"))
        val registered = mapInput.specialAreas!!["Afghanistan"]
        assertNotNull(registered)
        assertEquals(0.5, registered.width)
        assertEquals(0.6, registered.height)
    }

    @Test
    fun geoJsonSourceInputWrapsStringsAndObjects() {
        val url = GeoJSONSourceInput("https://example.test/china.geojson")
        assertEquals("https://example.test/china.geojson", url.unsafeCast<String>())

        val geoJson = unsafeJso<GeoJSON> {
            type = "FeatureCollection"
            UTF8Encoding = true
            UTF8Scale = 64.0
        }
        val fromObject = GeoJSONSourceInput(geoJson)
        assertTrue(fromObject.unsafeCast<GeoJSON>() === geoJson)
        assertEquals(64.0, fromObject.unsafeCast<GeoJSON>().UTF8Scale)
    }

    @Test
    fun geoSvgSourceInputFactoriesWrapTheirValues() {
        val markup = GeoSVGSourceInput("<svg><path/></svg>")
        assertEquals("<svg><path/></svg>", markup.unsafeCast<String>())

        val doc: Document = web.dom.document
        val fromDocument = GeoSVGSourceInput(doc)
        assertTrue(fromDocument.unsafeCast<Document>() === doc)

        val svgElement: SVGElement =
            doc.createElementNS("http://www.w3.org/2000/svg", "svg").unsafeCast<SVGElement>()
        val fromElement = GeoSVGSourceInput(svgElement)
        assertTrue(fromElement.unsafeCast<SVGElement>() === svgElement)
    }
}

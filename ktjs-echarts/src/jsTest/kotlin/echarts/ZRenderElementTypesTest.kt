package echarts

import echarts.options.StringOrDoubleOrArray
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import web.canvas.CanvasLineCap
import web.canvas.CanvasLineJoin
import zrender.BoundingRectIntersectOpt
import zrender.ElementTextConfig
import zrender.RectLike
import zrender.StringOrImageLike
import zrender.TextOverflows
import zrender.ZRColor
import zrender.ZRPointLike
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import zrender.ZRElement
import zrender.ZRElementEvent

/**
 * Covers the zrender element geometry/style types in `ZRenderElementTypes.kt`: the options that
 * drive [zrender.ZRBoundingRect.intersect], the ellipse/bezier/compound-path shape fields, the text
 * placement and style fields, and the element/event flags.
 */
class ZRenderElementTypesTest {

    private fun squareGeoJson(): GeoJSON = unsafeJso<GeoJSON> {
        type = "FeatureCollection"
        features = arrayOf(
            unsafeJso<GeoJSONFeature> {
                type = "Feature"
                properties = unsafeJso<GeoJSONProperties> { name = "Square" }
                geometry = unsafeJso<GeoJSONGeometry> {
                    type = "Polygon"
                    coordinates = arrayOf(
                        arrayOf(
                            arrayOf(0.0, 0.0),
                            arrayOf(0.0, 1.0),
                            arrayOf(1.0, 1.0),
                            arrayOf(0.0, 0.0),
                        ),
                    ).unsafeCast<JsAny>()
                }
            },
        )
    }

    @Test
    fun boundingRectIntersectOptionsDriveTheGeometryHelper() {
        val regions = ECharts.parseGeoJSON(squareGeoJson(), "name")
        assertEquals(1, regions.size)
        val rect = regions[0].getBoundingRect()

        val mtv = unsafeJso<ZRPointLike>()
        val out = unsafeJso<RectLike>()
        val opt = unsafeJso<BoundingRectIntersectOpt> {
            direction = 0.0
            bidirectional = true
            touchThreshold = 1.0
            outIntersectRect = out
            clamp = false
        }

        assertEquals(0.0, opt.direction)
        assertEquals(true, opt.bidirectional)
        assertEquals(1.0, opt.touchThreshold)
        assertEquals(false, opt.clamp)
        assertTrue(opt.outIntersectRect === out)

        // Intersect without the opt: the geometry-only path, used to prove the wrapper can hand a
        // real parsed region to the helper. `mtv` receives the minimum translation vector; the
        // boolean return is the plain overlap test.
        val other = unsafeJso<RectLike> {
            x = rect.x
            y = rect.y
            width = rect.width
            height = rect.height
        }
        assertTrue(rect.intersect(other, mtv))
    }

    @Test
    fun zrShapeAndStylePropsRoundTrip() {
        val subPath = unsafeJso<ZRShapeProps> {
            x = 0.0
            y = 0.0
            width = 4.0
            height = 4.0
        }
        val shape = unsafeJso<ZRShapeProps> {
            rx = 3.0
            ry = 5.0
            x1 = 0.0
            y1 = 0.0
            x2 = 1.0
            y2 = 1.0
            x3 = 2.0
            y3 = 2.0
            x4 = 3.0
            y4 = 3.0
            paths = arrayOf(subPath)
            pathData = "M0 0 L1 1"
        }
        assertEquals(3.0, shape.rx)
        assertEquals(5.0, shape.ry)
        assertEquals(2.0, shape.x3)
        assertEquals(2.0, shape.y3)
        assertEquals(3.0, shape.x4)
        assertEquals(3.0, shape.y4)
        assertEquals(1, shape.paths?.size)
        assertEquals(4.0, shape.paths?.get(0)?.width)
        assertEquals("M0 0 L1 1", shape.pathData)

        val style = unsafeJso<ZRStyleProps> {
            image = StringOrImageLike("https://example.test/a.png")
            sx = 1.0
            sy = 2.0
            sWidth = 10.0
            sHeight = 20.0
            fill = ZRColor("#fff")
            stroke = ZRColor("#000")
            strokeNoScale = true
            fillOpacity = 0.4
            strokeOpacity = 0.6
            lineCap = "round".unsafeCast<CanvasLineCap>()
            lineJoin = "bevel".unsafeCast<CanvasLineJoin>()
            miterLimit = 8.0
            strokePercent = 0.5
            strokeFirst = true
            lineHeight = 14.0
            tag = "series-0"
            overflow = TextOverflows.truncate
            ellipsis = "…"
            truncateMinChar = 3.0
            textShadowColor = "#123456"
            textShadowBlur = 2.0
            textShadowOffsetX = 1.0
            textShadowOffsetY = 3.0
        }
        assertEquals(1.0, style.sx)
        assertEquals(2.0, style.sy)
        assertEquals(10.0, style.sWidth)
        assertEquals(20.0, style.sHeight)
        assertEquals(true, style.strokeNoScale)
        assertEquals(0.4, style.fillOpacity)
        assertEquals(0.6, style.strokeOpacity)
        assertEquals("round", style.lineCap.unsafeCast<String>())
        assertEquals("bevel", style.lineJoin.unsafeCast<String>())
        assertEquals(8.0, style.miterLimit)
        assertEquals(0.5, style.strokePercent)
        assertEquals(true, style.strokeFirst)
        assertEquals(14.0, style.lineHeight)
        assertEquals("series-0", style.tag)
        assertEquals("truncate", style.overflow.unsafeCast<String>())
        assertEquals("…", style.ellipsis)
        assertEquals(3.0, style.truncateMinChar)
        assertEquals("#123456", style.textShadowColor)
        assertEquals(2.0, style.textShadowBlur)
        assertEquals(1.0, style.textShadowOffsetX)
        assertEquals(3.0, style.textShadowOffsetY)
    }

    @Test
    fun elementTextConfigAndElementFlagsRoundTrip() {
        val textConfig = unsafeJso<ElementTextConfig> {
            position = StringOrDoubleOrArray("inside")
            insideFill = "#fff"
            insideStroke = "#000"
            outsideFill = "#eee"
            outsideStroke = "#333"
            inside = true
            autoOverflowArea = true
        }
        assertEquals("#fff", textConfig.insideFill)
        assertEquals("#000", textConfig.insideStroke)
        assertEquals("#eee", textConfig.outsideFill)
        assertEquals("#333", textConfig.outsideStroke)
        assertEquals(true, textConfig.autoOverflowArea)

        val element = unsafeJso<ZRElement> {
            invisible = true
            ignore = true
            cursor = "pointer"
            this.textConfig = textConfig
        }
        assertEquals(true, element.invisible)
        assertEquals(true, element.ignore)
        assertEquals("pointer", element.cursor)
        assertNotNull(element.textConfig)

        val event = unsafeJso<ZRElementEvent> { }
        event.cancelBubble = true
        assertEquals(true, event.cancelBubble)
    }
}

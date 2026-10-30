package echarts.options

import js.objects.recordOf
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import zrender.ZRColor
import zrender.ZRFontWeight

/**
 * Tests for the toolbox component and its feature buttons.
 *
 * These fields live in `EChartsOption.kt` (`toolbox`, `toolbox.feature.*`). Upstream gives every
 * feature a shared [ToolboxFeatureOption] base plus feature-specific options, and the icons carry a
 * separate text style beyond the ordinary item style. Each case builds the option through
 * [unsafeJso] and reads every field back, so the typed property keeps mapping to the exact JavaScript
 * key and the union wrappers accept their forms.
 */
class ToolboxOptionTest {
    @Test
    fun toolboxIconStyleCarriesTextFields() {
        val iconStyle = unsafeJso<ToolboxIconStyleOption> {
            color = ZRColor("#333")
            textFill = ZRColor("#f00")
            textBackgroundColor = ZRColor("#eee")
            textPosition = LabelPosition("bottom")
            textAlign = "center"
            textBorderRadius = StringOrDoubleOrArray(4.0)
            textPadding = DoubleOrDoubleArray(arrayOf(3.0, 5.0))
            textFontFamily = "Arial"
            textFontSize = StringOrDouble(12.0)
            textFontWeight = ZRFontWeight(700.0)
            textFontStyle = "italic"
        }

        assertEquals("#333", iconStyle.color!!.unsafeCast<String>())
        assertEquals("#f00", iconStyle.textFill!!.unsafeCast<String>())
        assertEquals("#eee", iconStyle.textBackgroundColor!!.unsafeCast<String>())
        assertEquals("bottom", iconStyle.textPosition!!.unsafeCast<String>())
        assertEquals("center", iconStyle.textAlign)
        assertEquals(4.0, iconStyle.textBorderRadius!!.unsafeCast<Double>())
        assertEquals(2, iconStyle.textPadding!!.unsafeCast<Array<Double>>().size)
        assertEquals("Arial", iconStyle.textFontFamily)
        assertEquals(12.0, iconStyle.textFontSize!!.unsafeCast<Double>())
        assertEquals(700.0, iconStyle.textFontWeight!!.unsafeCast<Double>())
        assertEquals("italic", iconStyle.textFontStyle)
    }

    @Test
    fun toolboxComponentAndFeatureFieldsRoundTrip() {
        val saveAsImage = unsafeJso<SaveAsImageOption> {
            show = true
            connectedBackgroundColor = ZRColor("#000")
            pixelRatio = 2.0
            excludeComponents = arrayOf("toolbox")
            lang = arrayOf("Save Image")
        }
        val restore = unsafeJso<ToolboxFeatureOption> {
            title = StringOrDictionary("Restore")
            iconStyle = unsafeJso<ToolboxIconStyleOption> { textFill = ZRColor("#0f0") }
            iconStatus = recordOf("restore" to "active")
            onclick = {}
        }
        val dataView = unsafeJso<DataViewOption> {
            readOnly = true
            lang = arrayOf("Data View", "Close")
            textareaColor = "#ccc"
            textareaBorderColor = "#999"
            textColor = "#333"
            buttonColor = "#ddd"
            buttonTextColor = "#000"
            optionToContent = { _ -> StringOrHTMLElement("rows") }
            contentToOption = { _, old -> old }
            refreshRate = 100.0
        }
        val magicType = unsafeJso<MagicTypeOption> {
            type = arrayOf("line", "bar")
        }
        val toolbox = unsafeJso<ToolboxOption> {
            itemSize = 18.0
            showTitle = false
            zlevel = 2.0
            iconStyle = unsafeJso<ToolboxIconStyleOption> { textFill = ZRColor("#00f") }
            feature = unsafeJso<ToolboxFeature> {
                this.saveAsImage = saveAsImage
                this.restore = restore
                this.dataView = dataView
                this.magicType = magicType
            }
        }

        assertEquals(18.0, toolbox.itemSize)
        assertEquals(false, toolbox.showTitle)
        assertEquals(2.0, toolbox.zlevel)
        assertEquals("#00f", toolbox.iconStyle!!.textFill!!.unsafeCast<String>())

        val feature = assertNotNull(toolbox.feature)
        assertEquals("#000", assertNotNull(feature.saveAsImage).connectedBackgroundColor!!.unsafeCast<String>())
        assertEquals(2.0, feature.saveAsImage!!.pixelRatio)
        assertEquals(1, feature.saveAsImage!!.excludeComponents!!.size)
        assertEquals("Save Image", feature.saveAsImage!!.lang!![0])
        assertEquals("active", feature.restore!!.iconStatus!!["restore"])
        assertNotNull(feature.restore!!.onclick)
        assertEquals("Restore", feature.restore!!.title!!.unsafeCast<String>())
        assertTrue(feature.dataView!!.readOnly!!)
        assertEquals(2, feature.dataView!!.lang!!.size)
        assertEquals("#ccc", feature.dataView!!.textareaColor)
        assertEquals("#999", feature.dataView!!.textareaBorderColor)
        assertEquals("#333", feature.dataView!!.textColor)
        assertEquals("#ddd", feature.dataView!!.buttonColor)
        assertEquals("#000", feature.dataView!!.buttonTextColor)
        assertEquals(100.0, feature.dataView!!.refreshRate)
        assertNotNull(feature.dataView!!.optionToContent)
        assertNotNull(feature.dataView!!.contentToOption)
        assertEquals(2, feature.magicType!!.type!!.size)
    }

    @Test
    fun toolboxEmphasisWrapsItsIconStyle() {
        val emphasis = unsafeJso<ToolboxEmphasisOption> {
            iconStyle = unsafeJso<ToolboxIconStyleOption> { textFill = ZRColor("#fff") }
        }

        assertEquals("#fff", emphasis.iconStyle!!.textFill!!.unsafeCast<String>())
    }
}

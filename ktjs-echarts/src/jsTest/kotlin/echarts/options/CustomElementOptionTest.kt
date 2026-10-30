package echarts.options

import js.objects.recordOf
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Covers the custom-element transition and base/displayable members of `CustomElementOptions.kt`.
 * Each field is set through [unsafeJso] and read back so the typed property keeps mapping to the
 * exact JavaScript key; the nested transition targets ([CustomElementProps], the zrender shape and
 * style props) and animation sub-options are built with [unsafeJso] as well.
 */
class CustomElementOptionTest {

    @Test
    fun transitionOptionMixinLeaveToAndAnimationsRoundTrip() {
        val mixin = unsafeJso<TransitionOptionMixin> {
            leaveTo = unsafeJso<CustomElementProps> { }
            updateAnimation = unsafeJso<ElementAnimationOption> { }
            leaveAnimation = unsafeJso<ElementAnimationOption> { }
        }
        assertNotNull(mixin.leaveTo)
        assertNotNull(mixin.updateAnimation)
        assertNotNull(mixin.leaveAnimation)

        val shape = unsafeJso<CustomShapeTransitionOption> {
            leaveTo = unsafeJso<zrender.ZRShapeProps> { rx = 2.0 }
            updateAnimation = unsafeJso<ElementAnimationOption> { }
            leaveAnimation = unsafeJso<ElementAnimationOption> { }
        }
        assertNotNull(shape.leaveTo)
        assertNotNull(shape.updateAnimation)
        assertNotNull(shape.leaveAnimation)

        val style = unsafeJso<CustomStyleTransitionOption> {
            leaveTo = unsafeJso<zrender.ZRStyleProps> { fillOpacity = 0.5 }
            updateAnimation = unsafeJso<ElementAnimationOption> { }
            leaveAnimation = unsafeJso<ElementAnimationOption> { }
        }
        assertNotNull(style.leaveTo)
        assertNotNull(style.updateAnimation)
        assertNotNull(style.leaveAnimation)
    }

    @Test
    fun customBaseElementFieldsRoundTrip() {
        val base = unsafeJso<CustomBaseElementOption> {
            info = recordOf("role" to "axis-label".unsafeCast<JsAny>())
            clipPath = unsafeJso<CustomClipPathOption> { }
            tooltipDisabled = true
            updateAnimation = unsafeJso<ElementAnimationOption> { }
            leaveAnimation = unsafeJso<ElementAnimationOption> { }
        }
        assertNotNull(base.info)
        assertNotNull(base.clipPath)
        assertEquals(true, base.tooltipDisabled)
        assertNotNull(base.updateAnimation)
        assertNotNull(base.leaveAnimation)
    }

    @Test
    fun displayableAndPathElementFieldsRoundTrip() {
        val displayable = unsafeJso<CustomDisplayableOption> {
            styleEmphasis = unsafeJso<CustomStyleOption> { }
        }
        assertNotNull(displayable.styleEmphasis)

        val path = unsafeJso<CustomPathOption> {
            autoBatch = true
            morph = false
        }
        assertEquals(true, path.autoBatch)
        assertEquals(false, path.morph)
    }
}

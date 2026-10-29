package echartsgl.options

import echarts.AnimationEasing
import echarts.options.DoubleOrDoubleArray
import echartsgl.AutoRotateDirection
import echartsgl.MouseButton
import echartsgl.ProjectionType

/**
 * Camera control of a 3D component: rotation, zooming, panning and the automatic rotation.
 *
 * The defaults differ per component; `grid3D` starts at `alpha = 20`, `beta = 40`,
 * `distance = 200`, `minDistance = 40` and `maxDistance = 400`.
 */
external interface ViewControlOption {
    /** Projection of the camera: `"perspective"` or `"orthographic"`. */
    var projection: ProjectionType?

    /** Whether the camera rotates around the scene on its own. */
    var autoRotate: Boolean?

    /** Direction of the automatic rotation: `"cw"` or `"ccw"`. */
    var autoRotateDirection: AutoRotateDirection?

    /** Speed of the automatic rotation, in degrees per second. */
    var autoRotateSpeed: Double?

    /** Seconds the camera waits after the last mouse interaction before auto-rotating again. */
    var autoRotateAfterStill: Double?

    /** Inertia of a mouse gesture, `0..1`; `0` stops the camera as soon as the mouse stops. */
    var damping: Double?

    /** Sensitivity of the rotation gesture, either one value or `[horizontal, vertical]`. */
    var rotateSensitivity: DoubleOrDoubleArray?

    /** Sensitivity of the zoom gesture. */
    var zoomSensitivity: Double?

    /** Sensitivity of the pan gesture, either one value or `[horizontal, vertical]`. */
    var panSensitivity: DoubleOrDoubleArray?

    /** Mouse button that pans the camera. */
    var panMouseButton: MouseButton?

    /** Mouse button that rotates the camera. */
    var rotateMouseButton: MouseButton?

    /** Distance from the camera to the scene; perspective projection only. */
    var distance: Double?

    /** Closest distance the zoom gesture can reach; perspective projection only. */
    var minDistance: Double?

    /** Farthest distance the zoom gesture can reach; perspective projection only. */
    var maxDistance: Double?

    /** Size of the viewing volume; orthographic projection only. */
    var orthographicSize: Double?

    /** Largest viewing volume the zoom gesture can reach; orthographic projection only. */
    var maxOrthographicSize: Double?

    /** Smallest viewing volume the zoom gesture can reach; orthographic projection only. */
    var minOrthographicSize: Double?

    /** Point the camera looks at and rotates around, `[x, y, z]`. */
    var center: Array<Double>?

    /** Vertical rotation angle in degrees; positive looks down from above. */
    var alpha: Double?

    /** Horizontal rotation angle in degrees; positive looks from the right. */
    var beta: Double?

    /** Smallest [alpha] the rotation gesture can reach. */
    var minAlpha: Double?

    /** Largest [alpha] the rotation gesture can reach. */
    var maxAlpha: Double?

    /** Smallest [beta] the rotation gesture can reach. */
    var minBeta: Double?

    /** Largest [beta] the rotation gesture can reach. */
    var maxBeta: Double?

    /** Whether a view change made with `setOption`/actions animates instead of jumping. */
    var animation: Boolean?

    /** Duration of the view animation in milliseconds. */
    var animationDurationUpdate: Double?

    /** Easing of the view animation; the default is `"cubicInOut"`. */
    var animationEasingUpdate: AnimationEasing?
}

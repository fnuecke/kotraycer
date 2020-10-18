package li.cil.kotraycer.render

import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.math.Vector2
import li.cil.kotraycer.math.Vector3
import li.cil.kotraycer.math.deg2rad
import kotlin.math.tan

class SimpleCamera(
    private val eyePosition: Vector3,
    lookAt: Vector3,
    viewport: Vector2,
    horizontalFoV: Float = 90f,
    nearClipPlane: Float = 0.1f
) : Camera {
    private val planeRight: Vector3
    private val planeUp: Vector3
    private val toPlaneOrigin: Vector3

    init {
        val verticalFoV = horizontalFoV * viewport.y / viewport.x
        val planeWidth = 2f * tan(0.5f * deg2rad(horizontalFoV)) * nearClipPlane
        val planeHeight = 2f * tan(0.5f * deg2rad(verticalFoV)) * nearClipPlane

        val forward = (lookAt - eyePosition).normalized()
        val toPlaneCenter = forward * nearClipPlane

        val right = forward.cross(Vector3.POSY)
        val up = right.cross(forward)

        planeRight = right * planeWidth
        planeUp = up * planeHeight
        toPlaneOrigin = toPlaneCenter - (planeRight + planeUp) * 0.5f
    }

    override fun getRay(uv: Vector2): Ray =
        Ray(eyePosition, (toPlaneOrigin + planeRight * uv.x + planeUp * uv.y).normalized())
}
package li.cil.kotraycer.math

import kotlin.math.sqrt

data class Vector2(val x: Float, val y: Float) {
    fun distance(v: Vector2) = sqrt((x - v.x) * (x - v.x) + (y - v.y) * (y - v.y))
}

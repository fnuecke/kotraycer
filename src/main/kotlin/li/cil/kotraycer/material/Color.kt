package li.cil.kotraycer.material

import li.cil.kotraycer.math.Vector3
import kotlin.math.min

typealias Color = Vector3

object Colors {
    val BLACK = Vector3.ZERO
    val WHITE = Vector3.ONE

    fun toRGBInt(color: Color): Int {
        // Meh. Why can't those be regular operators like in the rest of the civilized world?
        return (min(255, (color.x * 255).toInt()) shl 16) or
                (min(255, (color.y * 255).toInt()) shl 8) or
                min(255, (color.z * 255).toInt())
    }
}

package li.cil.kotraycer.material

import li.cil.kotraycer.math.Vector3

typealias Color = Vector3

object Colors {
    val BLACK = Vector3.ZERO
    val WHITE = Vector3.ONE

    fun toRGBInt(color: Color): Int {
        // Meh. Why can't those be regular operators like in the rest of the civilized world?
        return ((color.x * 255).toInt() shl 16) or
                ((color.y * 255).toInt() shl 8) or
                (color.z * 255).toInt()
    }
}

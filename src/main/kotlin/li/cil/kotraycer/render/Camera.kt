package li.cil.kotraycer.render

import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.math.Vector2

interface Camera {
    fun getRay(uv: Vector2): Ray
}
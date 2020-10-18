package li.cil.kotraycer.material

import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.render.Hit
import li.cil.kotraycer.render.RenderContext

interface Material {
    fun shade(ray: Ray, hit: Hit, context: RenderContext): Color
}

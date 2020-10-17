package li.cil.kotraycer.geometry

import li.cil.kotraycer.render.Hit
import li.cil.kotraycer.math.Ray

interface Shape {
    fun intersect(ray: Ray): Hit?
}

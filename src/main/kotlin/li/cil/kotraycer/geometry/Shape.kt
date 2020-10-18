package li.cil.kotraycer.geometry

import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.render.Hit

interface Shape {
    fun intersect(ray: Ray): Hit?
    fun bounds(): AABB
}

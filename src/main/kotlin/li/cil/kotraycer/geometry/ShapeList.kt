package li.cil.kotraycer.geometry

import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.render.Hit

class ShapeList(private val shapes: List<Shape>) : Shape {
    override fun intersect(ray: Ray): Hit? {
        var bestHit: Hit? = null
        for (shape in shapes) {
            val hit = shape.intersect(ray)
            if (hit != null && (bestHit == null || hit.distance < bestHit.distance)) {
                bestHit = hit
            }
        }
        return bestHit
    }

    override fun bounds() = shapes.map { it.bounds() }.fold(AABB.EMPTY, { acc, aabb -> acc.include(aabb) })
}

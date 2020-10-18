package li.cil.kotraycer.geometry

import li.cil.kotraycer.material.Material
import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.math.Vector3
import li.cil.kotraycer.render.Hit
import li.cil.kotraycer.render.INTERSECTION_BIAS
import kotlin.math.sqrt

class Sphere(private val center: Vector3, private val radius: Float, private val material: Material) : Shape {
    override fun intersect(ray: Ray): Hit? {
        val origin = center - ray.origin
        val b = origin.dot(ray.direction)
        val det = radius * radius + b * b - origin.dot(origin)
        if (det < 0) return null
        val sqrtDet = sqrt(det)
        var t0 = b + sqrtDet
        var t1 = b - sqrtDet

        if (t1 < t0) {
            val tmp = t0
            t0 = t1
            t1 = tmp
        }

        if (t0 > INTERSECTION_BIAS) {
            val position = ray.at(t0)
            val normal = (position - center).normalized()
            return Hit(t0, position, normal, material)
        }

        if (t1 > INTERSECTION_BIAS) {
            val position = ray.at(t1)
            val normal = (position - center).normalized()
            return Hit(t1, position, normal, material)
        }

        return null
    }

    override fun bounds() = AABB(center - Vector3.ONE * radius, center + Vector3.ONE * radius)
}

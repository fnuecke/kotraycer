package li.cil.kotraycer.geometry

import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.math.Vector3
import li.cil.kotraycer.math.max
import li.cil.kotraycer.math.min

data class AABB(val min: Vector3, val max: Vector3) {
    companion object {
        val EMPTY = AABB(Vector3.MAX, Vector3.MIN)
    }

    operator fun get(component: Int) = when (component) {
        0 -> min
        1 -> max
        else -> throw IllegalArgumentException()
    }

    fun centroid() = (min + max) * 0.5f
    fun include(b: AABB) = AABB(min(min, b.min), max(max, b.max))
    fun include(v: Vector3) = AABB(min(min, v), max(max, v))

    fun intersect(ray: Ray): Boolean {
        var tMin = (this[ray.sign[0]].x - ray.origin.x) * ray.invDirection.x
        var tMax = (this[1 - ray.sign[0]].x - ray.origin.x) * ray.invDirection.x
        val tyMin = (this[ray.sign[1]].y - ray.origin.y) * ray.invDirection.y
        val tyMax = (this[1 - ray.sign[1]].y - ray.origin.y) * ray.invDirection.y

        if (tMin > tyMax || tyMin > tMax) return false
        if (tyMin > tMin) tMin = tyMin
        if (tyMax < tMax) tMax = tyMax

        val tzMin = (this[ray.sign[2]].z - ray.origin.z) * ray.invDirection.z
        val tzMax = (this[1 - ray.sign[2]].z - ray.origin.z) * ray.invDirection.z

        if (tMin > tzMax || tzMin > tMax) return false
        if (tzMin > tMin) tMin = tzMin
        if (tzMax < tMax) tMax = tzMax
        return (tMin < Float.MAX_VALUE) && (tMax > 0f)
    }

    fun size() = max - min
}

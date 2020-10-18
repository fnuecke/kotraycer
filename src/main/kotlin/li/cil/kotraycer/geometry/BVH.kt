package li.cil.kotraycer.geometry

import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.render.Hit

class BVH(shapes: List<Shape>) : Shape {
    private val left: Shape?
    private val right: Shape?
    private val bounds: AABB

    init {
        when (shapes.size) {
            1 -> {
                left = shapes[0]
                right = null
                bounds = shapes[0].bounds()
            }
            2 -> {
                left = shapes[0]
                right = shapes[1]
                bounds = shapes[0].bounds().include(shapes[1].bounds())
            }
            else -> {
                val projectedCentroidBounds = shapes.fold(AABB.EMPTY, { acc, shape ->
                    acc.include(shape.bounds().centroid())
                })
                val size = projectedCentroidBounds.size()
                val axis = size.maxComponent()
                val sortedShapes = sorted(shapes, axis)
                left = BVH(sortedShapes.subList(0, sortedShapes.size / 2))
                right = BVH(sortedShapes.subList(sortedShapes.size / 2, sortedShapes.size))
                bounds = left.bounds().include(right.bounds())
            }
        }
    }

    override fun intersect(ray: Ray): Hit? {
        if (!bounds.intersect(ray)) {
            return null
        }

        val hitLeft: Hit? = left?.intersect(ray)
        val hitRight: Hit? = right?.intersect(ray)

        return when {
            hitLeft != null && hitRight != null -> if (hitLeft.distance < hitRight.distance) hitLeft else hitRight
            hitLeft != null -> hitLeft
            else -> hitRight
        }
    }

    override fun bounds() = bounds

    private fun sorted(shapes: List<Shape>, axis: Int) = shapes.sortedWith { s1, s2 ->
        s1.bounds().min[axis].compareTo(s2.bounds().min[axis])
    }
}

package li.cil.kotraycer.render

import li.cil.kotraycer.material.Color
import li.cil.kotraycer.material.Colors
import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.math.Vector3

fun trace(ray: Ray, context: RenderContext): Color {
    if (context.remainingRecursions <= 0) {
        return Colors.BLACK
    }

    val hit = context.scene.intersect(ray)
    return if (hit != null) {
        val recursiveContext = context.copy(remainingRecursions = context.remainingRecursions - 1)
        hit.material.shade(ray, hit, recursiveContext)
    } else {
        sampleSkybox(ray)
    }
}

fun sampleSkybox(ray: Ray): Color {
    val gradient = (ray.direction.dot(Vector3.POSY) + 1) * 0.5f
    return Color(0.8f, 0.8f, 0.9f).lerp(Color(0.6f, 0.6f, 0.8f), gradient)
}

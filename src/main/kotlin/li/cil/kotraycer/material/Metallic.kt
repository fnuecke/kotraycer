package li.cil.kotraycer.material

import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.math.Vector3
import li.cil.kotraycer.math.plus
import li.cil.kotraycer.math.times
import li.cil.kotraycer.render.Hit
import li.cil.kotraycer.render.RenderContext
import li.cil.kotraycer.render.trace

class Metallic(private val albedo: Color, private val roughness: Float) : Material {
    override fun shade(ray: Ray, hit: Hit, context: RenderContext): Color {
        val reflected = hit.normal.reflect(ray.direction)
        val scattering = Vector3.inUnitSphere(context.rng) * roughness
        val direction = reflected + scattering
        return if (direction.dot(hit.normal) > 0) { // avoid rays going into the hit object
            val rayOut = Ray(hit.position, direction.normalized())
            val color = trace(rayOut, context)
            albedo * color
        } else {
            Colors.BLACK
        }
    }
}

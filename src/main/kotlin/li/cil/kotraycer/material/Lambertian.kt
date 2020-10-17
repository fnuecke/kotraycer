package li.cil.kotraycer.material

import li.cil.kotraycer.math.*
import li.cil.kotraycer.render.Hit
import li.cil.kotraycer.render.RenderContext
import li.cil.kotraycer.render.trace

class Lambertian(private val albedo: Color) : Material {
    override fun shade(ray: Ray, hit: Hit, context: RenderContext): Color {
        val scatterTarget = hit.position + hit.normal + Vector3.inUnitSphere(context.rng)
        val scatteredRay = Ray(hit.position, (scatterTarget - hit.position).normalized())
        return albedo * trace(scatteredRay, context)
    }
}
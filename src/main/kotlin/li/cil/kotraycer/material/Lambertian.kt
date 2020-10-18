package li.cil.kotraycer.material

import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.math.nextVector3InUnitSphere
import li.cil.kotraycer.render.Hit
import li.cil.kotraycer.render.RenderContext
import li.cil.kotraycer.render.trace

class Lambertian(private val albedo: Color) : Material {
    override fun shade(ray: Ray, hit: Hit, context: RenderContext): Color {
        val scatterTarget = hit.position + hit.normal + context.rng.nextVector3InUnitSphere()
        val scatteredRay = Ray(hit.position, (scatterTarget - hit.position).normalized())
        return albedo * trace(scatteredRay, context)
    }
}
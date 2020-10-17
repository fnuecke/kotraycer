package li.cil.kotraycer.material

import li.cil.kotraycer.math.Ray
import li.cil.kotraycer.math.Vector3
import li.cil.kotraycer.math.unaryMinus
import li.cil.kotraycer.render.Hit
import li.cil.kotraycer.render.RenderContext
import li.cil.kotraycer.render.trace

class Dielectric(private val n: Float) : Material {
    override fun shade(ray: Ray, hit: Hit, context: RenderContext): Color {
        val n1 = context.refractionStack[context.refractionDepth]
        val n2: Float
        val normal: Vector3

        val orientation = ray.direction.dot(hit.normal)
        if (orientation < 0) { // potentially entering another medium
            assert(context.refractionDepth < context.refractionStack.size - 1)
            n2 = n
            normal = hit.normal
        } else { // potentially leaving a medium
            if (context.refractionDepth == 0) return Colors.BLACK // floating point inaccuracies
            n2 = context.refractionStack[context.refractionDepth - 1]
            normal = -hit.normal
        }

        assert(n1 >= 0.0f)
        assert(n2 >= 0.0f)

        val refracted = normal.refract(ray.direction, n1, n2)
        if (refracted != null && context.rng.nextFloat() > schlick(ray.direction.dot(-normal), n1, n2)) {
            // transitioning to other medium, update refraction index stack
            val newRefractionDepth: Int
            if (orientation < 0) { // entering another medium, push new index
                newRefractionDepth = context.refractionDepth + 1
                context.refractionStack[newRefractionDepth] = n
            } else { // leaving current medium, pop current index
                newRefractionDepth = context.refractionDepth - 1
            }

            return trace(Ray(hit.position, refracted.normalized()), context.copy(refractionDepth = newRefractionDepth))
        } else {
            // staying in same medium, don't change refraction index stack
            val direction = normal.reflect(ray.direction)
            return trace(Ray(hit.position, direction), context)
        }
    }

    private fun schlick(cosine: Float, n1: Float, n2: Float): Float {
        val a = (n1 - n2) / (n1 + n2)
        val b = 1f - cosine
        val bb = b * b
        val r0 = a * a
        return r0 + (1f - r0) * (bb * bb * b)
    }
}

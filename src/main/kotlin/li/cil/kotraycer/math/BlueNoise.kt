package li.cil.kotraycer.math

import kotlin.math.min
import kotlin.random.Random

object BlueNoise {
    fun generate(rng: Random, count: Int): Array<Vector2> {
        val samples = Array(count) { Vector2(rng.nextFloat(), rng.nextFloat()) }
        var n = 1
        while (n < count) {
            var maxDistance = minWrappedDistance(samples[n], samples, n - 1)
            for (i in 0..9) {
                val candidate = Vector2(rng.nextFloat(), rng.nextFloat())
                val distance = minWrappedDistance(candidate, samples, n)
                if (distance > maxDistance) {
                    maxDistance = distance
                    samples[n] = candidate
                }
            }
            n++
        }
        return samples
    }

    private fun minWrappedDistance(v: Vector2, vs: Array<Vector2>, n: Int): Float {
        var distance = wrappedDistance(v, vs[0])
        for (i in 1 until n) {
            distance = min(distance, wrappedDistance(v, vs[i]))
        }
        return distance
    }

    private fun wrappedDistance(v1: Vector2, v2: Vector2): Float {
        var distance = v1.distance(v2)
        distance = min(distance, v1.distance(Vector2(v2.x - 1, v2.y)))
        distance = min(distance, v1.distance(Vector2(v2.x - 1, v2.y + 1)))
        distance = min(distance, v1.distance(Vector2(v2.x, v2.y + 1)))
        distance = min(distance, v1.distance(Vector2(v2.x + 1, v2.y + 1)))
        distance = min(distance, v1.distance(Vector2(v2.x + 1, v2.y)))
        distance = min(distance, v1.distance(Vector2(v2.x + 1, v2.y - 1)))
        distance = min(distance, v1.distance(Vector2(v2.x, v2.y - 1)))
        distance = min(distance, v1.distance(Vector2(v2.x - 1, v2.y - 1)))
        return distance
    }
}

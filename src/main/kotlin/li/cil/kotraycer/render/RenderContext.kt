package li.cil.kotraycer.render

import li.cil.kotraycer.geometry.Shape
import kotlin.random.Random

data class RenderContext(
    val scene: Shape,
    val rng: Random,
    val remainingRecursions: Int,
    val refractionDepth: Int = 0,
    val refractionStack: FloatArray = FloatArray(MAX_RECURSION).also { indices -> indices[0] = 1f }
)

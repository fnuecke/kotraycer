package li.cil.kotraycer.math

data class Ray(val origin: Vector3, val direction: Vector3) {
    val invDirection = 1f / direction
    val sign = direction.map { if (it < 0) 1 else 0 }
    fun at(t: Float) = origin + direction * t
}

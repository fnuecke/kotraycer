package li.cil.kotraycer.math

data class Ray(val origin: Vector3, val direction: Vector3) {
    fun at(t: Float) = origin + direction * t
}

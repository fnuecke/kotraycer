package li.cil.kotraycer.math

import kotlin.math.sqrt
import kotlin.random.Random

data class Vector3(val x: Float, val y: Float, val z: Float) {
    companion object {
        val ZERO = Vector3(0f, 0f, 0f)
        val ONE = Vector3(1f, 1f, 1f)
        val POSX = Vector3(1f, 0f, 0f)
        val POSY = Vector3(0f, 1f, 0f)
        val POSZ = Vector3(0f, 0f, 1f)
        val NEGX = Vector3(-1f, 0f, 0f)
        val NEGY = Vector3(0f, -1f, 0f)
        val NEGZ = Vector3(0f, 0f, -1f)

        fun inUnitSphere(rng: Random): Vector3 {
            var p: Vector3
            do {
                val x = rng.nextFloat() * 2 - 1
                val y = rng.nextFloat() * 2 - 1
                val z = rng.nextFloat() * 2 - 1
                p = Vector3(x, y, z)
            } while (p.squaredMagnitude() >= 1.0f)
            return p
        }
    }

    fun squaredMagnitude() = this.dot(this)

    fun magnitude() = sqrt(squaredMagnitude())

    fun normalized() = this / magnitude()

    fun dot(v: Vector3) = x * v.x + y * v.y + z * v.z

    fun cross(v: Vector3) = Vector3(
        y * v.z - z * v.y,
        z * v.x - x * v.z,
        x * v.y - y * v.x
    )

    fun reflect(v: Vector3) = v - this * 2f * v.dot(this)

    fun refract(v: Vector3, n1: Float, n2: Float): Vector3? {
        val n1n2 = n1 / n2
        val dn = v.dot(this)
        val discriminant = 1f - n1n2 * n1n2 * (1f - dn * dn)

        return if (discriminant > 0) {
            v * n1n2 - this * sqrt(discriminant)
        } else {
            null
        }
    }

    fun lerp(v: Vector3, t: Float) = Vector3(
        x + (v.x - x) * t,
        x + (v.y - y) * t,
        x + (v.z - z) * t
    )
}

operator fun Vector3.unaryMinus() = Vector3(-x, -y, -z)
operator fun Vector3.plus(v: Vector3) = Vector3(x + v.x, y + v.y, z + v.z)
operator fun Vector3.minus(v: Vector3) = this + -v
operator fun Vector3.times(scalar: Float) = Vector3(x * scalar, y * scalar, z * scalar)
operator fun Float.times(v: Vector3) = Vector3(v.x * this, v.y * this, v.z * this)
operator fun Vector3.times(v: Vector3) = Vector3(x * v.x, y * v.y, z * v.z)
operator fun Vector3.div(scalar: Float) = Vector3(x / scalar, y / scalar, z / scalar)
operator fun Vector3.div(v: Vector3) = Vector3(x / v.x, y / v.y, z / v.z)

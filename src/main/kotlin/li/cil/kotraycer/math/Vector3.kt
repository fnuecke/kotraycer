package li.cil.kotraycer.math

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

data class Vector3(val x: Float, val y: Float, val z: Float) : Iterable<Float> {
    companion object {
        val ZERO = Vector3(0f, 0f, 0f)
        val ONE = Vector3(1f, 1f, 1f)
        val POSX = Vector3(1f, 0f, 0f)
        val POSY = Vector3(0f, 1f, 0f)
        val POSZ = Vector3(0f, 0f, 1f)
        val NEGX = Vector3(-1f, 0f, 0f)
        val NEGY = Vector3(0f, -1f, 0f)
        val NEGZ = Vector3(0f, 0f, -1f)
        val MIN = Vector3(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE)
        val MAX = Vector3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE)
    }

    override fun iterator(): Iterator<Float> = iterator { yield(x); yield(y); yield(z) }

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

    fun maxComponent() = when {
        abs(x) > abs(y) && abs(x) > abs(z) -> 0
        abs(y) > abs(z) -> 1
        else -> 2
    }

    operator fun get(component: Int) = when (component) {
        0 -> x
        1 -> y
        2 -> z
        else -> throw IllegalArgumentException()
    }

    operator fun unaryMinus() = Vector3(-x, -y, -z)
    operator fun plus(v: Vector3) = Vector3(x + v.x, y + v.y, z + v.z)
    operator fun plus(scalar: Float) = Vector3(x + scalar, y + scalar, z + scalar)
    operator fun minus(v: Vector3) = this + -v
    operator fun minus(scalar: Float) = this + -scalar
    operator fun times(scalar: Float) = Vector3(x * scalar, y * scalar, z * scalar)
    operator fun times(v: Vector3) = Vector3(x * v.x, y * v.y, z * v.z)
    operator fun div(scalar: Float) = Vector3(x / scalar, y / scalar, z / scalar)
    operator fun div(v: Vector3) = Vector3(x / v.x, y / v.y, z / v.z)
}

operator fun Float.times(v: Vector3) = Vector3(v.x * this, v.y * this, v.z * this)
operator fun Float.div(v: Vector3) = Vector3(this / v.x, this / v.y, this / v.z)

fun Random.nextVector3() = Vector3(nextFloat(), nextFloat(), nextFloat())

fun Random.nextVector3InUnitSphere(): Vector3 {
    var p: Vector3
    do {
        p = nextVector3() * 2f - 1f
    } while (p.squaredMagnitude() >= 1.0f)
    return p
}

fun min(v1: Vector3, v2: Vector3) = Vector3(min(v1.x, v2.x), min(v1.y, v2.y), min(v1.z, v2.z))
fun max(v1: Vector3, v2: Vector3) = Vector3(max(v1.x, v2.x), max(v1.y, v2.y), max(v1.z, v2.z))

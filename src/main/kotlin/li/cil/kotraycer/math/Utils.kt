package li.cil.kotraycer.math

import kotlin.math.PI

fun deg2rad(degrees: Float) = degrees * (PI.toFloat() / 180)
fun rad2deg(radians: Float) = radians * (180 / PI.toFloat())

package li.cil.kotraycer.render

import li.cil.kotraycer.material.Material
import li.cil.kotraycer.math.Vector3

data class Hit(val distance: Float, val position: Vector3, val normal: Vector3, val material: Material)

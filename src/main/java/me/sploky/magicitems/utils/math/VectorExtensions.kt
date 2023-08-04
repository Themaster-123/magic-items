package me.sploky.magicitems.utils.math

import org.bukkit.util.Vector
import org.joml.Vector3d
import org.joml.Vector3f

fun Vector3f.toBukkitVector(): Vector {
    return Vector(this.x, this.y, this.z)
}

fun Vector3d.toBukkitVector(): Vector {
    return Vector(this.x, this.y, this.z)
}

fun Vector.vector3f(): Vector3f {
    return Vector3f(this.x.toFloat(), this.y.toFloat(), this.z.toFloat())
}

fun Vector.vector3d(): Vector3d {
    return Vector3d(this.x, this.y, this.z)
}
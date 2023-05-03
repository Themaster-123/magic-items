package me.sploky.magicitems.utils.math

import org.bukkit.util.Vector
import org.joml.Vector3f

fun Vector3f.toBukkitVector(): Vector {
    return Vector(this.x, this.y, this.z)
}
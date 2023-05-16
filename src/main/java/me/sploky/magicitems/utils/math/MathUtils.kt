package me.sploky.magicitems.utils.math

import org.bukkit.util.Vector
import org.joml.Quaternionf
import kotlin.math.cos
import kotlin.math.sin

class MathUtils {
    companion object {
        @JvmStatic
        fun eulerToQuaternion(pitch: Float, yaw: Float, roll: Float): Quaternionf {
            val cr: Float = cos(pitch * 0.5f)
            val sr: Float = sin(pitch * 0.5f)
            val cp: Float = cos(yaw * 0.5f)
            val sp: Float = sin(yaw * 0.5f)
            val cy: Float = cos(roll * 0.5f)
            val sy: Float = sin(roll * 0.5f)


            return Quaternionf(
                sr * cp * cy - cr * sp * sy,
                cr * sp * cy + sr * cp * sy,
                cr * cp * sy - sr * sp * cy,
                cr * cp * cy + sr * sp * sy
            ).normalize()
        }

        @JvmStatic
        fun reflect(vector: Vector, normal: Vector): Vector {
            val normalizedNormal = normal.clone().normalize()

            return vector.clone().subtract(normalizedNormal.multiply(2 * vector.dot(normal)))
        }
    }
}
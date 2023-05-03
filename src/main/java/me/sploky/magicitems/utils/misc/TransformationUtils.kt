package me.sploky.magicitems.utils.misc

import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f

class TransformationUtils {
    companion object {
        @JvmStatic
        fun createTransformation(basePosition: Vector, position: Vector, leftRotation: Quaternionf,
                                 scale: Vector = Vector(1, 1, 1), rightRotation: Quaternionf = Quaternionf(), centered: Boolean = true
        ): Transformation {
            val adjustedPosition = position.clone().subtract(basePosition);
            var center = Vector3f()
            if (centered)
                center = leftRotation.transform(Vector3f(.5f))

            adjustedPosition.subtract(Vector(center.x, center.y, center.z))
            return Transformation(
                Vector3f(adjustedPosition.x.toFloat(), adjustedPosition.y.toFloat(), adjustedPosition.z.toFloat()),
                leftRotation,
                Vector3f(scale.x.toFloat(), scale.y.toFloat(), scale.z.toFloat()),
                rightRotation
            )
        }
    }
}
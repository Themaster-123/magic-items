package me.sploky.magicitems.utils.misc

import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f

class TransformationUtils {
    companion object {
        @JvmStatic
        fun createTransformation(localPosition: Vector, leftRotation: Quaternionf,
                                 scale: Vector = Vector(1, 1, 1), rightRotation: Quaternionf = Quaternionf(), centered: Boolean = true
        ): Transformation {
            val position = localPosition.clone()
            var center = Vector3f()
            val vector3fScale = Vector3f(scale.x.toFloat(), scale.y.toFloat(), scale.z.toFloat())

            if (centered)
                center = leftRotation.transform(Vector3f(.5f).mul(vector3fScale))

            position.subtract(Vector(center.x, center.y, center.z))
            return Transformation(
                Vector3f(position.x.toFloat(), position.y.toFloat(), position.z.toFloat()),
                leftRotation,
                vector3fScale,
                rightRotation
            )
        }

        @JvmStatic
        fun createTransformation(basePosition: Vector, position: Vector, leftRotation: Quaternionf,
                                 scale: Vector = Vector(1, 1, 1), rightRotation: Quaternionf = Quaternionf(), centered: Boolean = true
        ): Transformation {
            val adjustedPosition = position.clone().subtract(basePosition)

            return createTransformation(adjustedPosition, leftRotation, scale, rightRotation, centered)
        }
    }
}
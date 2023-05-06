package me.sploky.magicitems.registries

import me.sploky.magicitems.magicitemsbase.items.Cleanable

class CleanableRegistry {
    companion object {
        @JvmStatic
        private val cleanableElements = mutableListOf<Cleanable>()

        @JvmStatic
        fun registerElement(cleanable: Cleanable) {
            cleanableElements.add(cleanable)
        }

        @JvmStatic
        fun cleanElements() {
            cleanableElements.forEach { it.cleanUp() }
        }
    }
}
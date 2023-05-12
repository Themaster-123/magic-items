package me.sploky.magicitems.utils.animation

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData

class ParticleUtils {
    companion object {
        @JvmStatic
        fun createBlockBreakParticles(location: Location, blockData: BlockData) {
            val world: World = location.world

            world.spawnParticle<BlockData>(Particle.BLOCK_CRACK, location, 20, .5, .5, .5, blockData)
            world.spawnParticle<BlockData>(Particle.BLOCK_DUST, location, 20, .5, .5, .5, blockData)
        }

        @JvmStatic
        fun createBlockBreakParticles(location: Location) {
            createBlockBreakParticles(location, location.block.blockData)
        }

        @JvmStatic
        fun createBlockBreakParticles(block: Block) {
            createBlockBreakParticles(block.location.add(.5, .5, .5), block.blockData)
        }
    }
}
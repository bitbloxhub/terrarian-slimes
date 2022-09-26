package io.github.lucaargolo.terrarianslimes.common.entity.slimes

import io.github.lucaargolo.terrarianslimes.common.block.BlockCompendium
import io.github.lucaargolo.terrarianslimes.utils.ModConfig
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.SlimeEntity
import net.minecraft.item.ItemConvertible
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.world.World

class RainbowSlimeEntity<C: ModConfig.ModdedSlimeConfig>(
    entityType: EntityType<out SlimeEntity>,
    world: World,
    particleItem: ItemConvertible,
    config: C,
    defaultSize: Int
): ModdedSlimeEntity<C>(entityType, world, particleItem, config, defaultSize) {

    override fun getParticles() = BlockStateParticleEffect(ParticleTypes.BLOCK, BlockCompendium.RAINBOW_SLIME_BLOCK.defaultState)

}
package io.github.lucaargolo.terrarianslimes.common.entity.throwable

import io.github.lucaargolo.terrarianslimes.common.block.BlockCompendium
import io.github.lucaargolo.terrarianslimes.common.block.GlowstickLightBlock
import io.github.lucaargolo.terrarianslimes.common.entity.EntityCompendium
import io.github.lucaargolo.terrarianslimes.common.item.ItemCompendium
import net.minecraft.block.Material
import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.state.property.Properties
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World

class ThrowableGlowstickEntity: ThrowableEntity {

    constructor(entityType: EntityType<ThrowableEntity>, world: World): super(entityType, world)

    constructor(world: World, x: Double, y: Double, z: Double): super(EntityCompendium.GLOWSTICK, x, y, z, world)

    override fun getNormalVariant() = ItemCompendium.GLOWSTICK

    override fun getStickyVariant() = ItemCompendium.STICKY_GLOWSTICK

    override fun getBouncyVariant() = ItemCompendium.BOUNCY_GLOWSTICK

    private var delay = 20

    override fun tick() {
        super.tick()
        if(!isMoving && isColliding) {
            if (delay-- <= 0 && world?.isClient == false) {
                delay = 20
                val state = world.getBlockState(blockPos)
                if (state.material.isReplaceable && !state.material.isLiquid) {
                    world.setBlockState(blockPos, BlockCompendium.GLOWSTICK_LIGHT.defaultState.with(GlowstickLightBlock.GLOWSTICK, throwableType))
                }else if(state.material == Material.WATER) {
                    world.setBlockState(blockPos, BlockCompendium.GLOWSTICK_LIGHT.defaultState.with(GlowstickLightBlock.GLOWSTICK, throwableType).with(Properties.WATERLOGGED, true))
                }
            }
        }
        if(age >= 12000) {
            this.remove(RemovalReason.DISCARDED)
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        delay = 0
    }

    override fun readCustomDataFromNbt(tag: NbtCompound) {
        super.readCustomDataFromNbt(tag)
        age = tag.getInt("age")
    }

    override fun writeCustomDataToNbt(tag: NbtCompound) {
        super.writeCustomDataToNbt(tag)
        tag.putInt("age", age)
    }

}
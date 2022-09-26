package io.github.lucaargolo.terrarianslimes.common.entity.spike

import io.github.lucaargolo.terrarianslimes.common.entity.EntityCompendium
import io.github.lucaargolo.terrarianslimes.common.entity.slimes.ModdedSlimeEntity
import io.github.lucaargolo.terrarianslimes.network.PacketCompendium
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class SpikeEntity: PersistentProjectileEntity {

    var statusEffect: StatusEffect? = null

    constructor(entityType: EntityType<SpikeEntity>, world: World): super(entityType, world)

    constructor(world: World, x: Double, y: Double, z: Double): super(EntityCompendium.SPIKE, x, y, z, world)

    constructor(world: World, owner: LivingEntity, damage: Double, statusEffect: StatusEffect? = null): super(EntityCompendium.SPIKE, owner, world) {
        this.damage = damage
        this.statusEffect = statusEffect
    }

    override fun onHit(target: LivingEntity) {
        super.onHit(target)
        statusEffect?.let { target.addStatusEffect(StatusEffectInstance(it, 200, 0)) }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        super.onBlockHit(blockHitResult)
        this.remove(RemovalReason.DISCARDED)
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if(entityHitResult.entity !is ModdedSlimeEntity<*>) {
            super.onEntityHit(entityHitResult)
        }
    }

    override fun getHitSound(): SoundEvent {
        return SoundEvents.ENTITY_HOSTILE_SPLASH
    }

    override fun asItemStack(): ItemStack = ItemStack.EMPTY

    override fun writeCustomDataToNbt(tag: NbtCompound) {
        super.writeCustomDataToNbt(tag)
        statusEffect?.let { tag.putString("statusEffect", Registry.STATUS_EFFECT.getId(it).toString()) }
    }

    override fun readCustomDataFromNbt(tag: NbtCompound) {
        super.readCustomDataFromNbt(tag)
        statusEffect = Registry.STATUS_EFFECT.get(Identifier(tag.getString("statusEffect")))
    }

    override fun createSpawnPacket(): Packet<*> {
        val buf = PacketByteBufs.create()
        buf.writeVarInt(id)
        buf.writeUuid(getUuid())
        buf.writeDouble(x)
        buf.writeDouble(y)
        buf.writeDouble(z)
        buf.writeByte(MathHelper.floor(pitch * 256.0f / 360.0f))
        buf.writeByte(MathHelper.floor(yaw * 256.0f / 360.0f))
        buf.writeInt(owner?.id ?: 0)

        return ServerPlayNetworking.createS2CPacket(PacketCompendium.SPAWN_SPIKE_ENTITY, buf)
    }



}
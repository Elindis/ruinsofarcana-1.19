package net.elindis.ruinsofarcana.effect;

import net.elindis.ruinsofarcana.mixin.PersistentProjectileEntityAccessor;
import net.elindis.ruinsofarcana.networking.ModPackets;
import net.elindis.ruinsofarcana.particle.ModParticles;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;

import java.util.List;


public class FirestormEffect extends StatusEffect {

    // ^ For when you create the firestorm effect
    public FirestormEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (!entity.world.isClient) {

        }

    }
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof ArmorStandEntity armorStandEntity) {
            armorStandEntity.kill();
        }
    }


    // Repels enemy projectiles!
    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.world.isClient()) {

            if (pLivingEntity instanceof ArmorStandEntity && pLivingEntity.isTouchingWaterOrRain()) {
                pLivingEntity.kill();
            }
            List<Entity> entityList = pLivingEntity.world.getOtherEntities(pLivingEntity, new Box(pLivingEntity.getBlockPos()).expand(2));
            for (Entity entity: entityList) {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.damage(DamageSource.ON_FIRE, 3);
                }
            }

            if (Random.create().nextBetween(0,3) == 1) {
                pLivingEntity.world.playSound(null, pLivingEntity.getBlockPos(), SoundEvents.BLOCK_CAMPFIRE_CRACKLE,
                        SoundCategory.NEUTRAL, 2f, 0.8f+Random.create().nextFloat()/3);
                pLivingEntity.world.playSound(null, pLivingEntity.getBlockPos(), SoundEvents.BLOCK_FIRE_AMBIENT,
                        SoundCategory.NEUTRAL, 2f, 0.8f+Random.create().nextFloat()/3);
            }
            ModParticleUtil.doFirestormParticles(pLivingEntity, ParticleTypes.FLAME, 9);
            ModParticleUtil.doFirestormParticles(pLivingEntity, ParticleTypes.LAVA, 5);

            super.applyUpdateEffect(pLivingEntity, pAmplifier);
        }
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}

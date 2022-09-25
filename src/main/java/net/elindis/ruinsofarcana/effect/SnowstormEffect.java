package net.elindis.ruinsofarcana.effect;

import net.elindis.ruinsofarcana.item.ModItems;
import net.elindis.ruinsofarcana.particle.ModParticles;
import net.elindis.ruinsofarcana.sound.ModSounds;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;

import java.util.List;


public class SnowstormEffect extends StatusEffect {

    // ^ For when you create the firestorm effect
    public SnowstormEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        entity.world.playSound(null, entity.getBlockPos(), ModSounds.SNOWSTORM,
                SoundCategory.NEUTRAL, 1.0f, 0.8f+Random.create().nextFloat()/3);
//        if (!entity.world.isClient) {
//            PacketByteBuf buffer = PacketByteBufs.create();
//            for (ServerPlayerEntity spe : PlayerLookup.around(entity.getServer().getWorld(entity.world.getRegistryKey()), entity.getPos(), 50)) {
//                System.out.println("snowstorm sent");
//                ServerPlayNetworking.send(spe, ModPackets.SNOWSTORM_ID, buffer);
//
//            }
//
//        }


    }
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
//        MinecraftClient.getInstance().getSoundManager().stopSounds(ModSounds.SNOWSTORM_ID, SoundCategory.NEUTRAL);
        if (entity instanceof ArmorStandEntity armorStandEntity) {
            armorStandEntity.kill();

        }
    }


    // Repels enemy projectiles!
    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.world.isClient()) {
            // Not to be used underwater.
            if (pLivingEntity instanceof ArmorStandEntity && pLivingEntity.isSubmergedInWater()) {
                pLivingEntity.kill();
            }

            List<Entity> entityList = pLivingEntity.world.getOtherEntities(pLivingEntity, new Box(pLivingEntity.getBlockPos()).expand(2));
            for (Entity entity: entityList) {
                if (entity instanceof PlayerEntity playerEntity) {
                    // Players wielding a frozen lance are immune
                    if (playerEntity.getMainHandStack().isOf(ModItems.FROZEN_LANCE) || playerEntity.getOffHandStack().isOf(ModItems.FROZEN_LANCE)) {
                        continue;
                    }
                }
                // Removes firestorms nearby (doesn't work for some reason)
//                if (entity instanceof ArmorStandEntity armorStandEntity) {
//                    if (armorStandEntity.hasStatusEffect(ModEffects.FIRESTORM)) {
//                        armorStandEntity.removeStatusEffect(ModEffects.FIRESTORM);
//                    }
//                }
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.damage(DamageSource.FREEZE, 2);
                    livingEntity.extinguish();
                    livingEntity.setFrozenTicks(livingEntity.getFrozenTicks()+4);
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 2, false, false, false));


                }
                // Blazes take double damage
                if (entity instanceof BlazeEntity blazeEntity) {
                    blazeEntity.damage(DamageSource.FREEZE, 2);
                }
            }

            if (Random.create().nextBetween(0,10) == 1) {
                // chance to put out fires
                BlockPos.iterateOutwards(pLivingEntity.getBlockPos(), 3, 2, 3).forEach(blockPos1 -> {
                    if (Random.create().nextBetween(0,2) == 0) {
                        if (pLivingEntity.world.getBlockState(blockPos1).isOf(Blocks.FIRE) || pLivingEntity.world.getBlockState(blockPos1).isOf(Blocks.SOUL_FIRE)) {
                            pLivingEntity.world.setBlockState(blockPos1, Blocks.AIR.getDefaultState());

                        }
                    }
                });
                pLivingEntity.world.playSound(null, pLivingEntity.getBlockPos(), SoundEvents.BLOCK_SMALL_AMETHYST_BUD_BREAK,
                        SoundCategory.NEUTRAL, 1.6f, 0.8f+Random.create().nextFloat()/3);

            }
            if (Random.create().nextBetween(0,2) == 1) {
                pLivingEntity.world.playSound(null, pLivingEntity.getBlockPos(), ModSounds.SNOWSTORM,
                        SoundCategory.NEUTRAL, 0.7f, 0.8f+Random.create().nextFloat()/3);

            }

            ModParticleUtil.doSnowstormParticles(pLivingEntity, ModParticles.SNOW_TORNADO_PARTICLE, 15);

            super.applyUpdateEffect(pLivingEntity, pAmplifier);
        }
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}

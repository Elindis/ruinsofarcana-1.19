package net.elindis.ruinsofarcana.effect;

import net.elindis.ruinsofarcana.mixin.PersistentProjectileEntityAccessor;
import net.elindis.ruinsofarcana.particle.ModParticles;
import net.elindis.ruinsofarcana.sound.ModSounds;
import net.elindis.ruinsofarcana.sound.WhirlwindSound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.sound.SoundCategory;

import java.util.List;


public class RepelEffect extends StatusEffect {

    protected RepelEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
    WhirlwindSound whirlwindSound = new WhirlwindSound(ModSounds.WHIRLWIND, SoundCategory.PLAYERS, SoundInstance.createRandom(), null);

        public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        whirlwindSound.setPlayer((PlayerEntity) entity);
        super.onRemoved(entity, attributes, amplifier);
        // start the sound effect
        MinecraftClient.getInstance().getSoundManager().play(whirlwindSound);
        whirlwindSound.setVolume(0.00f);
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        // stop the sound effect
        MinecraftClient.getInstance().getSoundManager().stop(whirlwindSound);
    }


    // Repels enemy projectiles!
    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        doParticles(pLivingEntity);
        if (!MinecraftClient.getInstance().getSoundManager().isPlaying(whirlwindSound)) {
            MinecraftClient.getInstance().getSoundManager().play(whirlwindSound);
        } else {
            whirlwindSound.setPlayer((PlayerEntity) pLivingEntity);
        }
        if (whirlwindSound.getVolume() < 0.7f) {
            whirlwindSound.setVolume(whirlwindSound.getVolume()+0.02f);
        }
        if (pLivingEntity.hasStatusEffect(ModEffects.REPEL)) {
            System.out.println("we have it");
            if ((pLivingEntity.getStatusEffect(ModEffects.REPEL).getDuration() < 20)) {
                System.out.println("duration is low");
                if (whirlwindSound.getVolume() > 0) {
                    whirlwindSound.setVolume(whirlwindSound.getVolume()-0.04f);
                    System.out.println("doing the thing");
                }
            }
        }

        if (!pLivingEntity.world.isClient()) {

            List<Entity> projectileList = pLivingEntity.world.getOtherEntities(pLivingEntity, pLivingEntity.getBoundingBox().expand(1.5));

            for (Entity entity:projectileList) {

                if (entity instanceof ProjectileEntity) {
                    // This is a bit of witchcraft.
                    boolean inGround = ((PersistentProjectileEntityAccessor) entity).getInGround();
                    if (!inGround) {

                        if (((ProjectileEntity) entity).getOwner() != null) {
                            if (((ProjectileEntity) entity).getOwner().equals(pLivingEntity)) {
                                // This might open up a weak spot when the player shoots a projectile.
                                // But I'm okay with that!
                                return;
                            }
                        }

                        entity.setYaw(entity.getYaw()*-1);
                        entity.setPitch(entity.getPitch()*-1);
                        entity.setVelocity(entity.getVelocity().multiply(-1));
                    }

                }

                double playerX = pLivingEntity.getX();
                double playerZ = pLivingEntity.getZ();
                double targetX = entity.getX();
                double targetZ = entity.getZ();
                if (entity instanceof LivingEntity) {
                    entity.addVelocity((targetX-playerX)/2, 0.4, (targetZ-playerZ)/2);
                } else if (entity instanceof ItemEntity) {
                    entity.addVelocity((targetX-playerX)/8, 0.1, (targetZ-playerZ)/8);
                } else {
                    entity.addVelocity((targetX-playerX)/3, 0.2, (targetZ-playerZ)/3);
                }

            }
        }
        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    private void doParticles(LivingEntity pLivingEntity) {
        for (int i = 0; i < 5; i++) {
            double randomOffsetY = pLivingEntity.getRandom().nextFloat()*2-0.5;
            double randomOffsetX = pLivingEntity.getRandom().nextFloat()*2-1;
            double randomOffsetZ = pLivingEntity.getRandom().nextFloat()*2-1;
            pLivingEntity.world.addParticle(ModParticles.TORNADO_PARTICLE, pLivingEntity.getX()+randomOffsetX,
                    pLivingEntity.getY()+randomOffsetY+1, pLivingEntity.getZ()+randomOffsetZ,
                    0,0,0);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}

package net.elindis.ruinsofarcana.effect;

import net.elindis.ruinsofarcana.mixin.PersistentProjectileEntityAccessor;
import net.elindis.ruinsofarcana.networking.ModPackets;
import net.elindis.ruinsofarcana.particle.ModParticles;
import net.elindis.ruinsofarcana.sound.ModSounds;
import net.elindis.ruinsofarcana.sound.WhirlwindSound;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

import java.util.List;


public class RepelEffect extends StatusEffect {

    int counter = 0;
    public RepelEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
//        this.sound = sound;
    }
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {

        super.onRemoved(entity, attributes, amplifier);
//        entity.playSound(ModSounds.WHIRLWIND, 1f, 1f);
//            entity.world.playSoundFromEntity(null, entity, ModSounds.WHIRLWIND, SoundCategory.PLAYERS, 1f, 1f);
        if (!entity.world.isClient) {
            PacketByteBuf buffer = PacketByteBufs.create();
            ServerPlayNetworking.send((ServerPlayerEntity) entity, ModPackets.WHIRLWIND_ID, buffer);
        }

    }
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        // stop the sound effect
//        MinecraftClient.getInstance().getSoundManager().stopSounds(ModSounds.WHIRLWIND_ID, SoundCategory.PLAYERS);

    }


    // Repels enemy projectiles!
    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
//        if (this.counter == 0) {
//            System.out.println("playing a sound");
//            pLivingEntity.world.playSoundFromEntity(null, pLivingEntity, ModSounds.WHIRLWIND, SoundCategory.PLAYERS, 1f, 1f);
//        }
//        this.counter++;
//        if (this.counter > 660) {
//            System.out.println("resetting the timer");
//            this.counter = 0;
//        }
        if (pLivingEntity.world.isClient) {
            doParticles(pLivingEntity);

//        if (!MinecraftClient.getInstance().getSoundManager().isPlaying(sound)) {
//            MinecraftClient.getInstance().getSoundManager().play(sound);
//        } else {
//            sound.setPlayer((PlayerEntity) pLivingEntity);
//        }
//        if (sound.getVolume() < 0.7f) {
//            sound.setVolume(sound.getVolume()+0.02f);
//        }
//        if (pLivingEntity.hasStatusEffect(ModEffects.REPEL)) {
//            System.out.println("we have it");
//            if ((pLivingEntity.getStatusEffect(ModEffects.REPEL).getDuration() < 20)) {
//                System.out.println("duration is low");
//                if (sound.getVolume() > 0) {
//                    sound.setVolume(sound.getVolume()-0.04f);
//                    System.out.println("doing the thing");
//                }
//            }
//        }
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

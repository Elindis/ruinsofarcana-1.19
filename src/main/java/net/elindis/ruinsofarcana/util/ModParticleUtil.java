package net.elindis.ruinsofarcana.util;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ModParticleUtil {

    public static void doTransmutationTableParticles(BlockEntity entity, ParticleType particleType, int intensity) {
        for (int i = 0; i < intensity; i++) {
            double x = entity.getPos().getX() + 0.5f;
            double y = entity.getPos().getY() + 1.5f;
            double z = entity.getPos().getZ() + 0.5f;
            double deltaX = (entity.getWorld().random.nextDouble());
            double deltaY = (entity.getWorld().random.nextDouble());
            double deltaZ = (entity.getWorld().random.nextDouble());
            PlayerLookup.tracking(entity).forEach(player -> ((ServerWorld) entity.getWorld())
                    .spawnParticles(player, (ParticleEffect) particleType, true, x, y, z, 1, deltaX, deltaY, deltaZ, 0.1));
        }
    }

    public static void doSingularityParticles(BlockEntity entity, ParticleType particleType, int intensity) {
        for (int i = 0; i < intensity; i++) {
            double x = entity.getPos().getX() + 0.5f;
            double y = entity.getPos().getY() + 0.5f;
            double z = entity.getPos().getZ() + 0.5f;
            double deltaX = (entity.getWorld().random.nextDouble());
            double deltaY = (entity.getWorld().random.nextDouble());
            double deltaZ = (entity.getWorld().random.nextDouble());
            PlayerLookup.tracking(entity).forEach(player -> ((ServerWorld) entity.getWorld())
                    .spawnParticles(player, (ParticleEffect) particleType, true, x, y, z, 1, deltaX, deltaY, deltaZ, 0.1));
        }
    }

    public static void doLivingEntityParticles(LivingEntity entity, ParticleType particleType, int intensity) {
        for (int i = 0; i < intensity; i++) {
            double x = entity.getPos().getX() + 0.0f;
            double y = entity.getPos().getY() + 0.0f;
            double z = entity.getPos().getZ() + 0.0f;
            double deltaX = (entity.getWorld().random.nextDouble());
            double deltaY = (entity.getWorld().random.nextDouble());
            double deltaZ = (entity.getWorld().random.nextDouble());
            PlayerLookup.tracking(entity).forEach(player -> ((ServerWorld) entity.getWorld())
                    .spawnParticles(player, (ParticleEffect) particleType, true, x, y, z, 1, deltaX, deltaY, deltaZ, 0.1));
        }
    }

    public static void doHealUndeadParticles(LivingEntity entity, ParticleType particleType, int intensity) {
        for (int i = 0; i < intensity; i++) {
            double x = entity.getPos().getX() + 0.0f;
            double y = entity.getPos().getY() + 0.8f;
            double z = entity.getPos().getZ() + 0.0f;
            double deltaY = (entity.getWorld().random.nextDouble());
            PlayerLookup.tracking(entity).forEach(player -> ((ServerWorld) entity.getWorld())
                    .spawnParticles(player, (ParticleEffect) particleType, true, x, y, z, 1, 0, deltaY, 0, 0.04));
        }
    }

    public static void doFirestormParticles(LivingEntity entity, ParticleType particleType, int intensity) {
        for (int i = 0; i < intensity; i++) {
            double x = entity.getPos().getX() + (Random.create().nextFloat()*4)-2f;
            double y = entity.getPos().getY() + 1.5f;
            double z = entity.getPos().getZ() + (Random.create().nextFloat()*4)-2f;
            double deltaX = (entity.getWorld().random.nextDouble());
            double deltaY = (entity.getWorld().random.nextDouble());
            double deltaZ = (entity.getWorld().random.nextDouble());
            PlayerLookup.tracking(entity).forEach(player -> ((ServerWorld) entity.getWorld())
                    .spawnParticles(player, (ParticleEffect) particleType, true, x, y, z, 1, deltaX, deltaY, deltaZ, 0.1));
        }
    }

    public static void doSnowstormParticles(LivingEntity entity, ParticleType particleType, int intensity) {
        for (int i = 0; i < intensity; i++) {
            double x = entity.getPos().getX() + (Random.create().nextFloat()*4)-2f;
            double y = entity.getPos().getY() + 0.5f + Random.create().nextFloat()*4;
            double z = entity.getPos().getZ() + (Random.create().nextFloat()*4)-2f;
            double deltaX = (entity.getWorld().random.nextDouble());
            double deltaY = (entity.getWorld().random.nextDouble());
            double deltaZ = (entity.getWorld().random.nextDouble());
            PlayerLookup.tracking(entity).forEach(player -> ((ServerWorld) entity.getWorld())
                    .spawnParticles(player, (ParticleEffect) particleType, true, x, y, z, 1, deltaX, deltaY, deltaZ, 0.1));
        }
    }

    public static void doProjectileParticles(PersistentProjectileEntity entity, ParticleType particleType, int intensity, float speed, double movementFactor) {
        for (int i = 0; i < intensity; i++) {
            double x = entity.getPos().getX();
            double y = entity.getPos().getY();
            double z = entity.getPos().getZ();
            double deltaX = (entity.getWorld().random.nextDouble()) * movementFactor;
            double deltaY = (entity.getWorld().random.nextDouble()) * movementFactor;
            double deltaZ = (entity.getWorld().random.nextDouble()) * movementFactor;
            PlayerLookup.tracking(entity).forEach(player -> ((ServerWorld) entity.getWorld())
                    .spawnParticles(player, (ParticleEffect) particleType, true, x, y, z, 1, deltaX, deltaY, deltaZ, speed));
        }
    }
    public static void doBlockParticles(World world, BlockPos pos, PlayerEntity player, ParticleType particleType, int intensity, float speed) {
        for (int i = 0; i < intensity; i++) {
            double x = pos.getX() + 0.5f;
            double y = pos.getY() + 0.5f;
            double z = pos.getZ() + 0.5f;
            double deltaX = (world.random.nextDouble());
            double deltaY = (world.random.nextDouble());
            double deltaZ = (world.random.nextDouble());
            if (world instanceof ServerWorld) {
                ((ServerWorld) world).spawnParticles((ServerPlayerEntity) player, (ParticleEffect) particleType,
                        true, x, y, z, 1, deltaX, deltaY, deltaZ, speed);
            }
        }
    }

    public static void doSmokeBombParticles(World world, BlockPos pos, ThrownItemEntity entity, ParticleType particleType, int intensity, float speed) {
        for (int i = 0; i < intensity; i++) {
            float random1 = world.getRandom().nextFloat()*7;
            float random2 = Random.create().nextFloat()*7;
            float random3 = Random.create().nextFloat();
            random1 -= 3.0f;
            random2 -= 3.0f;

            double x = pos.getX() + random1;
            double y = pos.getY() + 0.5 + random3*2;
            double z = pos.getZ() + random2;


            double deltaX = (world.random.nextDouble()/8);
            double deltaY = (world.random.nextDouble()/4)*0.01;
            double deltaZ = (world.random.nextDouble()/8);
            if (world instanceof ServerWorld) {
                PlayerLookup.tracking(entity).forEach(player -> ((ServerWorld) entity.getWorld())
                        .spawnParticles(player, (ParticleEffect) particleType, true, x, y, z, 1, deltaX, deltaY, deltaZ, speed));
            }
        }
    }



}

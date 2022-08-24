package net.elindis.ruinsofarcana.util;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
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


}

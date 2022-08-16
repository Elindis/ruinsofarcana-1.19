package net.elindis.ruinsofarcana.util;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;

public class ModParticles {

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


}

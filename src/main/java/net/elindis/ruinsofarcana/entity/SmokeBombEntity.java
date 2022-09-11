package net.elindis.ruinsofarcana.entity;


import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class SmokeBombEntity extends ThrownItemEntity {

    public SmokeBombEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public SmokeBombEntity(World world, LivingEntity owner) {
        super(ModEntities.SMOKEBOMB_ENTITY_TYPE, owner, world);
    }

    public SmokeBombEntity(World world, double x, double y, double z) {
        super(ModEntities.SMOKEBOMB_ENTITY_TYPE, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.FIRE_CHARGE;
    }

    @Environment(EnvType.CLIENT)
    // Not entirely sure, but probably has do to with the particles. (OPTIONAL)
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.SMOKE : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }

    @Environment(EnvType.CLIENT)
    // Also not entirely sure, but probably also has to do with the particles. (OPTIONAL)
    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();
            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
    }

    // Blinds all entities in a radius. Even the player!
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.world.isClient) {

            Vec3d collisionPos = hitResult.getPos();
            BlockPos blockPos = new BlockPos(collisionPos);

            ModParticleUtil.doSmokeBombParticles(this.world, blockPos, this, ParticleTypes.CAMPFIRE_COSY_SMOKE, 150, 0.01f);

            Box box = Box.from(collisionPos).expand(3);
            List<Entity> entityList = this.world.getOtherEntities(this, box);
            for (Entity entity: entityList) {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0));
                }
            }

            this.world.sendEntityStatus(this, (byte)3); // particle?
            this.kill();
        }
    }
}

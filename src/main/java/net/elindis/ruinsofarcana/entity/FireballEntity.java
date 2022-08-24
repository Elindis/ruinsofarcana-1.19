package net.elindis.ruinsofarcana.entity;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class FireballEntity extends ThrownItemEntity {
    public float explosivePower = 4f;
    public FireballEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public FireballEntity(World world, LivingEntity owner) {
        super(ModEntities.FIREBALL_ENTITY_TYPE, owner, world);
    }
    public FireballEntity(World world, LivingEntity owner, float explosivePower) {
        super(ModEntities.FIREBALL_ENTITY_TYPE, owner, world);
        this.explosivePower = explosivePower;
    }

    public FireballEntity(World world, double x, double y, double z, float explosivePower) {
        super(ModEntities.FIREBALL_ENTITY_TYPE, x, y, z, world);
        this.explosivePower = explosivePower;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.FIRE_CHARGE;
    }

    @Environment(EnvType.CLIENT)
    // Not entirely sure, but probably has do to with the particles. (OPTIONAL)
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.FALLING_LAVA : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
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

    protected void onEntityHit(EntityHitResult entityHitResult) { // Called on entity hit. Not used in this case, but saved for iteration.
        super.onEntityHit(entityHitResult);
//        if (!this.world.isClient) {
//            Entity entity = entityHitResult.getEntity(); // sets a new Entity instance as the EntityHitResult (victim)
//            if (entity instanceof LivingEntity livingEntity && !entity.isPlayer()) {
//                this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(),
//                        4.0f, Explosion.DestructionType.DESTROY).collectBlocksAndDamageEntities();
//            }
//        }
    }

    protected void onCollision(HitResult hitResult) { // Called on collision with a block.
        super.onCollision(hitResult);
        if (!this.world.isClient) { // checks if the world is NOT client
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(),
                    getExplosivePower(), true, Explosion.DestructionType.BREAK);//.collectBlocksAndDamageEntities();
            this.world.sendEntityStatus(this, (byte)3); // particle?
            this.kill(); // kills the projectile
        }
    }
//    @Override
//    public Packet<?> createSpawnPacket() {
//        return EntitySpawnPacket.create(this, EntitySpawnPacket.PacketID);
//    }

    public float getExplosivePower() {
        return explosivePower;
    }
}

package net.elindis.ruinsofarcana.entity;

import net.elindis.ruinsofarcana.item.ModItems;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LightArrowEntity extends ArrowEntity {
    public LightArrowEntity(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
        setNoGravity(true);
    }
    public LightArrowEntity(World world, LivingEntity owner) {
        super(ModEntities.LIGHT_ARROW_ENTITY_TYPE, world);
        setNoGravity(true);
        float initialOffsetX = 0;
        float initialOffsetZ = 0;
        if (owner.getMainHandStack().isOf(ModItems.LIGHT_BOW)) {
            initialOffsetX += (-MathHelper.cos(MathHelper.RADIANS_PER_DEGREE * owner.getHeadYaw())/4);
            initialOffsetZ += (-MathHelper.sin(MathHelper.RADIANS_PER_DEGREE * owner.getHeadYaw())/4);
        }
        if (owner.getOffHandStack().isOf(ModItems.LIGHT_BOW)) {
            initialOffsetX += (MathHelper.cos(MathHelper.RADIANS_PER_DEGREE * owner.getHeadYaw())/4);
            initialOffsetZ += (MathHelper.sin(MathHelper.RADIANS_PER_DEGREE * owner.getHeadYaw())/4);
        }
        this.updatePosition(owner.getX()+initialOffsetX, owner.getEyeY() - 0.11f, owner.getZ()+initialOffsetZ);
        setOwner(owner);

        if (owner instanceof PlayerEntity) {
            this.pickupType = PickupPermission.DISALLOWED;
        }
    }

    public LightArrowEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public void tick() {
        super.tick();
        this.world.addParticle(ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        if (!world.isClient) {
            if (!this.inGround) {
                ModParticleUtil.doProjectileParticles(this, ParticleTypes.END_ROD,2, 0.02f, 0);
            }
            if (this.age > 40) {
                discard();
            }

        }
    }
    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        if (!world.isClient) {
            target.setOnFireFor(5);
        }
    }
//    @Override
//    public Packet<?> createSpawnPacket() {
//       // return EntitySpawnPacket.create(this, EntitySpawnPacket.PacketID);
//        return new CustomPayloadS2CPacket(EntitySpawnPacket.PacketID, EntitySpawnPacket.WriteEntitySpawn(this));
//    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK;
    }

//    @Override
//    protected void onBlockHit(BlockHitResult blockHitResult) {
//        if (!world.isClient) {
//            this.setSound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME);
//            this.discard();
//        }
//        this.playSound(this.getSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
//    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        //this.inBlockState = this.world.getBlockState(blockHitResult.getBlockPos());
        super.onBlockHit(blockHitResult);
        Vec3d vec3d = blockHitResult.getPos().subtract(this.getX(), this.getY(), this.getZ());
        this.setVelocity(vec3d);
        Vec3d vec3d2 = vec3d.normalize().multiply(0.05f);
        this.setPos(this.getX() - vec3d2.x, this.getY() - vec3d2.y, this.getZ() - vec3d2.z);
        this.setSound(SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK);
        this.playSound(this.getSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
        this.inGround = true;
        this.shake = 7;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        this.setSound(SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK);
        DamageSource damageSource;
        Entity entity2;
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        float f = (float)this.getVelocity().length();
        int i = MathHelper.ceil(MathHelper.clamp((double)f * this.getDamage(), 0.0, 2.147483647E9));

        if (this.isCritical()) {
            long l = this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(l + (long)i, Integer.MAX_VALUE);
        }
        if ((entity2 = this.getOwner()) == null) {
            damageSource = DamageSource.magic(this, this);
        } else {
            damageSource = DamageSource.magic(this, entity2);
            if (entity2 instanceof LivingEntity) {
                ((LivingEntity)entity2).onAttacking(entity);
            }
        }
        if (entity.damage(damageSource, i)) {

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;

                if (!this.world.isClient && entity2 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity);
                }
                this.onHit(livingEntity);
                if (entity2 != null && livingEntity != entity2 && livingEntity instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)entity2).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                }

            }
        } else {
            //this.setVelocity(this.getVelocity().multiply(-0.1));
            //this.setYaw(this.getYaw() + 180.0f);
            //this.prevYaw += 180.0f;
            if (!this.world.isClient && this.getVelocity().lengthSquared() < 1.0E-7) {
//                if (this.pickupType == PickupPermission.ALLOWED) {
//                    this.dropStack(this.asItemStack(), 0.1f);
//                }
                this.discard();
            }
        }
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            if (playerEntity.getStuckArrowCount() != 0) {
                playerEntity.setStuckArrowCount(playerEntity.getStuckArrowCount()-1);
            }
        }
    }

}

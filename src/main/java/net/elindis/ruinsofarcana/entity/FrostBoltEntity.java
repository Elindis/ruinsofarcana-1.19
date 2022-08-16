package net.elindis.ruinsofarcana.entity;

import net.elindis.ruinsofarcana.util.ModParticles;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class FrostBoltEntity extends PersistentProjectileEntity {

    public FrostBoltEntity(EntityType entityType, World world) {
        super(ModEntities.FROST_BOLT_ENTITY_ENTITY_TYPE, world);
    }
    public FrostBoltEntity(World world, LivingEntity owner) {
        super(ModEntities.FROST_BOLT_ENTITY_ENTITY_TYPE,owner, world);
        this.updatePosition(owner.getX(), owner.getEyeY() - 0.10000000149011612D, owner.getZ());
        setOwner(owner);

        if (owner instanceof PlayerEntity) {
            this.pickupType = PickupPermission.DISALLOWED;
        }
    }

    public FrostBoltEntity(World world, double x, double y, double z) {
        super(ModEntities.FROST_BOLT_ENTITY_ENTITY_TYPE, x, y, z, world);
    }

    public static void freezeWater(FrostBoltEntity entity, World world, BlockPos blockPos, int level) {

        BlockState blockState = Blocks.FROSTED_ICE.getDefaultState();
        float f = Math.min(16, level);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-f, 2, -f), blockPos.add(f, 0, f))) {
            BlockState blockState3;
            mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
            BlockState blockState2 = world.getBlockState(mutable);
            if (!blockState2.isAir() || (blockState3 = world.getBlockState(blockPos2)).getMaterial() != Material.WATER || blockState3.get(FluidBlock.LEVEL) != 0 ||
                    !blockState.canPlaceAt(world, blockPos2) || !world.canPlace(blockState, blockPos2, ShapeContext.absent())) continue;
            world.setBlockState(blockPos2, blockState);
            world.createAndScheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, MathHelper.nextInt(Random.create(), 60, 120));
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.NEUTRAL, 0.7f, 1.5f / entity.getRandom());
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 0.4f, 1.5f / entity.getRandom());
            entity.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.inGround && !this.world.isClient) {
            ModParticles.doProjectileParticles(this, ParticleTypes.SNOWFLAKE, 20, 0.1f, 0.1d);
        }
        if (!this.world.isClient) {
            freezeWater(this, world, new BlockPos(this.getX(), this.getY(), this.getZ()), 1);
        }
        if (this.age > 60 && !world.isClient) {
            discard();
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
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 3));
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

    private float getRandom() {
        return this.random.nextFloat() * 0.2f + 0.9f;
    }

    // -> This would make the projectile disappear on hitting a block.
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
                    livingEntity.setStuckArrowCount(0);
                    EnchantmentHelper.onUserDamaged(livingEntity, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity);
                }
                this.onHit(livingEntity);
                if (entity2 != null && livingEntity != entity2 && livingEntity instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)entity2).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                }

            }
        } else {
            this.setVelocity(this.getVelocity().multiply(-0.1));
            this.setYaw(this.getYaw() + 180.0f);
            this.prevYaw += 180.0f;
            if (!this.world.isClient && this.getVelocity().lengthSquared() < 1.0E-7) {
                this.discard();
            }
        }
        // Duct tape fix to resolve the problem of the projectile causing an arrow to appear in player models.
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            if (playerEntity.getStuckArrowCount() != 0) {
                playerEntity.setStuckArrowCount(playerEntity.getStuckArrowCount()-1);
            }
        }
    }
}

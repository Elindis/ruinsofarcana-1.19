package net.elindis.ruinsofarcana.entity;

import net.elindis.ruinsofarcana.block.ModBlocks;
import net.elindis.ruinsofarcana.enchantment.ModEnchantments;
import net.elindis.ruinsofarcana.item.ModItems;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Iterator;

public class FrostBoltEntity extends PersistentProjectileEntity {

    public FrostBoltEntity(EntityType entityType, World world) {
        super(ModEntities.FROST_BOLT_ENTITY_ENTITY_TYPE, world);
    }
    public FrostBoltEntity(World world, LivingEntity owner) {
        super(ModEntities.FROST_BOLT_ENTITY_ENTITY_TYPE,owner, world);
        float initialOffsetX = 0;
        float initialOffsetZ = 0;
        if (owner.getMainHandStack().isOf(ModItems.WAND_OF_FROST_BOLT)) {
            initialOffsetX += (-MathHelper.cos(MathHelper.RADIANS_PER_DEGREE * owner.getHeadYaw())/4);
            initialOffsetZ += (-MathHelper.sin(MathHelper.RADIANS_PER_DEGREE * owner.getHeadYaw())/4);
        }
        if (owner.getOffHandStack().isOf(ModItems.WAND_OF_FROST_BOLT)) {
            initialOffsetX += (MathHelper.cos(MathHelper.RADIANS_PER_DEGREE * owner.getHeadYaw())/4);
            initialOffsetZ += (MathHelper.sin(MathHelper.RADIANS_PER_DEGREE * owner.getHeadYaw())/4);
        }
        this.updatePosition(owner.getX()+initialOffsetX, owner.getEyeY() , owner.getZ()+initialOffsetZ);
        setOwner(owner);

        this.pickupType = PickupPermission.DISALLOWED;
    }

    public FrostBoltEntity(World world, double x, double y, double z) {
        super(ModEntities.FROST_BOLT_ENTITY_ENTITY_TYPE, x, y, z, world);
    }
    public static void freezeWater(FrostBoltEntity entity, World world, BlockPos blockPos, int level) {
        boolean playSounds = true;
        BlockState blockState = Blocks.FROSTED_ICE.getDefaultState();
        float f = (float)Math.min(16, 2 + level);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        Iterator var7 = BlockPos.iterate(blockPos.add((double)(-f), 2, (double)(-f)), blockPos.add((double)f, 0, (double)f)).iterator();

        while(var7.hasNext()) {
            BlockPos blockPos2 = (BlockPos)var7.next();
            if (blockPos2.isWithinDistance(entity.getPos(), (double)f)) {
                mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                BlockState blockState2 = world.getBlockState(mutable);
                if (blockState2.isAir()) {
                    BlockState blockState3 = world.getBlockState(blockPos2);
                    if (blockState3.getMaterial() == Material.WATER && (Integer)blockState3.get(FluidBlock.LEVEL) == 0 && blockState.canPlaceAt(world, blockPos2)
                            && world.canPlace(blockState, blockPos2, ShapeContext.absent())) {
                        world.setBlockState(blockPos2, blockState);
                        world.createAndScheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, MathHelper.nextInt(world.getRandom(), 60, 120));
                        if (playSounds) {
                            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 0.7f, 0.8f + entity.getRandom()/2);
                            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.NEUTRAL, 1f, 0.8f + entity.getRandom()/2);
                            playSounds = false;
                            entity.discard();
                        }

                    }
                }
            }
        }

    }
    public static void freezeLava(FrostBoltEntity entity, World world, BlockPos blockPos, int level) {
            boolean playSounds = true;
            BlockState blockState = ModBlocks.CRACKED_OBSIDIAN.getDefaultState();
            float f = (float)Math.min(16, 2 + level);
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            Iterator var7 = BlockPos.iterate(blockPos.add((double)(-f), 2, (double)(-f)), blockPos.add((double)f, 0, (double)f)).iterator();

            while(var7.hasNext()) {
                BlockPos blockPos2 = (BlockPos)var7.next();
                if (blockPos2.isWithinDistance(entity.getPos(), (double)f)) {
                    mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                    BlockState blockState2 = world.getBlockState(mutable);
                    if (blockState2.isAir()) {
                        BlockState blockState3 = world.getBlockState(blockPos2);
                        if (blockState3.getMaterial() == Material.LAVA && (Integer)blockState3.get(FluidBlock.LEVEL) == 0 && blockState.canPlaceAt(world, blockPos2)
                                && world.canPlace(blockState, blockPos2, ShapeContext.absent()) && blockState3.getFluidState().isStill()) {
                            world.setBlockState(blockPos2, blockState);
                            world.createAndScheduleBlockTick(blockPos2, ModBlocks.CRACKED_OBSIDIAN, MathHelper.nextInt(world.getRandom(), 200, 260));
                            if (playSounds) {
                                world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.NEUTRAL, 1.2f, 0.8f + entity.getRandom()/2);
                                world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.NEUTRAL, 1f, 0.8f + entity.getRandom()/2);
                                playSounds = false;
                                entity.discard();
                            }
                        }
                    }
                }
            }

    }

    @Override
    public void tick() {
        super.tick();
        if (!this.inGround && !this.world.isClient) {
            ModParticleUtil.doProjectileParticles(this, ParticleTypes.SNOWFLAKE, 20, 0.1f, 0.1d);
        }
        if (!this.world.isClient) {
            freezeWater(this, world, new BlockPos(this.getX(), this.getY(), this.getZ()), 1);
            freezeLava(this, world, new BlockPos(this.getX(), this.getY(), this.getZ()), 1);
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
            target.extinguish();
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 3));
//            target.addStatusEffect(new StatusEffectInstance(ModEffects.CONFUSION, 200, 3));
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 3));
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 200, 3));
            target.setFrozenTicks(600);
            target.damage(DamageSource.magic(this, target.getAttacker()), 10.0f);
//            ((MobEntity) target).setAiDisabled(true);
//            ((MobEntity) target).setTarget(null);
//            ((MobEntity) target).clearPositionTarget();
//            ((MobEntity) target).setAttacking(false);
//            ((MobEntity) target).setAttacking(null);
//            ((MobEntity) target).setAttacker(null);
//            ((MobEntity) target).clearGoalsAndTasks();



            // ===== WISDOM ENCHANTMENT INTEGRATION ===== //
            // For some reason it didn't work out of the box like the light bow did.

            if (target.getAttacker() == null) return;
            if (target.isDead() && (target.getAttacker().getMainHandStack().isOf(ModItems.WAND_OF_FROST_BOLT) ||
                    target.getAttacker().getOffHandStack().isOf(ModItems.WAND_OF_FROST_BOLT))) {
                int orbsToDropMH = EnchantmentHelper.getLevel(ModEnchantments.WISDOM, target.getAttacker().getMainHandStack());
                int orbsToDropOH = EnchantmentHelper.getLevel(ModEnchantments.WISDOM, target.getAttacker().getOffHandStack());
                ExperienceOrbEntity.spawn((ServerWorld) world, target.getPos(), Math.max(orbsToDropMH, orbsToDropOH));
            }
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

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!world.isClient) {
            Vec3d vec3d = blockHitResult.getPos().subtract(this.getX(), this.getY(), this.getZ());
            this.setVelocity(vec3d);
            Vec3d vec3d2 = vec3d.normalize().multiply(0.05f);
            this.setPos(this.getX() - vec3d2.x, this.getY() - vec3d2.y, this.getZ() - vec3d2.z);
            this.setSound(SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK);
            this.inGround = true;
            this.shake = 7;
            ModParticleUtil.doProjectileParticles(this, ParticleTypes.SNOWFLAKE, 50, 0.1f, 0.1d);
        }
        //this.inBlockState = this.world.getBlockState(blockHitResult.getBlockPos());
        this.playSound(this.getSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));


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
                    ModParticleUtil.doProjectileParticles(this, ParticleTypes.SNOWFLAKE, 50, 0.1f, 0.1d);
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
        if (entity instanceof PlayerEntity playerEntity) {
            if (playerEntity.getStuckArrowCount() != 0) {
                playerEntity.setStuckArrowCount(playerEntity.getStuckArrowCount()-1);
            }
        }
    }
}

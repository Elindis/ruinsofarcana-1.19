package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class FrozenLanceItem extends SwordItem {

    public FrozenLanceItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if ((double)state.getHardness(world, pos) != 0.0) {
            stack.damage(2, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1), attacker);
        target.setFrozenTicks(600);
        playSound((PlayerEntity) attacker);
        ModParticleUtil.doLivingEntityParticles(target, ParticleTypes.SNOWFLAKE, 40);
        target.extinguish();
        return true;
    }

    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity) {
            if (((PlayerEntity) entity).getMainHandStack().isOf(this.asItem()) ||
            ((PlayerEntity) entity).getOffHandStack().isOf(this.asItem())) {
                BlockPos pos = new BlockPos(entity.getPos());
                freezeWater((LivingEntity) entity, world, pos, 2);
            }
        }
    }

    private void playSound(PlayerEntity playerEntity) {
        playerEntity.world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                SoundEvents.BLOCK_SMALL_AMETHYST_BUD_BREAK, playerEntity.getSoundCategory(), 1.6f,
                 0.8f + (playerEntity.getRandom().nextFloat()/3));
    }
    public static void freezeWater(LivingEntity entity, World world, BlockPos blockPos, int level) {
        if (!entity.isOnGround()) {
            return;
        }
        BlockState blockState = Blocks.FROSTED_ICE.getDefaultState();
        float f = Math.min(16, 2 + level);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-f, -1.0, -f), blockPos.add(f, -1.0, f))) {
            BlockState blockState3;
            if (!blockPos2.isWithinDistance(entity.getPos(), (double)f)) continue;
            mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
            BlockState blockState2 = world.getBlockState(mutable);
            if (!blockState2.isAir() || (blockState3 = world.getBlockState(blockPos2)).getMaterial() != Material.WATER || blockState3.get(FluidBlock.LEVEL) != 0 || !blockState.canPlaceAt(world, blockPos2) || !world.canPlace(blockState, blockPos2, ShapeContext.absent())) continue;
            world.setBlockState(blockPos2, blockState);
            world.createAndScheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, MathHelper.nextInt(entity.getRandom(), 60, 120));
        }
    }
}



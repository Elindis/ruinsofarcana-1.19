package net.elindis.ruinsofarcana.block;


import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CrackedObsidianBlock extends FrostedIceBlock {
    public CrackedObsidianBlock(Settings settings) {
        super(settings);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((random.nextInt(3) == 0 || this.canMelt(world, pos, 1)) && this.increaseAge(state, world, pos)) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            Direction[] var6 = Direction.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Direction direction = var6[var8];
                mutable.set(pos, direction);
                BlockState blockState = world.getBlockState(mutable);
                if (blockState.isOf(this) && !this.increaseAge(blockState, world, mutable)) {
                    world.createAndScheduleBlockTick(mutable, this, MathHelper.nextInt(random, 400, 600));
                }
            }

        } else {
            world.createAndScheduleBlockTick(pos, this, MathHelper.nextInt(random, 400, 600));
        }
    }

    private boolean increaseAge(BlockState state, World world, BlockPos pos) {
        int i = (Integer)state.get(AGE);
        if (i < 3) {
            world.setBlockState(pos, (BlockState)state.with(AGE, i + 1), 2);
            return false;
        } else {
            world.setBlockState(pos, Blocks.LAVA.getDefaultState());
            world.updateNeighbor(pos, Blocks.LAVA, pos);
            return true;
        }
    }

    private boolean canMelt(BlockView world, BlockPos pos, int maxNeighbors) {
        int i = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        Direction[] var6 = Direction.values();
        int var7 = var6.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            mutable.set(pos, direction);
            if (world.getBlockState(mutable).isOf(this)) {
                ++i;
                if (i >= maxNeighbors) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected void melt(BlockState state, World world, BlockPos pos) {
            world.setBlockState(pos, Blocks.LAVA.getDefaultState());
            world.updateNeighbor(pos, Blocks.LAVA, pos);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        world.setBlockState(pos, Blocks.LAVA.getDefaultState());
        world.updateNeighbor(pos, Blocks.LAVA, pos);
    }
}

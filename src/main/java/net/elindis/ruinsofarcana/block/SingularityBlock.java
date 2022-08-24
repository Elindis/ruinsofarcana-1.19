package net.elindis.ruinsofarcana.block;

import net.elindis.ruinsofarcana.block.entity.ModBlockEntities;
import net.elindis.ruinsofarcana.block.entity.SingularityBlockEntity;
import net.elindis.ruinsofarcana.sound.ModSounds;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

public class SingularityBlock extends BlockWithEntity implements BlockEntityProvider{
	private static final VoxelShape DEFAULT_SHAPE = Block.createCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0);

	public SingularityBlock(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SingularityBlockEntity(pos, state);
	}
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient)  {
			return checkType(type, ModBlockEntities.SINGULARITY, (SingularityBlockEntity::tick));
		} else {
			return checkType(type, ModBlockEntities.SINGULARITY, (SingularityBlockEntity::serverTick));
		}
	}
	@Nullable
	@Override
	public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
		return super.getGameEventListener(world, blockEntity);
	}
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return DEFAULT_SHAPE;
	}
	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return DEFAULT_SHAPE;
	}
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random){
		if (random.nextInt(4) == 0) {
			world.playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, ModSounds.SINGULARITY, SoundCategory.BLOCKS, 0.3f, random.nextFloat() * 0.4f + 0.8f, true);
		}
	}
}

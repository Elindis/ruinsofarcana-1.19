package net.elindis.ruinsofarcana.block;

import net.elindis.ruinsofarcana.block.entity.ModBlockEntities;
import net.elindis.ruinsofarcana.block.entity.PedestalBlockEntity;
import net.elindis.ruinsofarcana.block.entity.SingularityBlockEntity;
import net.elindis.ruinsofarcana.particle.ModParticles;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
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

	public void saveData(World world, BlockEntity singularityBlockEntity) {
		singularityBlockEntity.markDirty();
		singularityBlockEntity.toUpdatePacket();
		if (world != null && !world.isClient()) {
			world.updateListeners(singularityBlockEntity.getPos(), singularityBlockEntity.getCachedState(),
					singularityBlockEntity.getCachedState(), Block.NOTIFY_ALL);
		}
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
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof SingularityBlockEntity)) {
			return;
		}
//		for (int i = 0; i < 5; i++) {
//			world.addParticle(ModParticles.SPIRAL_PARTICLE, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, 0, 0,0);
//		}

//		int i = ((SingularityBlockEntity)blockEntity).getDrawnSidesCount();
//		for (int j = 0; j < i+3; ++j) {
//			double d = (double)pos.getX() + random.nextDouble();
//			double e = (double)pos.getY() + random.nextDouble();
//			double f = (double)pos.getZ() + random.nextDouble();
//			double g = (random.nextDouble() - 0.5) * 0.5;
//			double h = (random.nextDouble() - 0.5) * 0.5;
//			double k = (random.nextDouble() - 0.5) * 0.5;
//			int l = random.nextInt(2) * 2 - 1;
//			if (random.nextBoolean()) {
//				f = (double)pos.getZ() + 0.25 + 0.25 * (double)l;
//				k = random.nextFloat() * 2.0f * (float)l;
//			} else {
//				d = (double)pos.getX() + 0.25 + 0.25 * (double)l;
//				g = random.nextFloat() * 2.0f * (float)l;
//			}
//			world.addParticle(ModParticles.SINGULARITY_PARTICLE, d, e, f, g, h, k);
//		}
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

}

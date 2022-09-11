package net.elindis.ruinsofarcana.block.research;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.stream.Stream;

public class ChaliceBlock extends GenericRelicBlock {
	private static final VoxelShape DEFAULT_SHAPE = Stream.of(
			Block.createCuboidShape(6, 0, 6, 10, 1, 10),
			Block.createCuboidShape(7, 1, 7, 9, 4, 9),
			Block.createCuboidShape(6, 4, 6, 10, 6, 10),
			Block.createCuboidShape(5, 6, 5, 11, 7, 11),
			Block.createCuboidShape(5, 7, 5, 6, 8, 11),
			Block.createCuboidShape(10, 7, 5, 11, 8, 11),
			Block.createCuboidShape(6, 7, 10, 10, 8, 11),
			Block.createCuboidShape(6, 7, 5, 10, 8, 6)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
//
//	public static final BooleanProperty PRISTINE = BooleanProperty.of("pristine");
//	public static final IntProperty QUALITY = IntProperty.of("quality", 0, 5);


	public ChaliceBlock(Settings settings) {
		super(settings);
//		this.setDefaultState((this.stateManager.getDefaultState())
//				.with(PRISTINE, true)
//				.with(QUALITY, 0));
	}
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return DEFAULT_SHAPE;
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return DEFAULT_SHAPE;
	}

//	@Override
//	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//		if (!state.get(PRISTINE)) return ActionResult.PASS;
//		if (player.getMainHandStack().isOf(Items.PAPER) && player.getOffHandStack().isOf(Items.CHARCOAL) ||
//				(player.getOffHandStack().isOf(Items.PAPER) && player.getMainHandStack().isOf(Items.CHARCOAL))) {
//			if (world.isClient) return ActionResult.SUCCESS;
//			world.setBlockState(pos, ModBlocks.CHALICE.getDefaultState().with(PRISTINE, false));
//			player.getMainHandStack().decrement(1);
//			player.getOffHandStack().decrement(1);
//			player.getInventory().offerOrDrop(ModItems.CHARCOAL_RUBBING.getDefaultStack());
//			return ActionResult.PASS;
//		}
//		return ActionResult.PASS;
//	}

//	@Override
//	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
//		if (world.isClient) return;
//		boolean pristine = true;
//		if (itemStack.hasNbt()) {
//			assert itemStack.getNbt() != null;
//			pristine = itemStack.getNbt().getBoolean("pristine");
//		}
//		world.setBlockState(pos, state.with(PRISTINE, pristine));
//	}
//
//	@Override
//	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
//		this.spawnBreakParticles(world, player, pos, state);
////		if (player.getAbilities().creativeMode) return;
//		if (!world.isClient) {
//			ItemStack itemStack = new ItemStack(ModBlocks.STONE_TABLET.asItem());
//			boolean pristine = getStateWithProperties(world.getBlockState(pos)).get(PRISTINE);
//			NbtCompound nbt = new NbtCompound();
//			nbt.putBoolean("pristine", pristine);
//			itemStack.setNbt(nbt);
//			ItemScatterer.spawn(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, itemStack);
//		}
//		world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
//	}
//	@Override
//	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//		builder.add(PRISTINE).;
//	}


}

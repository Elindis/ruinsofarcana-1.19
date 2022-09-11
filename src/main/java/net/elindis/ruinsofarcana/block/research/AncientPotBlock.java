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

public class AncientPotBlock extends GenericRelicBlock {
	private static final VoxelShape DEFAULT_SHAPE = Stream.of(
			Block.createCuboidShape(5, 1, 5, 11, 2, 11),
			Block.createCuboidShape(6, 0, 6, 10, 1, 10),
			Block.createCuboidShape(4, 2, 4, 12, 6, 12),
			Block.createCuboidShape(6, 6, 6, 10, 8, 10),
			Block.createCuboidShape(5, 8, 5, 11, 10, 11)
	).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

//	public static final BooleanProperty PRISTINE = BooleanProperty.of("pristine");
//	public static final IntProperty QUALITY = IntProperty.of("quality", 0, 5);


	public AncientPotBlock(Settings settings) {
		super(settings);
//		this.setDefaultState((this.stateManager.getDefaultState())
//				.with(PRISTINE, true));
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
//			world.setBlockState(pos, ModBlocks.ANCIENT_POT.getDefaultState().with(PRISTINE, false));
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


}

package net.elindis.ruinsofarcana.block.research;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class CipheredScrollBlock extends GenericRelicBlock {
	private static final VoxelShape DEFAULT_SHAPE = Block.createCuboidShape(4, 0, 4, 12, 4, 12);
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty PRISTINE = BooleanProperty.of("pristine");
	public static final IntProperty QUALITY = IntProperty.of("quality", 0, 5);

	public CipheredScrollBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState())
				.with(FACING, Direction.NORTH));
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
//			world.setBlockState(pos, ModBlocks.CIPHERED_SRCOLL.getDefaultState().with(PRISTINE, false).with(FACING, state.get(FACING)));
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

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, PRISTINE, QUALITY);
	}


}

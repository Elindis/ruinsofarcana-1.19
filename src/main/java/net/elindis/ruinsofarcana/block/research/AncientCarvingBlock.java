package net.elindis.ruinsofarcana.block.research;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class AncientCarvingBlock extends GenericRelicBlock {
	private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
	private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
	private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
	private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
	private static final VoxelShape UP_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
	private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
	public static final DirectionProperty FACING = Properties.FACING;
	public static final DirectionProperty ROTATION = DirectionProperty.of("rotation");
//	public static final BooleanProperty PRISTINE = BooleanProperty.of("pristine");
//	public static final IntProperty QUALITY = IntProperty.of("quality", 0, 5);

	public AncientCarvingBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState())
				.with(FACING, Direction.NORTH)
				.with(ROTATION, Direction.NORTH));
//				.with(PRISTINE, true));
	}
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch (state.get(FACING)) {
			case DOWN -> DOWN_SHAPE;
			case UP -> UP_SHAPE;
			case NORTH -> NORTH_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case EAST -> EAST_SHAPE;
			case WEST -> WEST_SHAPE;
		};
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return switch (state.get(FACING)) {
			case DOWN -> DOWN_SHAPE;
			case UP -> UP_SHAPE;
			case NORTH -> NORTH_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case EAST -> EAST_SHAPE;
			case WEST -> WEST_SHAPE;
		};
	}

//	@Override
//	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//		if (!state.get(PRISTINE)) return ActionResult.PASS;
//		if (player.getMainHandStack().isOf(Items.PAPER) && player.getOffHandStack().isOf(Items.CHARCOAL) ||
//				(player.getOffHandStack().isOf(Items.PAPER) && player.getMainHandStack().isOf(Items.CHARCOAL))) {
//			if (world.isClient) return ActionResult.SUCCESS;
//			world.setBlockState(pos, ModBlocks.ANCIENT_CARVING.getDefaultState().with(PRISTINE, false).with(FACING, state.get(FACING)).with(ROTATION, state.get(ROTATION)));
//			player.getMainHandStack().decrement(1);
//			player.getOffHandStack().decrement(1);
//			player.getInventory().offerOrDrop(ModItems.CHARCOAL_RUBBING.getDefaultStack());
//			return ActionResult.PASS;
//		}
//		return ActionResult.PASS;
//	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction direction = ctx.getSide();
		Direction rotation = ctx.getPlayerFacing().getOpposite();
		return this.getDefaultState().with(FACING, direction).with(ROTATION, rotation);
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
		builder.add(FACING, PRISTINE, ROTATION, QUALITY);
	}


}

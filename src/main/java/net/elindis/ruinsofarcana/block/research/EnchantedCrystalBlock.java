package net.elindis.ruinsofarcana.block.research;

import net.elindis.ruinsofarcana.block.ModBlocks;
import net.elindis.ruinsofarcana.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class EnchantedCrystalBlock extends GenericRelicBlock {

	private static final VoxelShape DEFAULT_SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 10.0, 12.0);
	public static final BooleanProperty PRISTINE = BooleanProperty.of("pristine");


	public EnchantedCrystalBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState())
				.with(PRISTINE, true));
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
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!state.get(PRISTINE)) return ActionResult.PASS;
		if (player.getMainHandStack().isOf(Items.PAPER) && player.getOffHandStack().isOf(Items.CHARCOAL) ||
				(player.getOffHandStack().isOf(Items.PAPER) && player.getMainHandStack().isOf(Items.CHARCOAL))) {
			if (world.isClient) return ActionResult.SUCCESS;
			world.setBlockState(pos, ModBlocks.ENCHANTED_CRYSTAL.getDefaultState().with(PRISTINE, false));
			player.getMainHandStack().decrement(1);
			player.getOffHandStack().decrement(1);
			player.getInventory().offerOrDrop(ModItems.CHARCOAL_RUBBING.getDefaultStack());
			return ActionResult.PASS;
		}
		return ActionResult.PASS;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(PRISTINE);
	}


}

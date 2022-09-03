package net.elindis.ruinsofarcana.block.research;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public abstract class GenericRelicBlock extends Block {
		public static final BooleanProperty PRISTINE = BooleanProperty.of("pristine");

	public GenericRelicBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState())
				.with(PRISTINE, true));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.isClient) return;
		boolean pristine = true;
		if (itemStack.hasNbt()) {
			assert itemStack.getNbt() != null;
			pristine = itemStack.getNbt().getBoolean("pristine");
		}
		world.setBlockState(pos, state.with(PRISTINE, pristine));
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		this.spawnBreakParticles(world, player, pos, state);
//		if (player.getAbilities().creativeMode) return;
		if (!world.isClient) {
			ItemStack itemStack = new ItemStack(this.asItem());
			boolean pristine = getStateWithProperties(world.getBlockState(pos)).get(PRISTINE);
			NbtCompound nbt = new NbtCompound();
			nbt.putBoolean("pristine", pristine);
			itemStack.setNbt(nbt);
			ItemScatterer.spawn(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, itemStack);
		}
		world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(PRISTINE);
	}


}

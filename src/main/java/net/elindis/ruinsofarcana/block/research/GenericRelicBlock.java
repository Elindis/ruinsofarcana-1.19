package net.elindis.ruinsofarcana.block.research;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class GenericRelicBlock extends Block {
		public static final BooleanProperty PRISTINE = BooleanProperty.of("pristine");
		public static final IntProperty QUALITY = IntProperty.of("quality", 0, 5);

	public GenericRelicBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState())
				.with(PRISTINE, true)
				.with(QUALITY, 0));
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		if (stack.getNbt() != null) {
			int quality = stack.getNbt().getInt("quality");
			String stars = getStars(quality);
			tooltip.add(Text.of("Quality: "+stars));
			boolean pristine = stack.getNbt().getBoolean("pristine");
			if (pristine) {
				tooltip.add(Text.of("Pristine"));
			}
		}
	}
	private String getStars(int quality) {
		// ★☆
		return switch (quality) {
			case 1 -> "★";
			case 2 -> "★★";
			case 3 -> "★★★";
			case 4 -> "★★★★";
			case 5 -> "★★★★★";
			default -> "Unidentified";
		};
	}
	public static int getQuality(BlockState state) {
		return state.get(QUALITY);
	}
	public static boolean getPristine(BlockState state) {
		return state.get(PRISTINE);
	}
	public static void setPristine(World world, BlockPos pos, BlockState state, boolean value) {
		world.setBlockState(pos, state.with(PRISTINE, value));
	}

	@Deprecated
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.getStackInHand(hand).isOf(Items.SPYGLASS) && state.get(QUALITY).equals(0)) {
			if (world.isClient) return ActionResult.PASS;
			// Relic quality is distributed on a curve.
			// 1: 30%  2: 25%  3: 20%  4: 15%  5: 10%
			int quality = randomFromCurve(30,25,20,15,10);
			world.setBlockState(pos, getStateWithProperties(state).with(QUALITY, quality));
		}
		return ActionResult.PASS;
	}

	private int randomFromCurve(int poor, int fair, int good, int great, int excellent) {
		int quality = 0;
		int random = Random.create().nextBetween(1,100);
		if (random <= poor) {
			quality = 1;
		}
		if (random > poor && random <= poor+fair) {
			quality = 2;
		}
		if (random > poor+fair && random <= poor+fair+good ) {
			quality = 3;
		}
		if (random > poor+fair+good && random <= poor+fair+good+great ) {
			quality = 4;
		}
		if (random > poor+fair+good+great && random <= poor+fair+good+great+excellent ) {
			quality = 5;
		}
		return quality;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.isClient) return;
		boolean pristine = true;
		int quality = 0;
		if (itemStack.hasNbt()) {
			assert itemStack.getNbt() != null;
			pristine = itemStack.getNbt().getBoolean("pristine");
			quality = itemStack.getNbt().getInt("quality");
		}
		world.setBlockState(pos, state.with(PRISTINE, pristine).with(QUALITY, quality));
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		this.spawnBreakParticles(world, player, pos, state);
//		if (player.getAbilities().creativeMode) return;
		if (!world.isClient) {
			ItemStack itemStack = new ItemStack(this.asItem());
			boolean pristine = getStateWithProperties(world.getBlockState(pos)).get(PRISTINE);
			int quality = getStateWithProperties(world.getBlockState(pos)).get(QUALITY);
			NbtCompound nbt = new NbtCompound();
			nbt.putBoolean("pristine", pristine);
			nbt.putInt("quality", quality);
			itemStack.setNbt(nbt);
			ItemScatterer.spawn(world, pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f, itemStack);
		}
		world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(PRISTINE).add(QUALITY);
	}


}

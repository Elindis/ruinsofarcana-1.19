package net.elindis.ruinsofarcana.item.equipment;

import net.minecraft.item.Item;

public class ScribesPenItem extends Item {
	public ScribesPenItem(Settings settings) {
		super(settings);
	}


//	@Override
//	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//		super.use(world, user, hand);
//		(ServerWorld) world.getServer().getCommandManager().getDispatcher().execute(
//		)
//		return TypedActionResult.pass(user.getStackInHand(hand));
//	}


	//	private void openAdvancementScreen(World world, PlayerEntity user, Hand hand) {
//		assert MinecraftClient.getInstance().player != null;
//
//		AdvancementsScreen advancementsScreen = new AdvancementsScreen(MinecraftClient.getInstance().player.networkHandler.getAdvancementHandler());
//		Advancement modAdvancement = getModAdvancement(user);
//
//		if (world.isClient)	MinecraftClient.getInstance().setScreen(advancementsScreen);
//		Screen modScreen = MinecraftClient.getInstance().currentScreen;
//
//		if (modScreen instanceof AdvancementsScreen) {
//			((AdvancementsScreen) modScreen).selectTab(modAdvancement);
//		}
//	}
//
//	private Advancement getModAdvancement(PlayerEntity player) {
//		Identifier id = new Identifier("ruinsofarcana:lightbow");
//		if (player instanceof ServerPlayerEntity) {
//			return Objects.requireNonNull(player.getServer()).getAdvancementLoader().get(id);
//		}
//		else return null;
//	}

}

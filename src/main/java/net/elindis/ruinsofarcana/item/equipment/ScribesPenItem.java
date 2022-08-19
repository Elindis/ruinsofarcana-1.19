package net.elindis.ruinsofarcana.item.equipment;

import net.minecraft.advancement.Advancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ScribesPenItem extends Item {
	public ScribesPenItem(Settings settings) {
		super(settings);
	}

	private void openAdvancementScreen(World world, PlayerEntity user, Hand hand) {
		assert MinecraftClient.getInstance().player != null;

		AdvancementsScreen advancementsScreen = new AdvancementsScreen(MinecraftClient.getInstance().player.networkHandler.getAdvancementHandler());
		Advancement modAdvancement = getModAdvancement(user);

		if (world.isClient)	MinecraftClient.getInstance().setScreen(advancementsScreen);
		Screen modScreen = MinecraftClient.getInstance().currentScreen;

		if (modScreen instanceof AdvancementsScreen) {
			((AdvancementsScreen) modScreen).selectTab(modAdvancement);
		}
	}

	private Advancement getModAdvancement(PlayerEntity player) {
		Identifier id = new Identifier("ruinsofarcana:lightbow");
		if (player instanceof ServerPlayerEntity) {
			return Objects.requireNonNull(player.getServer()).getAdvancementLoader().get(id);
		}
		else return null;
	}

}

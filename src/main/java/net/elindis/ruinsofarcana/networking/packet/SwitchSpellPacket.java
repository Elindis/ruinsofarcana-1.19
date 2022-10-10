package net.elindis.ruinsofarcana.networking.packet;

import net.elindis.ruinsofarcana.item.ModItems;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SwitchSpellPacket {
	public static void receive(MinecraftServer server, ServerPlayerEntity playerEntity,
							   ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender reponseSender) {

		// Make sure the player is holding the item
		ItemStack stack = playerEntity.getMainHandStack();
		if (!stack.isOf(ModItems.ARCANISTS_STAFF)) stack = playerEntity.getOffHandStack();
		if (!stack.isOf(ModItems.ARCANISTS_STAFF)) return;

		assert stack.getNbt() != null;
		int spellSelected = stack.getNbt().getInt("spell_selected");
		if (spellSelected >= 2) {
			spellSelected = 0;
		} else {
			spellSelected++;
		}
		stack.getNbt().putInt("spell_selected", spellSelected);
		NbtList nbtList = stack.getNbt().getList("spell_list", 8);

		if (spellSelected == 0) {
			playerEntity.sendMessage(Text.literal(nbtList.getString(0)).formatted(Formatting.AQUA)
					.append(Text.literal(" - ").formatted(Formatting.GRAY))
					.append(Text.literal(nbtList.getString(1)).formatted(Formatting.GRAY))
					.append(Text.literal(" - ").formatted(Formatting.GRAY))
					.append(Text.literal(nbtList.getString(2)).formatted(Formatting.GRAY)), true);
		}
		if (spellSelected == 1) {
			playerEntity.sendMessage(Text.literal(nbtList.getString(0)).formatted(Formatting.GRAY)
					.append(Text.literal(" - ").formatted(Formatting.GRAY))
					.append(Text.literal(nbtList.getString(1)).formatted(Formatting.AQUA))
					.append(Text.literal(" - ").formatted(Formatting.GRAY))
					.append(Text.literal(nbtList.getString(2)).formatted(Formatting.GRAY)), true);
		}
		if (spellSelected == 2) {
			playerEntity.sendMessage(Text.literal(nbtList.getString(0)).formatted(Formatting.GRAY)
					.append(Text.literal(" - ").formatted(Formatting.GRAY))
					.append(Text.literal(nbtList.getString(1)).formatted(Formatting.GRAY))
					.append(Text.literal(" - ").formatted(Formatting.GRAY))
					.append(Text.literal(nbtList.getString(2)).formatted(Formatting.AQUA)), true);
		}



	}
}

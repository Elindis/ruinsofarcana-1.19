package net.elindis.ruinsofarcana.networking;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.elindis.ruinsofarcana.networking.packet.WhirlwindStartPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ModPackets {

	public static final Identifier WHIRLWIND_ID = new Identifier(RuinsOfArcana.MOD_ID, "whirlwind");

	public static void registerC2SPackets() {

	}
	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(WHIRLWIND_ID, WhirlwindStartPacket::receive);
	}

}


package net.elindis.ruinsofarcana.networking;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.elindis.ruinsofarcana.networking.packet.SnowstormStartPacket;
import net.elindis.ruinsofarcana.networking.packet.SwitchSpellPacket;
import net.elindis.ruinsofarcana.networking.packet.WhirlwindStartPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModPackets {

	public static final Identifier WHIRLWIND_ID = new Identifier(RuinsOfArcana.MOD_ID, "whirlwind");
	public static final Identifier SNOWSTORM_ID = new Identifier(RuinsOfArcana.MOD_ID, "snowstorm");
	public static final Identifier SWITCH_SPELL_ID = new Identifier(RuinsOfArcana.MOD_ID, "switch_spell");

	public static void registerC2SPackets() {
		ServerPlayNetworking.registerGlobalReceiver(SWITCH_SPELL_ID, SwitchSpellPacket::receive);
	}
	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(WHIRLWIND_ID, WhirlwindStartPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(SNOWSTORM_ID, SnowstormStartPacket::receive);

	}

}


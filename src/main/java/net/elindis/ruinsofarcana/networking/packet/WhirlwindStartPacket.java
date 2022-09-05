package net.elindis.ruinsofarcana.networking.packet;

import net.elindis.ruinsofarcana.sound.WhirlwindSound;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.PacketByteBuf;

public class WhirlwindStartPacket {
	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
							   PacketByteBuf buf, PacketSender reponseSender) {
		client.getSoundManager().play(new WhirlwindSound(SoundInstance.createRandom(), client.player) {
		});
	}
}

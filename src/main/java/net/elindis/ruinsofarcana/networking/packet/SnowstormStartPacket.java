package net.elindis.ruinsofarcana.networking.packet;

import net.elindis.ruinsofarcana.sound.SnowstormSound;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.PacketByteBuf;

public class SnowstormStartPacket {
	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
							   PacketByteBuf buf, PacketSender reponseSender) {
		System.out.println("snowstorm received");
		client.getSoundManager().play(new SnowstormSound(SoundInstance.createRandom(), client.player) {
		});
	}
}

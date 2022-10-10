package net.elindis.ruinsofarcana.event;

import net.elindis.ruinsofarcana.networking.ModPackets;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

	public static final String KEY_CATEGORY_ARCANA = "key.category.ruinsofarcana.arcana";
	public static final String KEY_SWITCH_SPELL = "key.ruinsofarcana.switch_spell";

	public static KeyBinding switchSpellKey;

	public static void registerKeyInputs() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (switchSpellKey.wasPressed()) {
				// Do the thing
				assert client.player != null;
				ClientPlayNetworking.send(ModPackets.SWITCH_SPELL_ID, PacketByteBufs.create());
			}
		});
	}

	public static void register() {
		switchSpellKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				KEY_SWITCH_SPELL,
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_V,
				KEY_CATEGORY_ARCANA
		));
		registerKeyInputs();
	}
}

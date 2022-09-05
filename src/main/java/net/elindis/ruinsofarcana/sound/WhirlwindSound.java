package net.elindis.ruinsofarcana.sound;

import net.elindis.ruinsofarcana.effect.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;

@Environment(value= EnvType.CLIENT)
public class WhirlwindSound extends MovingSoundInstance {

	private ClientPlayerEntity player;

	public WhirlwindSound(Random random, ClientPlayerEntity player) {
		super(ModSounds.WHIRLWIND, SoundCategory.AMBIENT, random);
		this.repeat = true;
		this.repeatDelay = 0;
		this.player = player;
		this.pitch = 1f;
	}

	@Override
	public void tick() {

		if (this.player == null) return;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
		if (!player.hasStatusEffect(ModEffects.REPEL)) {
			this.setDone();
		}

	}

	@Override
	public boolean shouldAlwaysPlay() {
		return super.shouldAlwaysPlay();
	}

	public void setPlayer(ClientPlayerEntity player) {
		this.player = player;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	@Override
	public boolean canPlay() {
		return super.canPlay();
	}
}

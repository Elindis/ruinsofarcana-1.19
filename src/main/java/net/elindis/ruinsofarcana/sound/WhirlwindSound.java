package net.elindis.ruinsofarcana.sound;

import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;

public class WhirlwindSound extends MovingSoundInstance {

	private PlayerEntity player;

	public WhirlwindSound(SoundEvent soundEvent, SoundCategory soundCategory, Random random, PlayerEntity player) {
		super(ModSounds.WHIRLWIND, SoundCategory.AMBIENT, random);
		soundEvent = ModSounds.WHIRLWIND;
		soundCategory = SoundCategory.AMBIENT;
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

	}

	@Override
	public boolean shouldAlwaysPlay() {
		return super.shouldAlwaysPlay();
	}

	public void setPlayer(PlayerEntity player) {
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

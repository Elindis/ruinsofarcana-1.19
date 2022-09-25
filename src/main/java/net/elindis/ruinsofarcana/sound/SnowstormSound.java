package net.elindis.ruinsofarcana.sound;

import net.elindis.ruinsofarcana.effect.ModEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.random.Random;

@Environment(value= EnvType.CLIENT)
public class SnowstormSound extends MovingSoundInstance {

	private LivingEntity entity;

	public SnowstormSound(Random random, LivingEntity entity) {
		super(ModSounds.SNOWSTORM, SoundCategory.AMBIENT, random);
		this.repeat = true;
		this.repeatDelay = 0;
		this.entity = entity;
		this.pitch = 1f;
	}

	@Override
	public void tick() {

		if (this.entity == null) return;
		this.x = entity.getX();
		this.y = entity.getY();
		this.z = entity.getZ();
		if (!entity.hasStatusEffect(ModEffects.SNOWSTORM)) {
			this.setDone();
		}

	}

	@Override
	public boolean shouldAlwaysPlay() {
		return super.shouldAlwaysPlay();
	}

	public void setEntity(ClientPlayerEntity entity) {
		this.entity = entity;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	@Override
	public boolean canPlay() {
		return super.canPlay();
	}
}

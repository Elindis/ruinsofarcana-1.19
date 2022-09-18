package net.elindis.ruinsofarcana.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class FireTornadoParticle extends SpriteBillboardParticle {
	private final double initialX;
	private final double initialY;
	private final double initialZ;
	private final int randomOf4;
	private final float size;

	protected FireTornadoParticle(ClientWorld level, double xPos, double yPos, double zPos,
								  SpriteProvider spriteSet, double dx, double dy, double dz) {
		super(level, xPos, yPos, zPos, dx, dy, dz);

		this.velocityMultiplier = 0.6F;
		this.x = xPos;
		this.y = yPos+1.5f+Random.create().nextFloat()/2;
		this.z = zPos;
		this.initialX = xPos;
		this.initialY = yPos+1.5f+Random.create().nextFloat()/2;
		this.initialZ = zPos;
		this.velocityX = dx;
		this.velocityY = dy;
		this.velocityZ = dz;
		this.scale *= 3.4F;
		this.maxAge = 20;
		this.randomOf4 = Random.create().nextBetween(0, 3);
		this.size = 6f;

		this.red = 1f;
		this.green = 1f;
		this.blue = 1f;
	}

	@Override
	public void tick() {
		super.tick();
//		fadeIn();
		if (Random.create().nextBetween(0,10) == 1) {
			this.world.addParticle(ParticleTypes.LAVA, this.x, this.y, this.z, Random.create().nextFloat()/10,
					Random.create().nextFloat()/10, Random.create().nextFloat()/10);
			this.world.playSound(this.x, this.y, this.z, SoundEvents.BLOCK_FIRE_AMBIENT,
					SoundCategory.NEUTRAL, 0.8f, 0.8f+Random.create().nextFloat()/3, true);
			this.world.playSound(this.x, this.y, this.z, SoundEvents.BLOCK_CAMPFIRE_CRACKLE,
					SoundCategory.NEUTRAL, 0.8f, 0.8f+Random.create().nextFloat()/3, true);
		}

//		this.world.addParticle(ParticleTypes.FLAME, this.x, this.y, this.z, Random.create().nextFloat()/10,
//				Random.create().nextFloat()/10, Random.create().nextFloat()/10);
		float time = this.age;
		float size = 2.4f;
		if (randomOf4 == 0) {
			this.x = initialX + -MathHelper.cos(time/2)*size;
			this.z = initialZ + -MathHelper.sin(time/2)*size;
		}
		if (randomOf4 == 1) {
			this.x = initialX + -MathHelper.sin(-time/2)*size;
			this.z = initialZ + -MathHelper.cos(-time/2)*size;
		}
		if (randomOf4 == 2) {
			this.x = initialX + MathHelper.cos(time/2)*size;
			this.z = initialZ + MathHelper.sin(time/2)*size;
		}
		if (randomOf4 == 3) {
			this.x = initialX + MathHelper.sin(-time/2)*size;
			this.z = initialZ + MathHelper.cos(-time/2)*size;
		}
		if (randomOf4 % 2 == 0) {
			this.y = initialY + (age/(float)maxAge)/20;
		} else {
			this.y = initialY + -(age/(float)maxAge)/20;
		}

	}
	@Override
	public float getSize(float tickDelta) {
		float f = ((float)this.age) / (float)this.maxAge;
		f = 1.0f - f;
		f *= f;
		f = 1.0f - f;
		return this.scale * f;
	}

	private void fadeIn() {
		if (age < 2) {
			this.alpha = MathHelper.clamp((age-2f), 0, maxAge)/(float) maxAge;
		}
		else this.alpha = 1;
	}


	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory
	implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider sprites;

		public Factory(SpriteProvider spriteSet) {
			this.sprites = spriteSet;
		}

		@Override
		public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
									   double dx, double dy, double dz) {
			FireTornadoParticle tornadoParticle = new FireTornadoParticle(level, x, y, z, this.sprites, dx, dy, dz);
			tornadoParticle.setSprite(this.sprites);
			return tornadoParticle;
		}
	}
}
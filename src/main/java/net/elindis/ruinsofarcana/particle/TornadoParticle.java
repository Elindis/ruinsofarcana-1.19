package net.elindis.ruinsofarcana.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class TornadoParticle extends SpriteBillboardParticle {
	private final double initialX;
	private final double initialY;
	private final double initialZ;
	private final int randomOf4;
	private final float size;

	protected TornadoParticle(ClientWorld level, double xPos, double yPos, double zPos,
							  SpriteProvider spriteSet, double dx, double dy, double dz) {
		super(level, xPos, yPos, zPos, dx, dy, dz);

		this.velocityMultiplier = 0.6F;
		this.x = xPos;
		this.y = yPos;
		this.z = zPos;
		this.initialX = xPos;
		this.initialY = yPos;
		this.initialZ = zPos;
		this.velocityX = dx;
		this.velocityY = dy;
		this.velocityZ = dz;
		this.scale *= 0.9F;
		this.maxAge = 20;
		this.randomOf4 = Random.create().nextBetween(0, 3);
		this.size = 3f;

		this.red = 0.8f;
		this.green = 0.75f;
		this.blue = 0.7f;
	}

	@Override
	public void tick() {
		super.tick();
		fadeIn();
		float time = this.age;
		float size = 2;
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
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
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
			TornadoParticle tornadoParticle = new TornadoParticle(level, x, y, z, this.sprites, dx, dy, dz);
			tornadoParticle.setSprite(this.sprites);
			tornadoParticle.setAlpha(0.9f);
			return tornadoParticle;
		}
	}
}
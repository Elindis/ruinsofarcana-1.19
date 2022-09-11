package net.elindis.ruinsofarcana.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

public class SpiralParticle extends SpriteBillboardParticle {
	private final double initialX;
	private final double initialY;
	private final double initialZ;
	private final int randomOf4;
	private final float size;

	protected SpiralParticle(ClientWorld level, double xPos, double yPos, double zPos,
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
		this.scale *= 0.7F;
		this.maxAge = 60;
		this.setSpriteForAge(spriteSet);
		this.randomOf4 = Random.create().nextBetween(0, 3);
		this.size = 4f;

		this.red = 1f;
		this.green = 1f;
		this.blue = 1f;
	}

	@Override
	public void tick() {
		super.tick();
		fadeIn();
//		Vec3d particlePos = new Vec3d(this.x, this.y, this.z);
//		Vec3d initialPos = new Vec3d(initialX, initialY, initialZ);
//		float factor = 1f;
//		this.red = MathHelper.clamp(((float) particlePos.distanceTo(initialPos)*factor), 0, 1);
//		this.blue = MathHelper.clamp(((float) particlePos.distanceTo(initialPos)*factor), 0, 1);
//		this.green = MathHelper.clamp(((float) particlePos.distanceTo(initialPos)*factor), 0, 0.8f);
		float time = this.age/4f;
		if (randomOf4 == 0) {
			this.x = initialX + -MathHelper.cos(time/2)*size/time;
			this.z = initialZ + -MathHelper.sin(time/2)*size/time;
		}
		if (randomOf4 == 1) {
			this.x = initialX + -MathHelper.sin(-time/2)*size/time;
			this.z = initialZ + -MathHelper.cos(-time/2)*size/time;
		}
		if (randomOf4 == 2) {
			this.x = initialX + MathHelper.cos(time/2)*size/time;
			this.z = initialZ + MathHelper.sin(time/2)*size/time;
		}
		if (randomOf4 == 3) {
			this.x = initialX + MathHelper.sin(-time/2)*size/time;
			this.z = initialZ + MathHelper.cos(-time/2)*size/time;
		}
		if (randomOf4 % 2 == 0) {
			this.y = initialY + (age/(float)maxAge)/20;
		} else {
			this.y = initialY + -(age/(float)maxAge)/20;
		}

	}
	@Override
	public float getSize(float tickDelta) {
		float f = ((float)this.age + tickDelta) / (float)this.maxAge;
		f = 1.0f - f;
		f *= f;
		f = 1.0f - f;
		return this.scale * f;
	}

	private void fadeIn() {
		if (age < 10) {
			this.alpha = MathHelper.clamp((age-5f) * 2, 0, maxAge)/(float) maxAge;
		}
		else this.alpha = 1;
	}


	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider sprites;

		public Factory(SpriteProvider spriteSet) {
			this.sprites = spriteSet;
		}

		public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
									   double dx, double dy, double dz) {
			return new SpiralParticle(level, x, y, z, this.sprites, dx, dy, dz);
		}
	}
}
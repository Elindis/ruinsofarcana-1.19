package net.elindis.ruinsofarcana.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.random.Random;

public class JetParticle extends SpriteBillboardParticle {
	private final double initialX;
	private final double initialY;
	private final double initialZ;
	private final boolean random;
	protected JetParticle(ClientWorld level, double xPos, double yPos, double zPos,
							 SpriteProvider spriteSet, double dx, double dy, double dz) {
		super(level, xPos, yPos, zPos, dx, dy, dz);

		this.velocityMultiplier = 0.6F;
		this.x = xPos;
		this.y = yPos;
		this.z = zPos;
		this.initialX = xPos;
		this.initialY = yPos;
		this.initialZ = zPos;
		this.random = Random.create().nextBoolean();
		this.scale *= 0.5F;
		this.maxAge = 30;
		this.setSpriteForAge(spriteSet);

		this.red = 1f;
		this.green = 1f;
		this.blue = 1f;
	}

	@Override
	public void tick() {
		super.tick();
		fadeOut();
//		Vec3d particlePos = new Vec3d(this.x, this.y, this.z);
//		Vec3d initialPos = new Vec3d(initialX, initialY, initialZ);
//		float factor = 1f;
//		this.red = (float) particlePos.distanceTo(initialPos)*factor;
//		this.blue = (float) particlePos.distanceTo(initialPos)*factor;
//		this.green = (float) particlePos.distanceTo(initialPos)*factor;
		this.x = initialX;
		if (random) {
			this.y = initialY+(age*2/((float)maxAge));
		} else {
			this.y = initialY+(-age*2/((float)maxAge));
		}
		this.z = initialZ;
	}
	@Override
	public float getSize(float tickDelta) {
		float f = ((float)this.age + tickDelta) / (float)this.maxAge;
		f = 1.0f - f;
		f *= f;
		f = 1.0f - f;
		return this.scale * f;
	}

	private void fadeOut() {
		this.alpha = (-(1/(float)maxAge) * age + 1);
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
			return new JetParticle(level, x, y, z, this.sprites, dx, dy, dz);
		}
	}
}
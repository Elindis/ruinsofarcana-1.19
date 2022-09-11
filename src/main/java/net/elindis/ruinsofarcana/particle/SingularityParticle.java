package net.elindis.ruinsofarcana.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class SingularityParticle extends SpriteBillboardParticle {
	private final double startY;
	private final double startX;
	private final double startZ;
	private final double blockPosX;
	private final double blockPosY;
	private final double blockPosZ;


	protected SingularityParticle(ClientWorld clientWorld, double particlePosX, double particlePosY, double particlePosZ,
								  double blockX, double blockY, double blockZ) {
//		super(clientWorld, xPos, yPos, zPos, xd, yd, zd);

//		this.velocityMultiplier = 0.6F;
//		this.x = xd;
//		this.y = yd;
//		this.z = zd;
//		this.scale *= 0.25F;
//		this.maxAge = 60;
//		this.startX = this.x;
//		this.startY = this.y;
//		this.startZ = this.z;
//		this.setSpriteForAge(spriteSet);
//
//		this.red = 1f;
//		this.green = 1f;
//		this.blue = 1f;

		super(clientWorld, particlePosX, particlePosY, particlePosZ);
		this.velocityX = (Random.create().nextFloat()-0.5)/4;
		this.velocityY = (Random.create().nextFloat()-0.5)/4;
		this.velocityZ = (Random.create().nextFloat()-0.5)/4;
		this.blockPosX = blockX;
		this.blockPosY = blockY;
		this.blockPosZ = blockZ;
		this.x = particlePosX;
		this.y = particlePosY;
		this.z = particlePosZ;
		this.startX = this.x;
		this.startY = this.y;
		this.startZ = this.z;
		this.scale = 0.1f * (this.random.nextFloat() * 0.2f + 0.4f);
		float j = this.random.nextFloat() * 0.6f + 0.4f;
		this.red = j * 0.9f;
		this.green = j * 0.3f;
		this.blue = j;
		this.maxAge = (int)(Math.random() * 10.0) + 90;
		this.gravityStrength = -0.1f;

	}
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void move(double dx, double dy, double dz) {
		this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
		this.repositionFromBoundingBox();
	}

//	@Override
//	public float getSize(float tickDelta) {
//		float f = ((float)this.age + tickDelta) / (float)this.maxAge;
//		f = 1.0f - f;
//		f *= f;
//		f = 1.0f - f;
//		return this.scale * f;
//	}

	@Override
	public int getBrightness(float tint) {
		int i = super.getBrightness(tint);
		float f = (float)this.age / (float)this.maxAge;
		f *= f;
		f *= f;
		int j = i & 0xFF;
		int k = i >> 16 & 0xFF;
		if ((k += (int)(f * 15.0f * 16.0f)) > 240) {
			k = 240;
		}
		return j | k << 16;
	}

	@Override
	public void tick() {
//		float f;
//		this.alpha = (-(1/(float) maxAge)*age+1);
//		this.prevPosX = this.x;
//		this.prevPosY = this.y;
//		this.prevPosZ = this.z;
//		if (this.age++ >= this.maxAge) {
//			this.markDead();
//			return;
//		}
//		float g = f = (float)this.age / (float)this.maxAge;
//		f = -f + f * f * 2.0f;
//		f = 1.0f - f;
//		this.x = this.startX+0.2 + this.velocityX/1.5 * (double)f;
//		this.y = this.startY-0.1 + this.velocityY/2 * (double)f + (double)(1.0f - g);
//		this.z = this.startZ+0.2 + this.velocityZ/1.5 * (double)f;
		super.tick();

//		Vec3d particlePos = new Vec3d(this.x, this.y, this.z);
//		Vec3d initialPos = new Vec3d(blockPosX, blockPosY, blockPosZ);
//		float factor = 1f;
//		this.red = (float) particlePos.distanceTo(initialPos)*factor;
//		this.blue = (float) particlePos.distanceTo(initialPos)*factor;
//		this.green = (float) particlePos.distanceTo(initialPos)*factor;
		Vec3d particleVector = new Vec3d(this.x, this.y, this.z);
		Vec3d blockVector = new Vec3d(blockPosX, blockPosY, blockPosZ);
		double distance = particleVector.distanceTo(blockVector);
		float gravity = 0.025f*2;
		this.velocityX += (blockPosX - particleVector.x) * gravity/distance;
		this.velocityY += (blockPosY - particleVector.y) * gravity/distance;
		this.velocityZ += (blockPosZ - particleVector.z) * gravity/distance;

	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider sprites;

		public Factory(SpriteProvider spriteSet) {
			this.sprites = spriteSet;
		}

		public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
									   double dx, double dy, double dz) {
//			return new SingularityParticle(level, x, y, z, this.sprites, dx, dy, dz);
			SingularityParticle singularityParticle = new SingularityParticle(level, x, y, z, dx, dy, dz);
			singularityParticle.setSprite(this.sprites);
			return singularityParticle;
		}
	}
}

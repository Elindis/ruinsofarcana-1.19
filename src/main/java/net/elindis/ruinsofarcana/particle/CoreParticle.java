package net.elindis.ruinsofarcana.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class CoreParticle extends SpriteBillboardParticle {
	private final double blockPosX;
	private final double blockPosY;
	private final double blockPosZ;


	protected CoreParticle(ClientWorld clientWorld, double particlePosX, double particlePosY, double particlePosZ,
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
		this.velocityX = 0;
		this.velocityY = 0;
		this.velocityZ = 0;
		this.blockPosX = blockX;
		this.blockPosY = blockY;
		this.blockPosZ = blockZ;
		this.x = particlePosX;
		this.y = particlePosY;
		this.z = particlePosZ;
		this.scale = 0.33f;
		this.maxAge = 2;
//		this.gravityStrength = 0;

	}
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
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
		if (this.age++ >= maxAge) {
			this.markDead();
			return;
		}
		this.x = blockPosX;
		this.y = blockPosY;
		this.z = blockPosZ;
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
			CoreParticle singularityParticle = new CoreParticle(level, x, y, z, dx, dy, dz);
			singularityParticle.setSprite(this.sprites);
			return singularityParticle;
		}
	}
}

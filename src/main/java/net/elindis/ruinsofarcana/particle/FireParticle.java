package net.elindis.ruinsofarcana.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class FireParticle extends AbstractSlowingParticle {
	FireParticle(ClientWorld level, double xPos, double yPos, double zPos,
				 double dx, double dy, double dz) {
		super(level, xPos, yPos, zPos, dx, dy, dz);
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

		@Override
		public float getSize(float tickDelta) {
			float f = ((float)this.age + tickDelta) / (float)this.maxAge;
			return (this.scale * (1.0f - f * f * 0.5f)/3);
		}

		@Override
		public int getBrightness(float tint) {
			float f = ((float)this.age + tint) / (float)this.maxAge;
			f = MathHelper.clamp(f, 0.0f, 1.0f);
			int i = super.getBrightness(tint);
			int j = i & 0xFF;
			int k = i >> 16 & 0xFF;
			if ((j += (int)(f * 15.0f * 16.0f)) > 240) {
				j = 240;
			}
			return j | k << 16;
		}

		@Environment(value=EnvType.CLIENT)
		public static class SmallFactory
				implements ParticleFactory<DefaultParticleType> {
			private final SpriteProvider spriteProvider;

			public SmallFactory(SpriteProvider spriteProvider) {
				this.spriteProvider = spriteProvider;
			}

			@Override
			public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
				FireParticle flameParticle = new FireParticle(clientWorld, d, e, f, g, h, i);
				flameParticle.setSprite(this.spriteProvider);
				flameParticle.scale(0.5f);
				return flameParticle;
			}
		}

		@Environment(value=EnvType.CLIENT)
		public static class Factory
				implements ParticleFactory<DefaultParticleType> {
			private final SpriteProvider spriteProvider;

			public Factory(SpriteProvider spriteProvider) {
				this.spriteProvider = spriteProvider;
			}

			@Override
			public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
				FireParticle flameParticle = new FireParticle(clientWorld, d, e, f, g, h, i);
				flameParticle.setSprite(this.spriteProvider);
				return flameParticle;
			}
		}
}
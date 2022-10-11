package net.elindis.ruinsofarcana.spell;

import net.elindis.ruinsofarcana.block.ModBlocks;
import net.elindis.ruinsofarcana.effect.ModEffects;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class FreezeSpell extends Spell {

	public static String getName() { return "Freeze"; }

	@Override
	public int getExpCost() {
		return 5;
	}

	@Override
	public ParticleEffect getParticleType() {
		return ParticleTypes.SNOWFLAKE;
	}

	@Override
	public boolean isRaycaster() {
		return true;
	}

	@Override
	public float getReadyPercentage() {
		return 0.3f;
	}

	@Override
	public int getRange() {
		return 16;
	}

	@Override
	public void doSpellEffect(World world, LivingEntity user, float f) {
		if (user instanceof PlayerEntity playerEntity) {

			int maxDistance = getRange();
			Vec3d vec3d = user.getEyePos();
			Vec3d vec3d2;
			Vec3d vec3d3;
			Box box;
			Predicate<Entity> predicate;
			int i;
			EntityHitResult entityHitResult = ProjectileUtil.raycast(
					user, vec3d, vec3d3 = vec3d.add(vec3d2 = user.getRotationVec(1.0f).multiply(maxDistance)),
					box = user.getBoundingBox().stretch(vec3d2).expand(1.0),
					predicate = entity -> !entity.isSpectator() && entity.canHit(),
					i = maxDistance * maxDistance);

			if (entityHitResult == null) {
				// We play a sad magic sound.
				playFailureSound(world, playerEntity);
				return;
			}
			// On spell success:
			if (entityHitResult.getType().equals(HitResult.Type.ENTITY)) {
				Entity target = entityHitResult.getEntity();
				if (target instanceof LivingEntity) {
					((PlayerEntity) user).addExperience(-getExpCost());
					playSuccessSound(world, playerEntity);
					spellEffect((LivingEntity) target);


				} else {
					playFailureSound(world, playerEntity);
				}
			}
		}
	}

	@Override
	public void spellEffect(LivingEntity target) {
		target.setPosition(Math.floor(target.getX())+0.5f,target.getY(),Math.floor(target.getZ())+0.5f);
		// Freezes the target in a block of ice for 5 minutes
		target.addStatusEffect(new StatusEffectInstance(ModEffects.FROZEN, 6000, 0, false, false, false));

//		BlockPos icePos = target.getBlockPos();
		Box targetBox = target.getBoundingBox();
		BlockPos.stream(targetBox).forEach(blockPos -> {
			target.world.setBlockState(blockPos, ModBlocks.THIN_ICE.getDefaultState());
		});
//		target.world.setBlockState(icePos, Blocks.ICE.getDefaultState());
//		target.world.setBlockState(icePos.up(), Blocks.ICE.getDefaultState());

		ModParticleUtil.doHealUndeadParticles(target, ParticleTypes.SNOWFLAKE, 18);
	}

	@Override
	protected void playFailureSound(World world, PlayerEntity playerEntity) {
		// Example
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
				SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS, 0.7F, 1.5F);
	}

	protected void playSuccessSound(World world, PlayerEntity playerEntity) {
		// Example
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
				SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1.2F, 0.9F +
						playerEntity.getRandom().nextFloat()/2);
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
				SoundEvents.BLOCK_CONDUIT_ACTIVATE, SoundCategory.PLAYERS, 1F, 0.9F +
						playerEntity.getRandom().nextFloat()/2);
	}


}

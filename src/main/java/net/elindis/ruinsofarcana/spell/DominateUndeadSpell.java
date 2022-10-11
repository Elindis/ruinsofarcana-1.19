package net.elindis.ruinsofarcana.spell;

import net.elindis.ruinsofarcana.effect.ModEffects;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class DominateUndeadSpell extends Spell {

	public static String getName() { return "Dominate Undead"; }

	@Override
	public int getExpCost() {
		return 20;
	}

	@Override
	public ParticleEffect getParticleType() {
		return ParticleTypes.SMOKE;
	}

	@Override
	public boolean isRaycaster() {
		return true;
	}

	@Override
	public float getReadyPercentage() {
		return 0.9f;
	}

	@Override
	public int getRange() {
		return 8;
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
					if (((LivingEntity) target).isUndead()) {
						((PlayerEntity) user).addExperience(-getExpCost());
						playSuccessSound(world, playerEntity);
						spellEffect((LivingEntity) target);
					}

				} else {
					playFailureSound(world, playerEntity);
				}
			}
		}
	}

	@Override
	public void spellEffect(LivingEntity target) {
		// Permanently enslaves the non-boss undead target.
		target.addStatusEffect(new StatusEffectInstance(ModEffects.CONFUSION, 60, 2, false, true, false));
		ModParticleUtil.doLivingEntityParticles(target, ParticleTypes.LARGE_SMOKE, 25);
		ModParticleUtil.doLivingEntityParticles(target, ParticleTypes.PORTAL, 20);

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
				SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1.2F, 0.7F +
						playerEntity.getRandom().nextFloat()/2);
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
				SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, SoundCategory.PLAYERS, 1F, 0.7F +
						playerEntity.getRandom().nextFloat()/2);
	}


}

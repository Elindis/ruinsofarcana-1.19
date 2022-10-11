package net.elindis.ruinsofarcana.spell;

import net.elindis.ruinsofarcana.effect.ModEffects;
import net.elindis.ruinsofarcana.util.ModParticleUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RaiseZombieSpell extends Spell {

	public static String getName() { return "Raise Zombie"; }

	@Override
	public int getExpCost() {
		return 10;
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
		return 0.75f;
	}

	@Override
	public int getRange() {
		return 6;
	}

	@Override
	public void spellEffect(LivingEntity user) {

		World world = user.getWorld();
		BlockHitResult hitResult = (BlockHitResult) user.raycast(getRange(), 1f, true);
		ZombieEntity zombieEntity = new ZombieEntity(EntityType.ZOMBIE, world);

		// This allows the zombie to be under our control for THREE MINUTES.
		zombieEntity.addStatusEffect(new StatusEffectInstance(ModEffects.CONFUSION, 3600, 1, false, true, false));

		BlockPos raycastPos = new BlockPos(hitResult.getPos());
		BlockState raycastBlock = world.getBlockState(raycastPos);

		// raycastPos.offset allows the zombie to avoid being spawned inside of walls most of the time.
		Vec3d zombiePos = Vec3d.ofCenter(raycastBlock.getCollisionShape(world, raycastPos).isEmpty() ? raycastPos : raycastPos.offset(hitResult.getSide()));

		zombieEntity.setPosition(zombiePos);
		world.spawnEntity(zombieEntity);
		ModParticleUtil.doLivingEntityParticles(zombieEntity, ParticleTypes.LARGE_SMOKE, 25);
		ModParticleUtil.doLivingEntityParticles(zombieEntity, ParticleTypes.PORTAL, 20);
	}

	@Override
	protected void playFailureSound(World world, PlayerEntity playerEntity) {
		// Example
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
				SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS, 0.7F, 1.5F);
	}

	@Override
	protected void playSuccessSound(World world, PlayerEntity playerEntity, float f) {
		// Example
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
				SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK, SoundCategory.PLAYERS, 0.8F, 1F /
						(world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
				SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 0.8F, 1F /
						(world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
	}


}

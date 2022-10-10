package net.elindis.ruinsofarcana.spell;

import net.elindis.ruinsofarcana.effect.ModEffects;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class InfernoSpell extends Spell {

	public static String getName() { return "Inferno"; }

	@Override
	public int getExpCost() {
		return 5;
	}

	@Override
	public ParticleEffect getParticleType() {
		return ParticleTypes.LAVA;
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
		return 32;
	}

	@Override
	public void spellEffect(LivingEntity user) {
		System.out.println("Casted INFERNO");
		World world = user.getWorld();

		HitResult hitResult = user.raycast(getRange(), 1f, true);

		ArmorStandEntity firestormEntity = new ArmorStandEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);

		firestormEntity.setInvisible(true);
		firestormEntity.setInvulnerable(true);
		firestormEntity.setNoGravity(true);
		firestormEntity.setBoundingBox(new Box(0,0,0,0,0,0));
		firestormEntity.addStatusEffect(new StatusEffectInstance(ModEffects.FIRESTORM,
				600, 0, false, false, false));

		BlockPos.iterateOutwards(firestormEntity.getBlockPos(), 2, 1, 2).forEach(blockPos1 -> {
			if (Random.create().nextBetween(0,2) == 0) {
				if (world.getBlockState(blockPos1.up()).isOf(Blocks.AIR) || world.getBlockState(blockPos1.up()).isOf(Blocks.SNOW)) {
					world.setBlockState(blockPos1.up(), Blocks.FIRE.getDefaultState());
				}
			}
		});

		world.spawnEntity(firestormEntity);
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
				SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.PLAYERS, 0.8F, 1F /
						(world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
				SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 0.8F, 1F /
						(world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
	}


}

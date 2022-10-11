package net.elindis.ruinsofarcana.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class Spell {

	public int getExpCost() {
		return 0;
	}

	public ParticleEffect getParticleType() {
		return ParticleTypes.SMOKE;
	}

	public boolean isRaycaster() {
		return false;
	}

	public float getReadyPercentage() {
		return 0.5f;
	}

	public int getRange() {
		return 15;
	}

	public void doSpellEffect(World world, LivingEntity user, float f) {
		if (user instanceof PlayerEntity playerEntity) {

			// On spell success:
			if (playerEntity.totalExperience > getExpCost() || playerEntity.getAbilities().creativeMode) {
				playerEntity.addExperience(-getExpCost());
				playSuccessSound(world, (PlayerEntity) user, f);
				spellEffect(user);
			}
			// On spell failure:
			else {
				// We play a sad magic sound.
				playFailureSound(world, playerEntity);
				user.sendMessage(Text.of("Spell FAILED"));
			}
		}
	}

	public void spellEffect(LivingEntity user) {}

	protected void playFailureSound(World world, PlayerEntity playerEntity) {
		// Example
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
				SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS, 0.7F, 1.5F);
	}

	protected void playSuccessSound(World world, PlayerEntity playerEntity, float f) {
		// Example
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
				SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.PLAYERS, 0.8F, 1F /
						(world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
				SoundEvents.BLOCK_SMALL_AMETHYST_BUD_BREAK, SoundCategory.PLAYERS, 0.8F, 1F /
						(world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
	}

	public Spell getSpell() {
		return this;
	}


}

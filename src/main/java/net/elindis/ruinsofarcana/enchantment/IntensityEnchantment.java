package net.elindis.ruinsofarcana.enchantment;

import net.elindis.ruinsofarcana.item.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.server.world.ServerWorld;

public class IntensityEnchantment extends Enchantment {
	protected IntensityEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	public void onTargetDamaged(LivingEntity user, Entity target, int level) {
		if (user.world.isClient) return;
		if (target instanceof LivingEntity && user instanceof PlayerEntity) {
			target.damage(DamageSource.player((PlayerEntity)user), level*5f);
		}
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		// This should probably be wands-only.
		return stack.isOf(ModItems.LIGHT_BOW) || stack.isOf(ModItems.WAND_OF_FROST_BOLT);
	}


}

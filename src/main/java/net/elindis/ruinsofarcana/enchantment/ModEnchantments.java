package net.elindis.ruinsofarcana.enchantment;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEnchantments {

	public static Enchantment WISDOM = register("wisdom",
			new WisdomEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
	public static Enchantment INTENSITY = register("intensity",
			new IntensityEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));

	private static Enchantment register(String name, Enchantment enchantment) {
		return Registry.register(Registry.ENCHANTMENT, new Identifier(RuinsOfArcana.MOD_ID, name), enchantment);
	}

	public static void registerModEnchantments() {
		System.out.println("Registering enchantments for "+ RuinsOfArcana.MOD_ID);
	}
}

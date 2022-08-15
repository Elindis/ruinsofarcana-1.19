package net.elindis.ruinsofarcana;

//import net.elindis.ruinsofarcana.block.ModBlocks;
//import net.elindis.ruinsofarcana.block.entity.ModBlockEntities;
//import net.elindis.ruinsofarcana.effect.ModEffects;
//import net.elindis.ruinsofarcana.enchantments.ModEnchantments;
//import net.elindis.ruinsofarcana.entity.ModEntities;
//import net.elindis.ruinsofarcana.item.ModItems;
//import net.elindis.ruinsofarcana.potion.ModPotions;
//import net.elindis.ruinsofarcana.recipe.ModRecipes;
//import net.elindis.ruinsofarcana.screen.ModScreenHandlers;
//import net.elindis.ruinsofarcana.sound.ModSounds;
//import net.elindis.ruinsofarcana.util.ModLootTableModifiers;
//import net.elindis.ruinsofarcana.util.ModRegistries;
//import net.elindis.ruinsofarcana.world.dimension.ModDimensions;
//import net.elindis.ruinsofarcana.world.feature.ModConfiguredFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuinsOfArcana implements ModInitializer {

	public static final String MOD_ID = "ruinsofarcana";
	public static final Logger LOGGER = LoggerFactory.getLogger("ruinsofarcana");
	public static final Identifier PacketID = new Identifier(MOD_ID, "spawn_packet");

	@Override
	public void onInitialize() {
		// to-do
		// - Magical light-source (enchanted gem/arcanite lantern. special effects?)
		// - Ore multiplication via machine
		// - blueprints for certain vanilla recipes (lanterns, concrete, w/e)
		// - New critter
		// - New enemy
		// - New tree
		// - The structures, of course. They should have different Blueprint loot depending on the structure,
		// and the structure should be different in different biomes. That way players can explore a specific biome
		// to find a specific blueprint.

		// "Our data shows that this world originated from nothing. If that is so, then when this world one day
		// returns to nothing, will a new world be born? Does this fact imply the existence of a cycle of rebirth?"

//		ModConfiguredFeatures.registerConfiguredFeatures();
//		ModItems.registerModItems();
//		registerModFuel();
//
//		ModBlocks.registerModBlocks();
//
//		ModBlockEntities.registerAllBlockEntities();
//		ModRecipes.registerRecipes();
//		ModScreenHandlers.registerAllScreenHandlers();
//
//		ModEnchantments.registerModEnchantments();
//
//		ModDimensions.register();
//
//		ModLootTableModifiers.modifyLootTables();
//		ModEffects.registerEffects();
//		ModPotions.registerPotions();
//		ModRegistries.registerMisc();
//		ModSounds.registerSounds();
//		ModEntities.register();
	}

	// TODO: MOVE THIS
//	public static void registerModFuel() {
//		System.out.printf("Now registering fuels for " + RuinsOfArcana.MOD_ID);
//		FuelRegistry registry = FuelRegistry.INSTANCE;
//		registry.add(ModItems.PERADITE, 16000);
//	}
}

package net.elindis.ruinsofarcana.world.dimension;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {
	public static final RegistryKey<World> SANCTUARY = RegistryKey.of(Registry.WORLD_KEY,
			new Identifier(RuinsOfArcana.MOD_ID, "sanctuary"));
	public static final RegistryKey<DimensionType> SANCTUARY_TYPE = RegistryKey.of(Registry.DIMENSION_TYPE_KEY,
			SANCTUARY.getValue());


	public static void register() {
		RuinsOfArcana.LOGGER.debug("Registering ModDimensions for " + RuinsOfArcana.MOD_ID);
	}
}
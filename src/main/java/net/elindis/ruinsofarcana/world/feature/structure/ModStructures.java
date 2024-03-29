package net.elindis.ruinsofarcana.world.feature.structure;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.structure.StructureType;

public class ModStructures {

	public static StructureType<SkyStructures> SKY_STRUCTURES;

	/**
	 * Registers the structure itself and sets what its path is. In this case, the
	 * structure will have the Identifier of structure_tutorial:sky_structures.
	 *
	 * It is always a good idea to register your Structures so that other mods and datapacks can
	 * use them too directly from the registries. It's great for mod/datapacks compatibility.
	 */
	public static void registerStructureFeatures() {
		SKY_STRUCTURES = Registry.register(Registry.STRUCTURE_TYPE, new Identifier(RuinsOfArcana.MOD_ID, "sky_structures"), () -> SkyStructures.CODEC);
	}
}
package net.elindis.ruinsofarcana.util;

import net.elindis.ruinsofarcana.block.ModBlocks;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;

public class ModRegistries {


    public static void registerMisc() {
        registerFlammableBlocks();
        registerStrippables();
    }


    public static void registerFlammableBlocks() {
        FlammableBlockRegistry instance = FlammableBlockRegistry.getDefaultInstance();
        instance.add(ModBlocks.MYRTLE_LOG, 5, 5);
        instance.add(ModBlocks.MYRTLE_WOOD, 5, 5);
        instance.add(ModBlocks.STRIPPED_MYRTLE_LOG, 5, 5);
        instance.add(ModBlocks.STRIPPED_MYRTLE_WOOD, 5, 5);
        instance.add(ModBlocks.MYRTLE_PLANKS, 5, 20);
        instance.add(ModBlocks.MYRTLE_LEAVES, 30, 60);
    }
    public static void registerStrippables() {
        StrippableBlockRegistry.register(ModBlocks.MYRTLE_LOG, ModBlocks.STRIPPED_MYRTLE_LOG);
        StrippableBlockRegistry.register(ModBlocks.MYRTLE_WOOD, ModBlocks.STRIPPED_MYRTLE_WOOD);

    }

}

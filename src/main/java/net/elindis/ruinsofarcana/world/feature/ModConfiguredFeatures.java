package net.elindis.ruinsofarcana.world.feature;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.elindis.ruinsofarcana.block.ModBlocks;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;

import java.util.List;
import java.util.OptionalInt;

public class ModConfiguredFeatures {

    // Spawns the tree from a sapling

    // -> Spawns like a small Azalea with an Acacia-like trunk
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> MYRTLE_TREE =
            ConfiguredFeatures.register("myrtle_tree", Feature.TREE, new TreeFeatureConfig.Builder(
                    BlockStateProvider.of(ModBlocks.MYRTLE_LOG),
                    new LargeOakTrunkPlacer(5, 2, 0), BlockStateProvider.of(ModBlocks.MYRTLE_LEAVES),
                    new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), 4),
//                    new RandomSpreadFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0),
//                            ConstantIntProvider.create(2), 60),
                    //new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 2),
                    new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))).ignoreVines().build());

    public static final RegistryEntry<PlacedFeature> MYRTLE_CHECKED =
            PlacedFeatures.register("myrtle_checked", MYRTLE_TREE,
                    PlacedFeatures.wouldSurvive(ModBlocks.MYRTLE_SAPLING));

    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> MYRTLE_SPAWN =
            ConfiguredFeatures.register("myrtle_spawn", Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfig(List.of(new RandomFeatureEntry(MYRTLE_CHECKED, 0.5f)),
                            MYRTLE_CHECKED));




    public static void registerConfiguredFeatures() {
        System.out.println("Registering ModConfiguredFeatures for " + RuinsOfArcana.MOD_ID);
    }
}
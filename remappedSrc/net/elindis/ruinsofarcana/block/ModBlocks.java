package net.elindis.ruinsofarcana.block;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.elindis.ruinsofarcana.item.ModItemGroup;
import net.elindis.ruinsofarcana.world.feature.tree.MyrtleSaplingGenerator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModBlocks {
    // Myrtle blocks
    public static final Block MYRTLE_LOG = registerBlock("myrtle_log",
            new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_LOG).requiresTool().strength(4.0f)), ModItemGroup.RUINSOFARCANA);
    public static final Block MYRTLE_WOOD = registerBlock("myrtle_wood",
            new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_LOG).requiresTool().strength(4.0f)), ModItemGroup.RUINSOFARCANA);
    public static final Block STRIPPED_MYRTLE_LOG = registerBlock("stripped_myrtle_log",
            new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_LOG).requiresTool().strength(4.0f)), ModItemGroup.RUINSOFARCANA);
    public static final Block STRIPPED_MYRTLE_WOOD = registerBlock("stripped_myrtle_wood",
            new PillarBlock(FabricBlockSettings.copy(Blocks.OAK_LOG).requiresTool().strength(4.0f)), ModItemGroup.RUINSOFARCANA);
    public static final Block MYRTLE_PLANKS = registerBlock("myrtle_planks",
            new Block(FabricBlockSettings.copy(Blocks.OAK_PLANKS).requiresTool().strength(4.0f)), ModItemGroup.RUINSOFARCANA);
    public static final Block MYRTLE_LEAVES = registerBlock("myrtle_leaves",
            new LeavesBlock(FabricBlockSettings.copy(Blocks.OAK_LEAVES).nonOpaque()), ModItemGroup.RUINSOFARCANA);
    public static final Block MYRTLE_SAPLING = registerBlock("myrtle_sapling",
            new ModSaplingBlock(new MyrtleSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)), ModItemGroup.RUINSOFARCANA);


    public static final Block ANCIENT_INSCRIBED_STONE = registerBlock("ancient_inscribed_stone",
            new Block(FabricBlockSettings.of(Material.METAL).strength(4f).requiresTool().luminance(3).sounds(BlockSoundGroup.METAL)),
            ModItemGroup.RUINSOFARCANA);

    public static final Block RICE_CROP = registerBlockWithoutItem("rice_crop",
            new RiceCropBlock(FabricBlockSettings.copy(Blocks.WHEAT)), ModItemGroup.RUINSOFARCANA);


    // Blocks with entities
    public static final Block PEDESTAL = registerBlock("pedestal",
            new PedestalBlock(FabricBlockSettings.of(Material.STONE).nonOpaque()), ModItemGroup.RUINSOFARCANA);

    public static final Block TRANSMUTATION_TABLE = registerBlock("transmutation_table",
            new TransmutationTableBlock(FabricBlockSettings.of(Material.STONE).nonOpaque()), ModItemGroup.RUINSOFARCANA);

    public static final Block AURIC_PIPE = registerBlock("auric_pipe",
            new AuricPipeBlock(FabricBlockSettings.of(Material.METAL).nonOpaque().strength(2f)
                    .sounds(BlockSoundGroup.LANTERN)), ModItemGroup.RUINSOFARCANA);

    public static final Block MANUFACTORY = registerBlock("manufactory",
            new ManufactoryBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()), ModItemGroup.RUINSOFARCANA);




    public static void registerModBlocks() {
        RuinsOfArcana.LOGGER.info("Registering ModBlocks for "+ RuinsOfArcana.MOD_ID);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group){
        return Registry.register(Registry.ITEM, new Identifier(RuinsOfArcana.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(RuinsOfArcana.MOD_ID, name), block);
    }
    private static Block registerBlockWithoutItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.BLOCK, new Identifier(RuinsOfArcana.MOD_ID, name), block);
    }


}

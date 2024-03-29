package net.elindis.ruinsofarcana.block.entity;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.elindis.ruinsofarcana.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<ManufactoryBlockEntity> MANUFACTORY;
    public static BlockEntityType<PedestalBlockEntity> PEDESTAL;
    public static BlockEntityType<TransmutationTableBlockEntity> TRANSMUTATION_TABLE;
    public static BlockEntityType<ModHopperBlockEntity> AURIC_PIPE;
    public static BlockEntityType<SingularityBlockEntity> SINGULARITY;
    public static BlockEntityType<GravityInverterBlockEntity> GRAVITY_INVERTER;
    public static BlockEntityType<AirPurifierBlockEntity> AIR_PURIFIER;

    public static void registerAllBlockEntities() {

        MANUFACTORY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(RuinsOfArcana.MOD_ID, "manufactory"),
                FabricBlockEntityTypeBuilder.create(ManufactoryBlockEntity::new,
                        ModBlocks.MANUFACTORY).build(null));

        PEDESTAL = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(RuinsOfArcana.MOD_ID, "pedestal"),
                FabricBlockEntityTypeBuilder.create(PedestalBlockEntity::new,
                        ModBlocks.PEDESTAL).build(null));

        TRANSMUTATION_TABLE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(RuinsOfArcana.MOD_ID, "transmutation_table"),
                FabricBlockEntityTypeBuilder.create(TransmutationTableBlockEntity::new,
                        ModBlocks.TRANSMUTATION_TABLE).build(null));

        AURIC_PIPE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(RuinsOfArcana.MOD_ID, "auric_pipe"),
                FabricBlockEntityTypeBuilder.create(ModHopperBlockEntity::new,
                        ModBlocks.AURIC_PIPE).build(null));

        SINGULARITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(RuinsOfArcana.MOD_ID, "singularity"),
                FabricBlockEntityTypeBuilder.create(SingularityBlockEntity::new,
                        ModBlocks.SINGULARITY).build(null));

        GRAVITY_INVERTER = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(RuinsOfArcana.MOD_ID, "gravity_inverter"),
                FabricBlockEntityTypeBuilder.create(GravityInverterBlockEntity::new,
                        ModBlocks.GRAVITY_INVERTER).build(null));

        AIR_PURIFIER = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(RuinsOfArcana.MOD_ID, "air_purifier"),
                FabricBlockEntityTypeBuilder.create(AirPurifierBlockEntity::new,
                        ModBlocks.AIR_PURIFIER).build(null));
    }
}

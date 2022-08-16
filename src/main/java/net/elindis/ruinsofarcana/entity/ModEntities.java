package net.elindis.ruinsofarcana.entity;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ModEntities {

//    public static final EntityType<FireballEntity> PackedSnowballEntityType = Registry.register(
//            Registry.ENTITY_TYPE,
//            new Identifier(RuinsOfAkhTal.MOD_ID, "packed_snowball"),
//            FabricEntityTypeBuilder.<FireballEntity>create(SpawnGroup.MISC, FireballEntity::new)
//                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
//                    .trackRangeBlocks(64).trackedUpdateRate(8) // trackRangeBlocks is how far out it renders (in blocks), I think.
//                    .build()
//    );
//    public static final EntityType<FrostBoltEntity> FROST_BOLT_ENTITY_ENTITY_TYPE = Registry.register(
//            Registry.ENTITY_TYPE,
//            new Identifier(RuinsOfAkhTal.MOD_ID, "magic_arrow"),
//            FabricEntityTypeBuilder.<FrostBoltEntity>create(SpawnGroup.MISC, FrostBoltEntity::new)
//                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
//                    .trackRangeBlocks(64).trackedUpdateRate(8) // trackRangeBlocks is how far out it renders (in blocks), I think.
//                    .build()
//    );
//
//    public static final EntityType<LightArrowEntity> LIGHT_ARROW_ENTITY_TYPE = Registry.register(
//            Registry.ENTITY_TYPE,
//            new Identifier(RuinsOfAkhTal.MOD_ID, "light_arrow"),
//            FabricEntityTypeBuilder.<LightArrowEntity>create(SpawnGroup.MISC, LightArrowEntity::new)
//                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
//                    .trackRangeBlocks(64).trackedUpdateRate(8) // trackRangeBlocks is how far out it renders (in blocks), I think.
//                    .build()
//    );

    //-----Entity Map-----//
    public static final LinkedHashMap<EntityType<?>, Identifier> ENTITIES = new LinkedHashMap<>();

    public static final EntityType<LightArrowEntity> LIGHT_ARROW_ENTITY_TYPE = create("light_arrow",
            FabricEntityTypeBuilder.<LightArrowEntity>create(SpawnGroup.MISC, LightArrowEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
                    .trackRangeBlocks(64).trackedUpdateRate(8) // trackRangeBlocks is how far out it renders (in blocks), I think.
                    .build());

    public static final EntityType<FrostBoltEntity> FROST_BOLT_ENTITY_ENTITY_TYPE = create("frost_bolt",
            FabricEntityTypeBuilder.<FrostBoltEntity>create(SpawnGroup.MISC, FrostBoltEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
                    .trackRangeBlocks(64).trackedUpdateRate(8) // trackRangeBlocks is how far out it renders (in blocks), I think.
                    .build());

    public static final EntityType<FireballEntity> FIREBALL_ENTITY_TYPE = create("packed_snowball",
            FabricEntityTypeBuilder.<FireballEntity>create(SpawnGroup.MISC, FireballEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
                    .trackRangeBlocks(64).trackedUpdateRate(8) // trackRangeBlocks is how far out it renders (in blocks), I think.
                    .build());


    //-----Registry-----//
    public static void register() {
        ENTITIES.keySet().forEach(entityType -> Registry.register(Registry.ENTITY_TYPE, ENTITIES.get(entityType), entityType));
        System.out.println("Registering entities.");
    }

    private static <T extends Entity> EntityType<T> create(String name, EntityType<T> type) {
        ENTITIES.put(type, new Identifier(RuinsOfArcana.MOD_ID, name));
        return type;
    }


}

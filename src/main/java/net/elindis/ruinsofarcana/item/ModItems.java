package net.elindis.ruinsofarcana.item;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.elindis.ruinsofarcana.block.ModBlocks;
import net.elindis.ruinsofarcana.item.equipment.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {

    // Component items

    // Copper, gold, and luxite makes Auric Ingots!
    public static final Item AURIC_INGOT = registerItem(
            "auric_ingot", new Item(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA)));

    public static final Item AURIC_NUGGET = registerItem(
            "auric_nugget", new Item(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA)));

    // Luxite is the most basic. It stores and emits light, and is the least energetic of the three.
    public static final Item LUXITE = registerItem(
            "luxite", new Item(new Item.Settings().group(ModItemGroup.RUINSOFARCANA)));

    // Prismite is created by infusing base materials with experience. It's volatile and energetic.
    public static final Item PRISMITE = registerItem(
            "prismite", new Item(new Item.Settings().group(ModItemGroup.RUINSOFARCANA)));

    // Iridite is created in a forbidden ritual. It is extremely energetic and powerful, but it is treacherous.
    public static final Item IRIDITE = registerItem(
            "iridite", new Item(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA)));

    // Research items

    public static final Item CHARCOAL_RUBBING = registerItem(
            "charcoal_rubbing", new Item(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA)));

    // Weapons and Tools
    public static final Item ARKENITE_MULTITOOL = registerItem(
            "arkenite_multitool", new MultitoolItem(ModToolMaterials.AURIC, 1, -2.2f,
                    new FabricItemSettings().rarity(Rarity.UNCOMMON).group(ModItemGroup.RUINSOFARCANA)));

    public static final Item THUNDER_MACE = registerItem(
            "thunder_mace", new ThunderMaceItem(ModToolMaterials.AURIC, 2, -3.2f,
                    new FabricItemSettings().rarity(Rarity.UNCOMMON).group(ModItemGroup.RUINSOFARCANA)));

    public static final Item FROZEN_LANCE = registerItem(
            "frozen_lance", new FrozenLanceItem(ModToolMaterials.AURIC, 3, -2.4f,
                    new FabricItemSettings().rarity(Rarity.UNCOMMON).group(ModItemGroup.RUINSOFARCANA)));

    public static final Item SOLAR_SWORD = registerItem(
            "solar_sword", new SolarSwordItem(ModToolMaterials.AURIC, 3, -2.4f,
                    new FabricItemSettings().rarity(Rarity.UNCOMMON).group(ModItemGroup.RUINSOFARCANA)));

    public static final Item WAND_OF_TRANSMUTATION = registerItem(
            "wand_of_transmutation", new WandOfTransmutationItem(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).maxCount(1).maxDamage(1)));

    public static final Item WAND_OF_FROST_BOLT = registerItem(
            "wand_of_frost_bolt", new WandOfFrostBoltItem(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).maxCount(1).maxDamage(1)));

    public static final Item WAND_OF_FIREBALL = registerItem(
            "wand_of_fireball", new WandOfFireballItem(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).maxCount(1).maxDamage(1)));

    public static final Item WAND_OF_FIRESTORM = registerItem(
            "wand_of_firestorm", new WandOfFirestormItem(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).maxCount(1).maxDamage(1)));

    public static final Item WAND_OF_DISINTEGRATION = registerItem(
            "wand_of_disintegration", new WandOfDisintegrationItem(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).maxCount(1).maxDamage(1)));

    public static final Item WAND_OF_CYCLONE = registerItem(
            "wand_of_cyclone", new WandOfCycloneItem(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).maxCount(1).maxDamage(1)));

    public static final Item WAND_OF_WHIRLWIND = registerItem(
            "wand_of_whirlwind", new WandOfWhirlwindItem(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).maxCount(1).maxDamage(1)));

    public static final Item LIGHT_BOW = registerItem(
            "light_bow", new LightBowItem(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).maxCount(1).maxDamage(750)));

    public static final Item SCRIBES_PEN = registerItem(
            "scribes_pen", new ScribesPenItem(new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).maxCount(1).maxDamage(15)));

    // Armor
    public static final Item TRUESIGHT_GOGGLES = registerItem(
            "truesight_goggles", new AuricArmorItem(ModArmorMaterials.AURIC, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).fireproof()));

    public static final Item DIVING_NECKLACE = registerItem(
            "diving_necklace", new AuricArmorItem(ModArmorMaterials.AURIC, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).fireproof()));

    public static final Item GRAVITY_BELT = registerItem(
            "gravity_belt", new AuricArmorItem(ModArmorMaterials.AURIC, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).fireproof()));

    public static final Item TRAVELERS_BOOTS = registerItem(
            "travelers_boots", new AuricArmorItem(ModArmorMaterials.AURIC, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).fireproof()));

    public static final Item CIRCLET_OF_REBIRTH = registerItem(
            "circlet_of_rebirth", new AuricArmorItem(ModArmorMaterials.AURIC, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).fireproof().maxDamage(100)));


    // Ancient notes
    public static final Item ANCIENT_NOTE_GENERIC = registerItem(
            "ancient_note_sealed", new AncientResearchNoteGenericItem(new FabricItemSettings().maxCount(1).group(ModItemGroup.RUINSOFARCANA)));
    public static final Item ANCIENT_NOTE_1 = registerItem(
            "ancient_note_1", new AncientNoteItem_1(new FabricItemSettings().rarity(Rarity.UNCOMMON).group(ModItemGroup.RUINSOFARCANA)));

    // Food items
    public static final Item RAW_RICE = registerItem(
            "raw_rice", new AliasedBlockItem(ModBlocks.RICE_CROP,
                    new FabricItemSettings().group(ModItemGroup.RUINSOFARCANA).food(ModFoodComponents.RAW_RICE)));
    public static final Item COOKED_RICE = registerItem(
            "cooked_rice", new Item(new FabricItemSettings().food(ModFoodComponents.COOKED_RICE).group(ModItemGroup.RUINSOFARCANA)));
    public static final Item RICE_BALL = registerItem(
            "rice_ball", new Item(new FabricItemSettings().food(ModFoodComponents.RICE_BALL).group(ModItemGroup.RUINSOFARCANA)));
    public static final Item FISH_ROLL = registerItem(
            "fish_roll", new Item(new FabricItemSettings().food(ModFoodComponents.FISH_ROLL).group(ModItemGroup.RUINSOFARCANA)));


    // Serums
    public static final Item SERUM_OF_SPEED = registerItem(
            "serum_of_speed", new SerumItem(new FabricItemSettings().food(ModFoodComponents.SPEED)
                    .maxCount(16).group(ModItemGroup.RUINSOFARCANA)));

    // Throwables and gadgets
    public static final Item SMOKEBOMB = registerItem(
            "smokebomb", new SmokeBombItem(new FabricItemSettings().maxCount(16).group(ModItemGroup.RUINSOFARCANA)));
    public static final Item ENDER_POUCH = registerItem(
            "ender_pouch", new EnderPouchItem(new FabricItemSettings().maxCount(1).group(ModItemGroup.RUINSOFARCANA)));



    // Methods
    public static void registerItems() {
        RuinsOfArcana.LOGGER.info(("Registering items for "+ RuinsOfArcana.MOD_ID));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(RuinsOfArcana.MOD_ID, name), item);
    }
}

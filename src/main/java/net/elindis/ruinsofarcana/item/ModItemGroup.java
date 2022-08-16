package net.elindis.ruinsofarcana.item;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final net.minecraft.item.ItemGroup RUINSOFARCANA = FabricItemGroupBuilder.build(new Identifier(RuinsOfArcana.MOD_ID, "ruinsofarcana"),
            () -> new ItemStack(ModItems.AURIC_INGOT));
}

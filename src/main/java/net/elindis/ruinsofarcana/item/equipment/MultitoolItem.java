package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.util.ModTags;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;

public class MultitoolItem extends MiningToolItem {

    public MultitoolItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, material, ModTags.Blocks.MULTITOOL_MINEABLE, settings);
    }



}

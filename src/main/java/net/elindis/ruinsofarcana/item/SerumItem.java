package net.elindis.ruinsofarcana.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class SerumItem extends Item {

    public SerumItem(Settings settings) {
        super(settings);
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }


}

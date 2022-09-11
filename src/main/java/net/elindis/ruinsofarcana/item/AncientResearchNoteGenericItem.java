package net.elindis.ruinsofarcana.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;


public class AncientResearchNoteGenericItem extends Item {
    public AncientResearchNoteGenericItem(Settings settings) {
        super(settings);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ruinsofarcana.ancient_note_sealed.tooltip"));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient() && hand == Hand.MAIN_HAND)
        {
            // Decrement itemstack if the user ISN'T in creative mode.
            if (!user.getAbilities().creativeMode) { itemStack.decrement(1); }

            // Gives the user a random item!
            int noteIndex = getRandomNote(1, 2);
            switch (noteIndex) {
                default: break;
                case 1: user.getInventory().insertStack(new ItemStack(ModItems.ANCIENT_NOTE_1)); break;
                case 2: user.getInventory().insertStack(new ItemStack(ModItems.AURIC_INGOT)); break;
            }
        }

    return super.use(world, user, hand);
    }
    private int getRandomNote(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

}

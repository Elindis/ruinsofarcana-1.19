package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.block.ModBlocks;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SealingParchmentItem extends Item {
    public SealingParchmentItem(Settings settings) {
        super(settings);
    }



    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        // First, set up the objects we're accessing from "context" for ease of reading.
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        ItemStack itemStack = context.getStack();
        PlayerEntity playerEntity = context.getPlayer();

        // This is the block we use our item on.
        BlockState blockState = world.getBlockState(blockPos);

        // Check to see if the block is eligible
        if (blockState.equals(ModBlocks.ANCIENT_INSCRIBED_STONE.getDefaultState()) ) {

            // Tells the server we used this on a block.
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            // If we're on the server, turned an Ancient Inscribed Stone into polished deepslate!
            if (!world.isClient()) {
                world.setBlockState(blockPos, Blocks.POLISHED_DEEPSLATE.getDefaultState());

                // Damages the item a little bit.
                if (playerEntity != null) {
                    // Consumes the parchment.
                    itemStack.decrement(1);
                    //playerEntity.getItemCooldownManager().set(this, 20);
                    playerEntity.sendMessage(Text.literal("Tried to retrieve an inscription."));
                    playerEntity.getInventory().insertStack(new ItemStack(Items.PAPER));
                }
            }


            // Plays a sounds for the player, and makes them do the "use item" animation.
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResult.success(world.isClient());
        }
        else {
            return super.useOnBlock(context);
        }

    }

}


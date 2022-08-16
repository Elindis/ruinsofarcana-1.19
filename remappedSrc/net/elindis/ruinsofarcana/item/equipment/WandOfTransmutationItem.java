package net.elindis.ruinsofarcana.item.equipment;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;


public class WandOfTransmutationItem extends Item {
    public WandOfTransmutationItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        // Let's straighten out all of the context we need first.
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockState blockState = world.getBlockState(blockPos);

        //  Let's do server things!
        if (!context.getWorld().isClient) {

            // Stone, granite, diorite, andesite
            if (blockState.isIn(BlockTags.STONE_ORE_REPLACEABLES)) {}


            // Here we create a list of all blocks that have the same material as the block we click!
            ArrayList<Block> blockList = new ArrayList<Block>();
            for (int i = 0; i < Registry.BLOCK.size(); i++) {
                if (Registry.BLOCK.get(i).getDefaultState().getMaterial() == blockState.getMaterial()) {
                    blockList.add(Registry.BLOCK.get(i));
                }
            }

            // Next we select a random block from the list and change our block into that one!
            int randomBlock = Random.create().nextBetween(0, blockList.size());
            world.setBlockState(blockPos, blockList.get(randomBlock).getDefaultState());

        }
        return ActionResult.SUCCESS;
    }
}

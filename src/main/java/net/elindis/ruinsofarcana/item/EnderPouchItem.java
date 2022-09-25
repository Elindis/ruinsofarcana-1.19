package net.elindis.ruinsofarcana.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class EnderPouchItem extends Item{
	public EnderPouchItem(Settings settings) {
		super(settings);
	}


	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		user.playSound(SoundEvents.BLOCK_ENDER_CHEST_OPEN, 1, 1);
		if (world.isClient) return TypedActionResult.success(user.getStackInHand(hand));

		Inventory enderPouchInventory = user.getEnderChestInventory();
		user.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player)
				-> GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, enderPouchInventory), Text.literal("Ender Chest")));
		return TypedActionResult.pass(user.getStackInHand(hand));
	}

}

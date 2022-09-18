package net.elindis.ruinsofarcana.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import java.util.Set;
import java.util.function.Predicate;

public class EnderPouchItem extends Item{
	public EnderPouchItem(Settings settings) {
		super(settings);
	}


	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		user.playSound(SoundEvents.BLOCK_ENDER_CHEST_OPEN, 1, 1);
		if (world.isClient) return TypedActionResult.success(user.getStackInHand(hand));

		Inventory enderChestInventory = user.getEnderChestInventory();
		user.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player)
				-> GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, enderChestInventory), Text.literal("Ender Chest")));
		return TypedActionResult.pass(user.getStackInHand(hand));
	}

}

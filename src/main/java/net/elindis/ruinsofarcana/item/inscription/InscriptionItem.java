package net.elindis.ruinsofarcana.item.inscription;

import net.elindis.ruinsofarcana.item.ModItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;


public class InscriptionItem extends Item {
	public InscriptionItem(Settings settings) {
		super(settings);
	}


	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		user.playSound(SoundEvents.BLOCK_ENDER_CHEST_OPEN, 1, 1);
		if (world.isClient) return TypedActionResult.success(user.getStackInHand(hand));

		if (user.getOffHandStack().isOf(ModItems.ARCANISTS_STAFF)) {
			NbtList nbtList = Objects.requireNonNull(user.getOffHandStack().getNbt()).getList("spell_list", 8);

//			// If possible, fill an empty slot
//			if (nbtList.getString(0).equals("Empty")) {
//				nbtList.set(0, NbtString.of(getInscriptionName()));
//				return TypedActionResult.pass(user.getStackInHand(hand));
//			}
//			if (nbtList.getString(1).equals("Empty")) {
//				nbtList.set(1, NbtString.of(getInscriptionName()));
//				return TypedActionResult.pass(user.getStackInHand(hand));
//			}
//			if (nbtList.getString(2).equals("Empty")) {
//				nbtList.set(2, NbtString.of(getInscriptionName()));
//				return TypedActionResult.pass(user.getStackInHand(hand));
//			}

			// We overwrite the active slot, unless it's identical to the inscription... that would be silly.
			// Should inscriptions be single-use? Hm.
			int selectedSpell = user.getOffHandStack().getNbt().getInt("spell_selected");
			if (!nbtList.getString(selectedSpell).equals(getInscriptionName())) {
				user.sendMessage(Text.of("Changed '"+nbtList.getString(selectedSpell)+"' to '"+getInscriptionName()+"'"), true);
				nbtList.set(selectedSpell, NbtString.of(getInscriptionName()));
//				user.getStackInHand(hand).decrement(1);
			}


		}
		return TypedActionResult.pass(user.getStackInHand(hand));
	}

	public String getInscriptionName() {
		return "Empty";
	}
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.literal("Use this inscription while holding your").formatted(Formatting.GRAY));
		tooltip.add(Text.literal("staff to enchant it with a new spell.").formatted(Formatting.GRAY));
	}
}

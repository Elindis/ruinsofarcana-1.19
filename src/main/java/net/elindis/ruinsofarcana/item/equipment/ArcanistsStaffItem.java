package net.elindis.ruinsofarcana.item.equipment;

import net.elindis.ruinsofarcana.spell.Spell;
import net.elindis.ruinsofarcana.spell.SpellList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ArcanistsStaffItem extends WandItem {
	public ArcanistsStaffItem(Settings settings) {
		super(settings);

	}
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		// Gets the currently selected spell from the item's NBT data
		Spell currentSpell = SpellList.getCurrentSpell(stack);

		// Check if the LivingEntity is a PlayerEntity.
		if (user instanceof PlayerEntity playerEntity) {

			// You can only cast if the spell is at least 50% charged.
			int i = this.getMaxUseTime(stack) - remainingUseTicks;
			float f = getPullProgress(i);
			if (((double)f > currentSpell.getReadyPercentage())) {

				// The conditions for the spell to be castable. On spell success:
				if (!isRaycaster()) {
					// Server-side effects.
					if (!world.isClient) {
						currentSpell.doSpellEffect(world, user, f);
					}
				}
				if (isRaycaster()) {
					float tickDelta = 1;

					HitResult.Type raycastType = user.raycast(getRange(), tickDelta,true).getType();
					if (!raycastType.equals(HitResult.Type.MISS)) {
						// Server-side effects.
						currentSpell.doSpellEffect(world, user, f);
					}
				}
			}
		}
	}

	@Override
	public ItemStack getDefaultStack() {
		ItemStack stack = this.asItem().getDefaultStack();
		NbtCompound nbt = new NbtCompound();
		NbtList spellList = new NbtList();

		spellList.add(0, NbtString.of("Empty"));
		spellList.add(1, NbtString.of("Empty"));
		spellList.add(2, NbtString.of("Empty"));

		nbt.put("spell_list", spellList);
		nbt.putInt("spell_selected", 0);

		stack.setNbt(nbt);
		return stack;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			ItemStack thisStack = new ItemStack(this);
			NbtCompound nbt = new NbtCompound();
			NbtList spellList = new NbtList();

			spellList.add(0, NbtString.of("Empty"));
			spellList.add(1, NbtString.of("Inferno"));
			spellList.add(2, NbtString.of("Weird"));

			nbt.put("spell_list", spellList);
			nbt.putInt("spell_selected", 0);

			thisStack.setNbt(nbt);
			stacks.add(thisStack);
		}
	}

	public void onCraft(ItemStack stack, World world, PlayerEntity player) {

		NbtCompound nbt = new NbtCompound();
		NbtList spellList = new NbtList();

		spellList.add(0, NbtString.of("Empty"));
		spellList.add(1, NbtString.of("Empty"));
		spellList.add(2, NbtString.of("Empty"));

		nbt.put("spell_list", spellList);
		nbt.putInt("spell_selected", 0);

		stack.setNbt(nbt);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (stack.getNbt() != null) {
			NbtList list = stack.getNbt().getList("spell_list", 8);
			int activeSpell = stack.getNbt().getInt("spell_selected");
			tooltip.add(Text.of("Spells:"));
			tooltip.add((activeSpell==0) ? Text.literal(" - "+list.getString(0)).formatted(Formatting.AQUA) : Text.literal(" - "+list.getString(0)).formatted(Formatting.GRAY));
			tooltip.add((activeSpell==1) ? Text.literal(" - "+list.getString(1)).formatted(Formatting.AQUA) : Text.literal(" - "+list.getString(1)).formatted(Formatting.GRAY));
			tooltip.add((activeSpell==2) ? Text.literal(" - "+list.getString(2)).formatted(Formatting.AQUA) : Text.literal(" - "+list.getString(2)).formatted(Formatting.GRAY));
		}
	}
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		super.use(world, user, hand);
		return TypedActionResult.consume(user.getStackInHand(hand));
	}
	@Override
	public void doUsageParticles(World world, LivingEntity user, ItemStack stack) {
		int random2 = world.getRandom().nextInt(9);
		if (random2 > 6) {
			float random = (user.getRandom().nextFloat()/10)-0.05f;
			float initialOffsetX = 0;
			float initialOffsetZ = 0;
			float additionalOffsetX = 0;
			float additionalOffsetZ = 0;

			if (user.getMainHandStack().isOf(this.asItem())) {

				initialOffsetX += (-MathHelper.cos(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()-60))*(1f/3));
				initialOffsetZ += (-MathHelper.sin(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()-60))*(1f/3));
				if (user.getPitch()<-30) {
					float pitch = user.getPitch()*-1/30;
					initialOffsetX*=1/pitch;
					initialOffsetZ*=1/pitch;
					additionalOffsetX = -MathHelper.cos(MathHelper.RADIANS_PER_DEGREE*(user.getHeadYaw()+30))/3;
					additionalOffsetX *= pitch/2;
					additionalOffsetZ = -MathHelper.sin(MathHelper.RADIANS_PER_DEGREE*(user.getHeadYaw()+30))/3;
					additionalOffsetZ *= pitch/2;
				}
			}
			if (user.getOffHandStack().isOf(this.asItem())) {
				initialOffsetX += (MathHelper.cos(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()+60))/3);
				initialOffsetZ += (MathHelper.sin(MathHelper.RADIANS_PER_DEGREE * (user.getHeadYaw()+60))/3);
				if (user.getPitch()<-30) {
					float pitch = user.getPitch()*-1/30;
					initialOffsetX*=1/pitch;
					initialOffsetZ*=1/pitch;
					additionalOffsetX = MathHelper.cos(MathHelper.RADIANS_PER_DEGREE*(user.getHeadYaw()-30))/3;
					additionalOffsetX *= pitch/2;
					additionalOffsetZ = MathHelper.sin(MathHelper.RADIANS_PER_DEGREE*(user.getHeadYaw()-30))/3;
					additionalOffsetZ *= pitch/2;
				}
			}

			double xPos = user.getX() + initialOffsetX + additionalOffsetX;
			double yPos = user.getEyeY()-(MathHelper.sin((MathHelper.RADIANS_PER_DEGREE* user.getPitch(MinecraftClient.getInstance().getTickDelta())))/2);
			double zPos = user.getZ() + initialOffsetZ+ additionalOffsetZ;
			world.addParticle(getParticleType(stack), xPos, yPos+0.2, zPos, random, random, random);
		}
	}

	private ParticleEffect getParticleType(ItemStack stack) {
		return SpellList.getCurrentSpell(stack).getParticleType();
	}
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (!world.isClient) return;
		doUsageParticles(world, user, stack);
	}
	public static float getPullProgress(int useTicks) {
		float f = (float)useTicks / 20.0f;
		if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
			f = 1.0f;
		}
		return f;
	}
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public Predicate<ItemStack> getProjectiles() {
		return null;
	}

}





//	@Override
//	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//		super.use(world, user, hand);
//		(ServerWorld) world.getServer().getCommandManager().getDispatcher().execute(
//		)
//		return TypedActionResult.pass(user.getStackInHand(hand));
//	}


	//	private void openAdvancementScreen(World world, PlayerEntity user, Hand hand) {
//		assert MinecraftClient.getInstance().player != null;
//
//		AdvancementsScreen advancementsScreen = new AdvancementsScreen(MinecraftClient.getInstance().player.networkHandler.getAdvancementHandler());
//		Advancement modAdvancement = getModAdvancement(user);
//
//		if (world.isClient)	MinecraftClient.getInstance().setScreen(advancementsScreen);
//		Screen modScreen = MinecraftClient.getInstance().currentScreen;
//
//		if (modScreen instanceof AdvancementsScreen) {
//			((AdvancementsScreen) modScreen).selectTab(modAdvancement);
//		}
//	}
//
//	private Advancement getModAdvancement(PlayerEntity player) {
//		Identifier id = new Identifier("ruinsofarcana:lightbow");
//		if (player instanceof ServerPlayerEntity) {
//			return Objects.requireNonNull(player.getServer()).getAdvancementLoader().get(id);
//		}
//		else return null;
//	}

//}

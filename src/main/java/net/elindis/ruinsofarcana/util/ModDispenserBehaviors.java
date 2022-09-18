package net.elindis.ruinsofarcana.util;

import net.elindis.ruinsofarcana.entity.SmokeBombEntity;
import net.elindis.ruinsofarcana.item.ModItems;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class ModDispenserBehaviors {

	public static void registerDispenserBehaviours() {
		DispenserBlock.registerBehavior(ModItems.SMOKEBOMB, new ProjectileDispenserBehavior(){
			@Override
			protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
				return Util.make(new SmokeBombEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
			}
		});
	}
}

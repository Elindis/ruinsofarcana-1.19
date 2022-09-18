package net.elindis.ruinsofarcana.particle;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticles {

	public static final DefaultParticleType SINGULARITY_PARTICLE = FabricParticleTypes.simple();
	public static final DefaultParticleType CORE_PARTICLE = FabricParticleTypes.simple();
	public static final DefaultParticleType SPIRAL_PARTICLE = FabricParticleTypes.simple();
	public static final DefaultParticleType JET_PARTICLE = FabricParticleTypes.simple();
	public static final DefaultParticleType TORNADO_PARTICLE = FabricParticleTypes.simple();
//	public static final DefaultParticleType FIRE_TORNADO_PARTICLE = FabricParticleTypes.simple();
	public static final DefaultParticleType FIRE_PARTICLE = FabricParticleTypes.simple();

	public static void registerParticles() {
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(RuinsOfArcana.MOD_ID, "singularity_particle"), SINGULARITY_PARTICLE);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(RuinsOfArcana.MOD_ID, "core_particle"), CORE_PARTICLE);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(RuinsOfArcana.MOD_ID, "spiral_particle"), SPIRAL_PARTICLE);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(RuinsOfArcana.MOD_ID, "jet_particle"), JET_PARTICLE);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(RuinsOfArcana.MOD_ID, "tornado_particle"), TORNADO_PARTICLE);
//		Registry.register(Registry.PARTICLE_TYPE, new Identifier(RuinsOfArcana.MOD_ID, "fire_tornado_particle"), FIRE_TORNADO_PARTICLE);
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(RuinsOfArcana.MOD_ID, "fire_particle"), FIRE_PARTICLE);

	}
}

package net.elindis.ruinsofarcana.sound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModSounds {

    public static final Identifier LIGHT_BOW_SHOT_ID = new Identifier("ruinsofarcana:light_bow_shot");
    public static SoundEvent LIGHT_BOW_SHOT = new SoundEvent(LIGHT_BOW_SHOT_ID);

    public static final Identifier THUNDER_MACE_ID = new Identifier("ruinsofarcana:thunder_mace");
    public static SoundEvent THUNDER_MACE = new SoundEvent(THUNDER_MACE_ID);

    public static final Identifier THUNDER_MACE_ID2 = new Identifier("ruinsofarcana:thunder_mace2");
    public static SoundEvent THUNDER_MACE2 = new SoundEvent(THUNDER_MACE_ID2);

    public static final Identifier RESEARCH_PROGRESS_ID = new Identifier("ruinsofarcana:research_progress");
    public static SoundEvent RESEARCH_PROGRESS = new SoundEvent(RESEARCH_PROGRESS_ID);

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, ModSounds.LIGHT_BOW_SHOT_ID, LIGHT_BOW_SHOT);
        Registry.register(Registry.SOUND_EVENT, ModSounds.THUNDER_MACE_ID, THUNDER_MACE);
        Registry.register(Registry.SOUND_EVENT, ModSounds.THUNDER_MACE_ID2, THUNDER_MACE2);
        Registry.register(Registry.SOUND_EVENT, ModSounds.RESEARCH_PROGRESS_ID, RESEARCH_PROGRESS);
    }

}

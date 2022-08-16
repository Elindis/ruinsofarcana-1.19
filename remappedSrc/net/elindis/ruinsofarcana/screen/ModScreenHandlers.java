package net.elindis.ruinsofarcana.screen;

import net.elindis.ruinsofarcana.RuinsOfArcana;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static ScreenHandlerType<ManufactoryScreenHandler> MANUFACTORY_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        MANUFACTORY_SCREEN_HANDLER =
                ScreenHandlerRegistry.registerSimple(new Identifier(RuinsOfArcana.MOD_ID, "manufactory"),
                        ManufactoryScreenHandler::new);
    }
}
package io.github.minnecraeft.toughenough.init;

import io.github.minnecraeft.toughenough.ToughEnough;
import io.github.minnecraeft.toughenough.screen.ClimatizerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class ToughEnoughScreenHandlers {

    public static final ScreenHandlerType<ClimatizerScreenHandler> CLIMATIZER_SCREEN_HANDLER;

    static {
        CLIMATIZER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(ToughEnough.identifier("climatizer"), ClimatizerScreenHandler::new);
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // keep for class initialisation (call from initializer)
    }
}

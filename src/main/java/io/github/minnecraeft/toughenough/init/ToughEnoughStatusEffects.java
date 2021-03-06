package io.github.minnecraeft.toughenough.init;

import io.github.minnecraeft.toughenough.entity.effect.HyperthermiaStatusEffect;
import io.github.minnecraeft.toughenough.entity.effect.HypothermiaStatusEffect;
import io.github.minnecraeft.toughenough.entity.effect.ThirstStatusEffect;
import net.minecraft.entity.effect.StatusEffect;

public final class ToughEnoughStatusEffects {

    public static final StatusEffect HYPERTHERMIA;
    public static final StatusEffect HYPOTHERMIA;
    public static final StatusEffect THIRST;

    static {
        HYPERTHERMIA = RegistryHelpers.register("hyperthermia", new HyperthermiaStatusEffect());
        HYPOTHERMIA = RegistryHelpers.register("hypothermia", new HypothermiaStatusEffect());
        THIRST = RegistryHelpers.register("thirst", new ThirstStatusEffect());
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // keep for class initialisation (call from initializer)
    }

}

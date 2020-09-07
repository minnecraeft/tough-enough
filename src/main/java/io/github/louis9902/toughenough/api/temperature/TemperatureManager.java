package io.github.louis9902.toughenough.api.temperature;

import dev.onyxstudios.cca.api.v3.component.AutoSyncedComponent;
import io.github.louis9902.toughenough.api.debug.DebugMonitor;
import net.minecraft.util.Identifier;

public interface TemperatureManager extends AutoSyncedComponent {

    int getTarget();

    int getTemperature();

    int getRate();

    void update();

    void addModifierTarget(Identifier identifier, int amount, int duration);

    void addModifierRate(Identifier identifier, int amount, int duration);

    boolean getDebug();

    void setDebug(boolean value);

    DebugMonitor getTargetMonitor();

    DebugMonitor getRateMonitor();
}

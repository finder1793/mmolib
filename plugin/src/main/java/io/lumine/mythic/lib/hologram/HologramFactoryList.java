package io.lumine.mythic.lib.hologram;

import io.lumine.mythic.lib.hologram.factory.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;

import java.lang.reflect.InvocationTargetException;

public enum HologramFactoryList {
    HOLOGRAPHIC_DISPLAYS("HolographicDisplays", HDHologramFactory.class, ServicePriority.High),
    DECENT_HOLOGRAMS("DecentHolograms", DecentHologramFactory.class, ServicePriority.Normal),
    HOLOGRAMS("Holograms", HologramsHologramFactory.class, ServicePriority.Normal),
    CMI("CMI", CMIHologramFactory.class, ServicePriority.Low),
    TR_HOLOGRAM("TrHologram", TrHologramFactory.class, ServicePriority.Normal);

    private final String pluginName;
    private final Class<? extends HologramFactory> factoryClass;
    private final ServicePriority priority;

    private HologramFactoryList(String pluginName, Class<? extends HologramFactory> factoryClass, ServicePriority priority) {
        this.pluginName = pluginName;
        this.factoryClass = factoryClass;
        this.priority = priority;
    }

    public String getPluginName() {
        return pluginName;
    }

    public boolean isInstalled(PluginManager manager) {
        return manager.getPlugin(pluginName) != null;
    }

    public HologramFactory generateFactory() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return factoryClass.getConstructor().newInstance();
    }

    public ServicePriority getServicePriority() {
        return priority;
    }
}

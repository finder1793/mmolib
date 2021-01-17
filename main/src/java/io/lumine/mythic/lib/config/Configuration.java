package io.lumine.mythic.lib.config;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.PropertyHolder;
import io.lumine.utils.config.properties.types.IntProp;
import io.lumine.utils.plugin.ReloadableModule;

public class Configuration extends ReloadableModule<MythicLib> implements PropertyHolder {
	
    private final static IntProp CLOCK_INTERVAL = Property.Int(Scope.CONFIG, "Clock.Interval", 1);
    
    public Configuration(MythicLib plugin)  {
        super(plugin);
    }
    
    @Override
    public void load(MythicLib plugin) {
        
    }
  
    @Override
    public void unload() {}

    @Override
    public String getPropertyNode() {
        return "Configuration";
    }
    
    public int getClockInterval() {
        return CLOCK_INTERVAL.get(this);
    }
}

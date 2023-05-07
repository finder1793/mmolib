package io.lumine.mythic.lib.util.config;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * TODO
 *
 * @deprecated Not implemented
 */
@Deprecated
public class YamlFile extends ConfigFile {
    public YamlFile(Plugin plugin, File file) {
        super(plugin, file);
    }

    @NotNull
    @Override
    public Object getContent() {
        return null;
    }

    @Override
    public void save() {

    }
}

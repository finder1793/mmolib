package io.lumine.mythic.lib.skill.handler;

import io.lumine.mythic.api.config.MythicConfig;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.core.config.GenericConfig;
import io.lumine.mythic.utils.config.MemorySection;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class MythicConfigImpl implements GenericConfig, Cloneable, MythicConfig {
    private String configName;
    private File file;
    private ConfigurationSection fc;

    public MythicConfigImpl(String name, ConfigurationSection fc) {
        this.configName = name;
        this.fc = fc;
    }

    public MythicConfigImpl(String name, File file, ConfigurationSection fc) {
        this.configName = name;
        this.file = file;
        this.fc = fc;
    }

    public MythicConfigImpl(String name, File file) {
        this.configName = name;
        this.file = file;
        this.fc = new YamlConfiguration();
        this.fc.createSection(this.configName);
    }

    public void setKey(String key) {
        this.configName = key;
    }

    public String getKey() {
        return this.configName;
    }

    public ConfigurationSection getFileConfiguration() {
        return this.fc;
    }

    public boolean isSet(String field) {
        return this.fc.isSet(this.configName + "." + field);
    }

    public void set(String key, Object value) {
        this.fc.set(this.configName + "." + key, value);
    }

    public void load() {
        this.fc = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        throw new NotImplementedException();
    }

    public MythicConfig getNestedConfig(String field) {
        return new MythicConfigImpl(this.configName + "." + field, this.fc);
    }

    public Map<String, MythicConfig> getNestedConfigs(String key) {
        Map<String, MythicConfig> map = new HashMap();
        if (!this.isSet(key)) {
            return map;
        } else {
            Iterator var3 = this.getKeys(key).iterator();

            while (var3.hasNext()) {
                String k = (String) var3.next();
                map.put(k, new MythicConfigImpl(this.configName + "." + key + "." + k, this.fc));
            }

            return map;
        }
    }

    public String getString(String field) {
        String key = this.configName + "." + field;
        return this.fc.getString(key, this.fc.getString(key.toLowerCase()));
    }

    public String getString(String[] key) {
        return this.getString(key, (String) null);
    }

    public String getString(String field, String def) {
        String key = this.configName + "." + field;
        return this.fc.getString(key, this.fc.getString(key.toLowerCase(), def));
    }

    public String getString(String[] key, String def, String... args) {
        String s = null;
        String[] var5 = key;
        int var6 = key.length;

        int var7;
        String a;
        for (var7 = 0; var7 < var6; ++var7) {
            a = var5[var7];
            s = this.getString(this.fc.getString(a.toLowerCase(), (String) null));
            if (s != null) {
                return s;
            }
        }

        var5 = args;
        var6 = args.length;

        for (var7 = 0; var7 < var6; ++var7) {
            a = var5[var7];
            if (a != null) {
                return a;
            }
        }

        return def;
    }

    public PlaceholderString getPlaceholderString(String field) {
        String key = this.configName + "." + field;
        String s = this.fc.getString(key);
        return s == null ? null : PlaceholderString.of(s);
    }

    public PlaceholderString getPlaceholderString(String field, String def) {
        String key = this.configName + "." + field;
        String s = this.fc.getString(key, def);
        return s == null ? null : PlaceholderString.of(s);
    }

    public String getColorString(String field) {
        String key = this.configName + "." + field;
        String s = this.fc.getString(key);
        if (s != null) {
            s = ChatColor.translateAlternateColorCodes('&', s);
        }

        return s;
    }

    public String getColorString(String field, String def) {
        String key = this.configName + "." + field;
        String s = this.fc.getString(key, def);
        if (s != null) {
            s = ChatColor.translateAlternateColorCodes('&', s);
        }

        return s;
    }

    public boolean getBoolean(String field) {
        return this.fc.getBoolean(this.configName + "." + field);
    }

    public boolean getBoolean(String field, boolean def) {
        return this.fc.getBoolean(this.configName + "." + field, def);
    }

    public int getInteger(String field) {
        String key = this.configName + "." + field;
        return this.fc.getInt(key, this.fc.getInt(key.toLowerCase()));
    }

    public int getInteger(String field, int def) {
        String key = this.configName + "." + field;
        return this.fc.getInt(key, this.fc.getInt(key.toLowerCase(), def));
    }

    /**
     * @deprecated
     */
    @Deprecated
    public int getInt(String field) {
        return this.fc.getInt(this.configName + "." + field);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public int getInt(String field, int def) {
        return this.fc.getInt(this.configName + "." + field, def);
    }

    public double getDouble(String field) {
        return this.fc.getDouble(this.configName + "." + field);
    }

    public double getDouble(String field, double def) {
        return this.fc.getDouble(this.configName + "." + field, def);
    }

    public List<String> getStringList(String field) {
        return this.fc.getStringList(this.configName + "." + field);
    }

    public List<String> getColorStringList(String field) {
        List<String> list = this.fc.getStringList(this.configName + "." + field);
        List<String> parsed = new ArrayList();
        if (list != null) {
            Iterator var5 = list.iterator();

            while (var5.hasNext()) {
                String str = (String) var5.next();
                parsed.add(ChatColor.translateAlternateColorCodes('&', str));
            }
        }

        return parsed;
    }

    public List<PlaceholderString> getPlaceholderStringList(String field) {
        List<String> list = this.fc.getStringList(this.configName + "." + field);
        List<PlaceholderString> parsed = new ArrayList();
        if (list != null) {
            Iterator var5 = list.iterator();

            while (var5.hasNext()) {
                String str = (String) var5.next();
                parsed.add(PlaceholderString.of(str));
            }
        }

        return parsed;
    }

    public List<Map<?, ?>> getMapList(String field) {
        return this.fc.getMapList(this.configName + "." + field);
    }

    public List<?> getList(String field) {
        String key = this.configName + "." + field;
        if (this.fc.isSet(key)) {
            return this.fc.getList(key);
        } else {
            return this.fc.isSet(key.toLowerCase()) ? this.fc.getList(key.toLowerCase()) : null;
        }
    }

    public List<Byte> getByteList(String field) {
        String key = this.configName + "." + field;
        if (this.fc.isSet(key)) {
            return this.fc.getByteList(key);
        } else {
            return this.fc.isSet(key.toLowerCase()) ? this.fc.getByteList(key.toLowerCase()) : null;
        }
    }

    public ItemStack getItemStack(String field, String def) {
        String key = this.configName + "." + field;
        if (this.fc.isSet(key)) {
            return this.fc.getItemStack(key);
        } else if (this.fc.isSet(key.toLowerCase())) {
            return this.fc.getItemStack(key.toLowerCase());
        } else {
            try {
                return new ItemStack(Material.valueOf(def));
            } catch (Exception var5) {
                return null;
            }
        }
    }

    public boolean isConfigurationSection(String section) {
        return this.fc.isConfigurationSection(this.configName + "." + section);
    }

    public Set<String> getKeys(String section) {
        return this.fc.getConfigurationSection(this.configName + "." + section).getKeys(false);
    }

    public boolean isList(String section) {
        return this.fc.isList(this.configName + "." + section);
    }

    public PlaceholderInt getPlaceholderInt(String key, String def) {
        String s = this.getString(key, def);
        return s == null ? null : PlaceholderInt.of(s);
    }

    public PlaceholderInt getPlaceholderInt(String[] key, String def, String... args) {
        String s = this.getString(key, def, args);
        return s == null ? null : PlaceholderInt.of(s);
    }

    public PlaceholderDouble getPlaceholderDouble(String key, String def) {
        String s = this.getString(key, def);
        return s == null ? null : PlaceholderDouble.of(s);
    }

    public PlaceholderDouble getPlaceholderDouble(String[] key, String def, String... args) {
        String s = this.getString(key, def, args);
        return s == null ? null : PlaceholderDouble.of(s);
    }

    public void merge(MythicConfigImpl tmplConfig, List<String> keysToIgnore) {
        ConfigurationSection thisFile = this.fc;
        ConfigurationSection tmplFile = tmplConfig.fc;
        String thisMob = this.configName;
        String tmplMob = tmplConfig.configName;
        Iterator var7 = tmplConfig.getKeys("").iterator();

        while (var7.hasNext()) {
            String k = (String) var7.next();
            if (!keysToIgnore.contains(k)) {
                if (this.getStringList(k).size() > 0) {
                    List<String> currentStringList = this.getStringList(k);
                    currentStringList.addAll(tmplConfig.getStringList(k));
                    this.set(k, currentStringList);
                } else if (!this.isSet(k)) {
                    this.set(k, tmplFile.get(tmplMob + "." + k));
                } else if (thisFile.get(thisMob + "." + k) instanceof MemorySection) {
                    MemorySection memSec = (MemorySection) thisFile.get(thisMob + "." + k);
                    Set<String> templateMemSec = ((MemorySection) tmplFile.get(tmplMob + "." + k)).getKeys(false);
                    templateMemSec.forEach((m) -> {
                        if (!memSec.isSet(m)) {
                            memSec.set(k, tmplFile.get(tmplMob + "." + k + "." + m));
                        }

                    });
                }
            }
        }

    }

    public File getFile() {
        return this.file;
    }
}


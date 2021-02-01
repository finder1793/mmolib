package io.lumine.mythic.lib.comp.text.hexcolor;

import net.md_5.bungee.api.ChatColor;

public class SimpleColorParser implements ColorParser {

    @Override
    public String parseColorCodes(String format) {
        return ChatColor.translateAlternateColorCodes('&', format);
    }
}


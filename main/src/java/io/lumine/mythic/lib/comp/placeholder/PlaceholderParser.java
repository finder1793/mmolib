package io.lumine.mythic.lib.comp.placeholder;

import org.bukkit.OfflinePlayer;

public interface PlaceholderParser {
	String parse(OfflinePlayer player, String string);
}

package io.lumine.mythic.lib.comp.placeholder;

import io.lumine.mythic.lib.MythicLib;
import org.bukkit.OfflinePlayer;

public class DefaultPlaceholderParser implements PlaceholderParser {

	@Override
	public String parse(OfflinePlayer player, String string) {
		return MythicLib.plugin.parseColors(string);
	}
}

package io.lumine.mythic.lib.comp.anticheat;

import org.bukkit.entity.Player;

import java.util.Map;

public abstract class AntiCheatSupport {
	public abstract void disableAntiCheat(Player player, Map<CheatType, Integer> map);
}

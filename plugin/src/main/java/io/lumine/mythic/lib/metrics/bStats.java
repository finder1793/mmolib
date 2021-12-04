package io.lumine.mythic.lib.metrics;

import org.bstats.bukkit.Metrics;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.utils.logging.Log;

public class bStats {

	private Metrics metrics;
	
	public bStats(MythicLib plugin)	{
		try {
        	this.metrics = new Metrics(plugin);
            Log.info("Started up bStats Metrics");
        } catch (Exception e) {
            Log.error("Metrics: Failed to enable bStats Metrics stats.");
        }
	}
}

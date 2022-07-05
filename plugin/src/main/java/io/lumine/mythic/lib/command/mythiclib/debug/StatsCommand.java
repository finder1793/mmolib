package io.lumine.mythic.lib.command.mythiclib.debug;

import com.google.gson.JsonObject;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.StatInstance;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.command.api.CommandTreeNode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand extends CommandTreeNode {
    public StatsCommand(CommandTreeNode parent) {
        super(parent, "stats");
    }

    @Override
    public CommandResult execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You can only use this command as a player");
            return CommandResult.FAILURE;
        }

        Player player = (Player) sender;
        MMOPlayerData mmo = MMOPlayerData.get(player);
        JsonObject stats = new JsonObject();
        for (StatInstance stat : mmo.getStatMap().getInstances()) {
            JsonObject instance = new JsonObject();
            instance.addProperty("base", stat.getBase());
            instance.addProperty("total", stat.getTotal());
            JsonObject modifiers = new JsonObject();
            for (String key : stat.getKeys()) {
                JsonObject mod = new JsonObject();
                StatModifier modifier = stat.getModifier(key);
                mod.addProperty("value", modifier.getValue());
                mod.addProperty("type", modifier.getType().name());
                modifiers.add(key, mod);
            }
            instance.add("modifiers", modifiers);
            stats.add(stat.getStat(), instance);
        }
        sender.sendMessage(stats.toString());

        return CommandResult.SUCCESS;
    }
}

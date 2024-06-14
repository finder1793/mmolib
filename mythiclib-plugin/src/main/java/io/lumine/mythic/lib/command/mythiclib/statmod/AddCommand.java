package io.lumine.mythic.lib.command.mythiclib.statmod;

import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.api.stat.modifier.TemporaryStatModifier;
import io.lumine.mythic.lib.command.api.CommandTreeNode;
import io.lumine.mythic.lib.command.api.Parameter;
import io.lumine.mythic.lib.player.modifier.ModifierSource;
import io.lumine.mythic.lib.player.modifier.ModifierType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AddCommand extends CommandTreeNode {
    public AddCommand(@NotNull CommandTreeNode parent) {
        super(parent, "add");

        addParameter(Parameter.PLAYER);
        addParameter(Parameter.STAT);
        addParameter(Parameter.AMOUNT.key("value"));
        addParameter(Parameter.DURATION_TICKS.optional(true));
        addParameter(STAT_KEY);
    }

    public static final String DEFAULT_STAT_KEY = "default";

    public static final Parameter STAT_KEY = new Parameter("key", true, (tree, list) -> {
        list.add(DEFAULT_STAT_KEY);
        list.add("whatever");
        list.add("my_plugin_name");
    });

    @Override
    public CommandResult execute(CommandSender sender, String[] args) {
        if (args.length < 5) return CommandResult.THROW_USAGE;

        final @Nullable Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return CommandResult.FAILURE;
        }

        final String statName = UtilityMethods.enumName(args[3]);
        final MMOPlayerData playerData = MMOPlayerData.get(target);
        final ModifierType type = args[4].toCharArray()[args[4].length() - 1] == '%' ? ModifierType.RELATIVE : ModifierType.FLAT;
        final double value = Double.parseDouble(type == ModifierType.RELATIVE ? args[4].substring(0, args[4].length() - 1) : args[4]);
        final long duration = args.length > 5 ? Math.max(1, (long) Double.parseDouble(args[5])) : 0;
        final String key = args.length > 6 ? args[6] : DEFAULT_STAT_KEY;

        if (duration <= 0)
            new StatModifier(key, statName, value, type, EquipmentSlot.OTHER, ModifierSource.OTHER).register(playerData);
        else
            new TemporaryStatModifier(key, statName, value, type, EquipmentSlot.OTHER, ModifierSource.OTHER).register(playerData, duration);
        return CommandResult.SUCCESS;
    }
}

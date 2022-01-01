package io.lumine.mythic.lib.commands.mmolib;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.player.PlayerMetadata;
import io.lumine.mythic.lib.skill.SimpleSkill;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.trigger.TriggerMetadata;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MMOLibCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equals("cast")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("This command is only for players");
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /mythiclib cast <skill_id>");
                    return true;
                }

                String skillId = args[1].toUpperCase().replace("-", "_");
                SkillHandler<?> handler = null;
                try {
                    handler = MythicLib.plugin.getSkills().getHandlerOrThrow(skillId);
                } catch (RuntimeException exception) {
                    sender.sendMessage("Could not find skill with ID '" + skillId + "'");
                }

                SimpleSkill castable = new SimpleSkill(handler);
                PlayerMetadata caster = MMOPlayerData.get((Player) sender).getStatMap().cache(EquipmentSlot.MAIN_HAND);
                castable.cast(new TriggerMetadata(caster, null, null));
            }

            return true;
        }

        MythicLib.plugin.reload();
        sender.sendMessage(ChatColor.GREEN + "> MythicLib reloaded!");
        return true;
    }
}
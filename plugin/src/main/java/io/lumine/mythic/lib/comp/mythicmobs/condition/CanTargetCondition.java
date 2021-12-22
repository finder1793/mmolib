package io.lumine.mythic.lib.comp.mythicmobs.condition;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.UtilityMethods;
import io.lumine.mythic.lib.comp.target.InteractionType;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.util.annotations.MythicCondition;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

@MythicCondition(author = "Indyuce", name = "mmoCanTarget", aliases = {}, description = "Whether the caster can interact with the target")
public class CanTargetCondition extends SkillCondition implements IEntityComparisonCondition {
    private final InteractionType interaction;

    public CanTargetCondition(String line, MythicLineConfig mlc) {
        super(line);

        String interactionName = mlc.getString(new String[]{"interact", "type", "name", "interaction", "interactionType", "interactionName"}, "OFFENSE_SKILL");
        this.interaction = InteractionType.valueOf(UtilityMethods.enumName(interactionName));
    }

    @Override
    public boolean check(AbstractEntity entity, AbstractEntity target) {
        Validate.isTrue(entity instanceof AbstractPlayer, "First entity must be a player");
        return MythicLib.inst().getEntities().canTarget((Player) entity.getBukkitEntity(), target.getBukkitEntity(), interaction);
    }
}
package io.lumine.mythic.lib.script.targeter.entity;

import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.script.targeter.EntityTargeter;
import org.bukkit.entity.Entity;

import java.util.Arrays;
import java.util.List;

public class TargetTargeter implements EntityTargeter {

    @Override
    public List<Entity> findTargets(SkillMetadata meta) {
        return Arrays.asList(meta.getTargetEntity());
    }
}

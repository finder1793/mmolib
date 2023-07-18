package io.lumine.mythic.lib.script.mechanic.buff;

import io.lumine.mythic.lib.script.mechanic.type.TargetMechanic;
import io.lumine.mythic.lib.util.configobject.ConfigObject;
import io.lumine.mythic.lib.util.DoubleFormula;
import io.lumine.mythic.lib.player.cooldown.CooldownInfo;
import io.lumine.mythic.lib.skill.SkillMetadata;
import org.bukkit.entity.Entity;

import java.util.function.BiConsumer;

public class ReduceCooldownMechanic extends TargetMechanic {
    private final DoubleFormula value;
    private final ReductionType type;
    private final String cooldownPath;

    public ReduceCooldownMechanic(ConfigObject config) {
        super(config);

        config.validateKeys("value", "path");

        this.cooldownPath = config.getString("path");
        type = config.contains("type") ? ReductionType.valueOf(config.getString("type").toUpperCase()) : ReductionType.FLAT;
        this.value = new DoubleFormula(config.getString("value"));
    }

    @Override
    public void cast(SkillMetadata meta, Entity target) {

        // Check if it's on cooldown first
        CooldownInfo info = meta.getCaster().getData().getCooldownMap().getInfo(cooldownPath);
        if (info == null || info.hasEnded())
            return;

        type.apply(info, value.evaluate(meta));
    }

    public enum ReductionType {

        /**
         * See {@link CooldownInfo#reduceFlat(double)}
         */
        FLAT((info, value) -> info.reduceFlat(value)),

        /**
         * See {@link CooldownInfo#reduceInitialCooldown(double)}
         */
        INITIAL((info, value) -> info.reduceInitialCooldown(value)),

        /**
         * See {@link CooldownInfo#reduceRemainingCooldown(double)}
         */
        REMAINING((info, value) -> info.reduceRemainingCooldown(value));

        private final BiConsumer<CooldownInfo, Double> effect;

        ReductionType(BiConsumer<CooldownInfo, Double> effect) {
            this.effect = effect;
        }

        public void apply(CooldownInfo info, double value) {
            effect.accept(info, value);
        }
    }
}

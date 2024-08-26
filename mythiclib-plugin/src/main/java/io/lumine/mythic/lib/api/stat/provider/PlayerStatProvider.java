package io.lumine.mythic.lib.api.stat.provider;

import io.lumine.mythic.lib.api.player.EquipmentSlot;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.player.PlayerMetadata;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerStatProvider extends StatProvider {

    @NotNull
    default Player getPlayer() {
        return getData().getPlayer();
    }

    @NotNull
    PlayerMetadata cache(@NotNull EquipmentSlot castHand);

    @NotNull
    MMOPlayerData getData();

    @NotNull
    default LivingEntity getEntity() {
        return getData().getPlayer();
    }
}

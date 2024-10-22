package io.lumine.mythic.lib.player.cooldown;

public interface CooldownProvider extends CooldownReference {

    /**
     * @return Cooldown in millis
     */
    public long getCooldown();
}

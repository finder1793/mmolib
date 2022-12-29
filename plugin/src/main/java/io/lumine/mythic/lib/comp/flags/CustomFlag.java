package io.lumine.mythic.lib.comp.flags;

public enum CustomFlag {

    // MMOItems flags -
    MI_WEAPONS,
    MI_COMMANDS,
    MI_CONSUMABLES,
    MI_TOOLS,

    // Common flags
    MMO_ABILITIES,
    MMO_PVP,

    /**
     * @deprecated Useless
     */
    @Deprecated
    ABILITY_PVP;

    public String getPath() {
        return name().toLowerCase().replace("_", "-");
    }
}

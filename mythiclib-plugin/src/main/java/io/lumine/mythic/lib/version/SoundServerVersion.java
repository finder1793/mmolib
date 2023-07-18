package io.lumine.mythic.lib.version;

import io.lumine.mythic.lib.MythicLib;

import java.util.function.Predicate;

public enum SoundServerVersion {

    /**
     * Latest version is NEVER used because it should
     * be corresponding to the enum
     */
    V1_16(version -> version.isStrictlyHigher(1, 15), 1),

    /**
     * Corresponds to 1.13 all the way up to 1.15
     */
    V1_13(version -> version.isStrictlyHigher(1, 12) && version.isBelowOrEqual(1, 15), 0),

    /**
     * Legacy versions, should be removed since MI no longer supports 1.12
     */
    @Deprecated
    LEGACY(version -> version.isBelowOrEqual(1, 12), 2);

    /*
     * versions from 1.13 to
     */

    private final Predicate<ServerVersion> matches;

    /*
     * index of 0 corresponds to the enum NAME, anything above is offset by 1
     */
    private final int index;

    public static final SoundServerVersion FOUND = findVersion();

    /*
     * SoundServerVersion is used to store what sound enum constants the server
     * must use depending on the running spigot build; then we get the INDEX
     * which tells what string to use in the String... from the constructor
     */
    SoundServerVersion(Predicate<ServerVersion> matches, int index) {
        this.matches = matches;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean matches(ServerVersion version) {
        return matches.test(version);
    }

    private static SoundServerVersion findVersion() {
        ServerVersion server = MythicLib.plugin.getVersion();
        for (SoundServerVersion checked : values())
            if (checked.matches(server))
                return checked;

        // uses latest by default
        return V1_16;
    }
}

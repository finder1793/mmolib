package io.lumine.mythic.lib.version;

import io.lumine.mythic.lib.version.wrapper.VersionWrapper;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

public class ServerVersion {
    private final String revision;
    private final int revisionNumber;
    private final int[] integers;
    private final VersionWrapper versionWrapper;

    private static final int MAXIMUM_INDEX = 3;

    public ServerVersion(Class<?> clazz) throws Exception {
        revision = clazz.getPackage().getName().replace(".", ",").split(",")[3];
        revisionNumber = Integer.parseInt(revision.split("_")[2].replaceAll("[^0-9]", ""));

        final String[] bukkitSplit = Bukkit.getServer().getBukkitVersion().split("\\-")[0].split("\\.");
        integers = new int[bukkitSplit.length];
        for (int i = 0; i < integers.length; i++)
            integers[i] = Integer.parseInt(bukkitSplit[i]);

        versionWrapper = (VersionWrapper) Class.forName("io.lumine.mythic.lib.version.wrapper.VersionWrapper_" + revision.substring(1))
                .getDeclaredConstructor().newInstance();
    }

    /**
     * @param version Two integers. {1, 12} corresponds to 1.12.x. It's useless to
     *                provide more than 2 arguments
     * @return If server version is lower than provided version
     */
    public boolean isBelowOrEqual(int... version) {
        return !isStrictlyHigher(version);
    }

    /**
     * @param version At most three integers. [1, 20, 2] corresponds to 1.20.2
     * @return If server version is higher than (and not equal to) provided
     *         version
     */
    public boolean isStrictlyHigher(int... version) {
        Validate.isTrue(version.length >= 1 && version.length <= MAXIMUM_INDEX, "Provide at least 1 integer and at most " + MAXIMUM_INDEX);

        final int maxLength = Math.max(version.length, integers.length);
        for (int i = 0; i < Math.min(MAXIMUM_INDEX, maxLength); i++) {
            final int server = i >= integers.length ? 0 : integers[i];
            final int provided = i >= version.length ? 0 : version[i];
            if (server != provided) return server > provided;
        }

        return false;
    }

    public String getRevision() {
        return revision;
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public int[] getIntegers() {
        return integers;
    }

    @Deprecated
    public int[] toNumbers() {
        return integers;
    }

    public VersionWrapper getWrapper() {
        return versionWrapper;
    }

    @Override
    public String toString() {
        return revision;
    }
}

package io.lumine.mythic.lib.comp.adventure;

import io.lumine.mythic.lib.comp.adventure.argument.AdventureArgument;
import io.lumine.mythic.lib.comp.adventure.argument.AdventureArgumentQueue;
import io.lumine.mythic.lib.comp.adventure.tag.AdventureTag;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * mythiclib
 * 30/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
@ApiStatus.NonExtendable
public class AdventureParser {
    // TODO: make a builder]

    /* Constants */
    private static final String DEFAULT_TAG_REGEX = "(?i)(?<=<(%s)).*?(?=>)";

    private final List<AdventureTag> tags = new ArrayList<>();

    public AdventureParser() {
        // TODO: register all default tags
    }

    public @NotNull String parse(@NotNull final String src) {
        String cpy = src;
        for (AdventureTag tag : tags) {
            cpy = parseTag(cpy, tag, tag.name());
            for (String alias : tag.aliases())
                cpy = parseTag(cpy, tag, alias);
        }
        return minecraftColorization(cpy);
    }

    private @NotNull String parseTag(@NotNull final String src, @NotNull final AdventureTag tag, @NotNull final String tagIdentifier) {
        final Pattern pattern = Pattern.compile(String.format(DEFAULT_TAG_REGEX, tagIdentifier));
        Matcher matcher = pattern.matcher(src);

        String cpy = src;
        while (matcher.find()) {
            final String rawTag = matcher.group(1);
            final String rawArgs = matcher.group();
            final List<AdventureArgument> args = Arrays.stream(rawArgs.split(":"))
                    .map(AdventureArgument::new)
                    .collect(Collectors.toList());

            final String resolved = tag.resolver().resolve(rawTag, new AdventureArgumentQueue(args));
            cpy = cpy.replace("<%s%s>".formatted(rawTag, rawArgs), Objects.requireNonNullElse(resolved, ""));
            matcher = pattern.matcher(cpy);
        }
        return cpy;
    }

    private @NotNull String minecraftColorization(@NotNull final String src) {
        return ChatColor.translateAlternateColorCodes('&', src);
    }

    public void add(AdventureTag tag) {
        tags.add(tag);
    }

    public void remove(AdventureTag tag) {
        tags.remove(tag);
    }

    public List<AdventureTag> tags() {
        return tags;
    }
}

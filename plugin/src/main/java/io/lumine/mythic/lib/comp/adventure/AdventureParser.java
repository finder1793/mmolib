package io.lumine.mythic.lib.comp.adventure;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.comp.adventure.argument.AdventureArgument;
import io.lumine.mythic.lib.comp.adventure.argument.AdventureArgumentQueue;
import io.lumine.mythic.lib.comp.adventure.resolver.ContextTagResolver;
import io.lumine.mythic.lib.comp.adventure.tag.AdventureTag;
import io.lumine.mythic.lib.comp.adventure.tag.implementation.*;
import io.lumine.mythic.lib.comp.adventure.tag.implementation.decorations.*;
import io.lumine.mythic.lib.util.AdventureUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
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
    // TODO: make a builder

    /* Constants */
    private static final String DEFAULT_TAG_REGEX = "(?i)(?<=<(%s)).*?(?=>)";
    private static final Pattern TAG_REGEX = Pattern.compile("(?i)(?<=<).*?(?=>)");

    private final List<AdventureTag> tags = new ArrayList<>();
    private final Function<String, String> fallBackResolver;

    public AdventureParser(@NotNull Function<String, String> fallBackResolver) {
        this.fallBackResolver = fallBackResolver;
    }

    public AdventureParser() {
        this(s -> "<invalid>");

        // Context
        add(new GradientTag());
        add(new RainbowTag());

        // Normal
        add(new VanillaColorTag());
        add(new HexColorTag());
        add(new AdventureColorTag());
        add(new NewlineTag());
        add(new BoldTag());
        add(new ItalicTag());
        add(new ObfuscatedTag());
        add(new ResetTag());
        add(new StrikethroughTag());
        add(new UnderlineTag());
    }

    public @NotNull String parse(@NotNull final String src) {
        String cpy = src;
        for (AdventureTag tag : tags) {
            cpy = parseTag(cpy, tag, tag.name());
            for (String alias : tag.aliases())
                cpy = parseTag(cpy, tag, alias);
        }
        cpy = removeUnparsedAndUselessTags(cpy);
        return minecraftColorization(cpy);
    }

    public @NotNull CompletableFuture<String> parseAsync(@NotNull final String src) {
        return AdventureUtils.supplyAsync(() -> parse(src));
    }

    public @NotNull Collection<String> parse(@NotNull final Collection<String> src) {
        List<String> parsed = src.stream()
                .map(this::parse)
                .collect(Collectors.toList());
        List<String> finalList = new ArrayList<>();
        for (final String line : parsed) {
            if (!line.contains("\n")) {
                finalList.add(line);
                continue;
            }
            finalList.addAll(Arrays.asList(line.split("\n")));
        }
        return finalList;
    }

    public @NotNull CompletableFuture<Collection<String>> parseAsync(@NotNull Collection<String> src) {
        return AdventureUtils.supplyAsync(() -> parse(src));
    }

    private @NotNull String parseTag(@NotNull final String src, @NotNull final AdventureTag tag, @NotNull final String tagIdentifier) {
        final Pattern pattern = Pattern.compile(String.format(DEFAULT_TAG_REGEX, tagIdentifier));
        Matcher matcher = pattern.matcher(src);

        String cpy = src;
        int iterations = 0;
        while (matcher.find() && iterations++ < 50) {
            try {
                final String rawTag = matcher.group(1);
                final String rawArgs = matcher.group();
                final String original = "<%s%s>".formatted(rawTag, rawArgs);
                final AdventureArgumentQueue args = parseArguments(rawArgs);
                boolean hasContext = tag.resolver() instanceof ContextTagResolver;

                String context = hasContext ? getTagContent(cpy, rawTag, original) : null;
                String resolved = hasContext ?
                        ((ContextTagResolver) tag.resolver()).resolve(rawTag, args, context)
                        : tag.resolver().resolve(rawTag, args);
                cpy = cpy.replace(hasContext ?
                        Objects.requireNonNullElse("%s%s".formatted(original, context), fallBackResolver.apply(original))
                        : original, Objects.requireNonNullElse(resolved, fallBackResolver.apply(original)));
                matcher = pattern.matcher(cpy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cpy;
    }

    private @NotNull String getTagContent(@NotNull String src, @NotNull String tagName, @NotNull String tagIdentifier) {
        // Match closed tags
        final String closeTag = "</%s>".formatted(tagName);
        String content = StringUtils.substringBetween(src, tagIdentifier, closeTag);
        if (content != null) return content;

        String cpy = src.substring(src.indexOf(tagIdentifier) + tagIdentifier.length());
        int colorIndex = cpy.length();

        // Match colors tags as context end
        Matcher matcher = TAG_REGEX.matcher(cpy);
        int iterations = 0;
        while (matcher.find() && iterations++ < 10) {
            final String rawTag = matcher.group();
            String[] split = rawTag.split(":");
            Optional<AdventureTag> optTag = findByName(split[0]).filter(AdventureTag::color);
            if (optTag.isPresent()) {
                colorIndex = cpy.indexOf("<%s>".formatted(rawTag));
                break;
            }
            matcher = TAG_REGEX.matcher(cpy);
        }
        content = cpy.substring(0, colorIndex);
        return content;
    }

    private AdventureArgumentQueue parseArguments(@NotNull String rawArgs) {
        String[] unparsedArgs = rawArgs.split(":");
        if (unparsedArgs.length > 0 && unparsedArgs[0].isEmpty())
            unparsedArgs = Arrays.copyOfRange(unparsedArgs, 1, unparsedArgs.length);
        return new AdventureArgumentQueue(
                Arrays.stream(unparsedArgs)
                        .map(AdventureArgument::new)
                        .collect(Collectors.toList())
        );
    }

    private @NotNull String removeUnparsedAndUselessTags(@NotNull String src) {
        Matcher matcher = TAG_REGEX.matcher(src);
        int iterations = 0;
        while (matcher.find() && iterations++ < 50) {
            final String matched = matcher.group();
            final String original = "<%s>".formatted(matched);
            src = src.replace(original, matched.startsWith("/") ? "§r" : fallBackResolver.apply(original));
            matcher = TAG_REGEX.matcher(src);
        }
        return src;
    }

    private @NotNull String minecraftColorization(@NotNull final String src) {
        return ChatColor.translateAlternateColorCodes('&', src);
    }

    public void add(AdventureTag tag) {
        if (tag.backwardsCompatible() && MythicLib.plugin.getVersion().isBelowOrEqual(1, 15)) {
            MythicLib.plugin.getLogger().warning("The tag %s is not compatible with your server version.".formatted(tag.name()));
            return;
        }
        tags.add(tag);
    }

    @ApiStatus.Internal
    public void forceRegister(AdventureTag tag) {
        tags.add(tag);
    }

    public void remove(AdventureTag tag) {
        tags.remove(tag);
    }

    public Optional<AdventureTag> findByName(@NotNull String name) {
        return tags.stream()
                .filter(tag -> tag.name().equalsIgnoreCase(name) || tag.aliases().stream().anyMatch(s -> s.equalsIgnoreCase(name)))
                .findFirst();
    }

    public List<AdventureTag> tags() {
        return tags;
    }
}

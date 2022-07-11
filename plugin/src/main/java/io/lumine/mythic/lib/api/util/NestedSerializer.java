package io.lumine.mythic.lib.api.util;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class NestedSerializer {
    private NestedSerializer() {}

    public static final String CLICK = "click";
    public static final String HOVER = "hover";
    public static final String KEYBIND = "key";
    public static final String TRANSLATABLE = "lang";
    public static final String TRANSLATABLE_2 = "translate";
    public static final String TRANSLATABLE_3 = "tr";
    public static final String INSERTION = "insert";
    public static final String COLOR = "color";
    public static final String COLOR_2 = "colour";
    public static final String COLOR_3 = "c";
    public static final String HEX = "#";
    public static final String FONT = "font";
    public static final String UNDERLINED = "underlined";
    public static final String UNDERLINED_2 = "u";
    public static final String STRIKETHROUGH = "strikethrough";
    public static final String STRIKETHROUGH_2 = "st";
    public static final String OBFUSCATED = "obfuscated";
    public static final String OBFUSCATED_2 = "obf";
    public static final String ITALIC = "italic";
    public static final String ITALIC_2 = "em";
    public static final String ITALIC_3 = "i";
    public static final String BOLD = "bold";
    public static final String BOLD_2 = "b";
    public static final String RESET = "reset";
    public static final String RESET_2 = "r";
    public static final String PRE = "pre";
    public static final String RAINBOW = "rainbow";
    public static final String GRADIENT = "gradient";
    public static final String TAG_START = "<";
    public static final String TAG_END = ">";
    public static final String CLOSE_TAG = "/";
    public static final String SEPARATOR = ":";

    public static @NonNull String serialize(final @NonNull Component component) {
        List<ComponentNode> list = recursiveTraversal(new ComponentNode(component));
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            // The previous node, null if it doesn't exist.
            Style previous = ((i - 1) >= 0) ? list.get(i - 1).getStyle() : null;

            // The next node, null if it doesn't exist.
            Style next = ((i + 1) < list.size()) ? list.get(i + 1).getStyle() : null;

            // The current node.
            ComponentNode node = list.get(i);

            // Serialized string for the node.
            sb.append(serializeNode(node, previous, next));
        }
        return sb.toString();
    }

    // Uses recursion to get the children of each node and order them correctly.
    // Will also store the parent style, so it trickles down if the style option is absent.
    private static List<ComponentNode> recursiveTraversal(ComponentNode root) {
        List<ComponentNode> list = new LinkedList<>();
        list.add(root);
        if (!root.getComponent().children().isEmpty()) {
            for (Component child : root.getComponent().children())
                list.addAll(recursiveTraversal(new ComponentNode(child, root.getStyle())));
        }
        return list;
    }

    private static String serializeNode(@NonNull ComponentNode node, Style previous, Style next) {
        final StringBuilder sb = new StringBuilder();
        final Style style = node.getStyle();

        // # start tags

        // ## color
        if(style.color() != null && (previous == null || previous.color() != style.color())) {
            sb.append(startColor(Objects.requireNonNull(style.color())));
        }

        // ## decoration
        // ### only start if previous didn't start
        if(style.hasDecoration(TextDecoration.BOLD) && (previous == null || !previous.hasDecoration(TextDecoration.BOLD))) {
            sb.append(startTag(BOLD));
        }
        if(style.hasDecoration(TextDecoration.ITALIC) && (previous == null || !previous.hasDecoration(TextDecoration.ITALIC))) {
            sb.append(startTag(ITALIC));
        }
        if(style.hasDecoration(TextDecoration.OBFUSCATED) && (previous == null || !previous.hasDecoration(TextDecoration.OBFUSCATED))) {
            sb.append(startTag(OBFUSCATED));
        }
        if(style.hasDecoration(TextDecoration.STRIKETHROUGH) && (previous == null || !previous.hasDecoration(TextDecoration.STRIKETHROUGH))) {
            sb.append(startTag(STRIKETHROUGH));
        }
        if(style.hasDecoration(TextDecoration.UNDERLINED) && (previous == null || !previous.hasDecoration(TextDecoration.UNDERLINED))) {
            sb.append(startTag(UNDERLINED));
        }

        // ## hover
        // ### only start if previous didn't start the same one
        final HoverEvent<?> hov = style.hoverEvent();
        if(hov != null && (previous == null || areDifferent(hov, previous.hoverEvent()))) {
            if(hov.value() instanceof Component) {
                sb.append(startTag(String.format("%s" + SEPARATOR + "%s" + SEPARATOR + "\"%s\"", HOVER, HoverEvent.Action.NAMES.key(hov.action()), serialize((Component) hov.value()))));
            } else {
                // TODO what to do here? warning? exception? toggleable strict mode?
            }
        }

        // ## click
        // ### only start if previous didn't start the same one
        final ClickEvent click = style.clickEvent();
        if(click != null && (previous == null || areDifferent(click, previous.clickEvent()))) {
            sb.append(startTag(String.format("%s" + SEPARATOR + "%s" + SEPARATOR + "\"%s\"", CLICK, ClickEvent.Action.NAMES.key(click.action()), click.value())));
        }

        // ## insertion
        // ### only start if previous didn't start the same one
        final String insert = style.insertion();
        if(insert != null && (previous == null || !insert.equals(previous.insertion()))) {
            sb.append(startTag(INSERTION + SEPARATOR + insert));
        }

        // ## font
        final Key font = style.font();
        if(font != null && (previous == null || !font.equals(previous.font()))) {
            sb.append(startTag(FONT + SEPARATOR + font.asString()));
        }

        // # append text
        if(node.getComponent() instanceof TextComponent) {
            sb.append(((TextComponent) node.getComponent()).content());
        } else {
            handleDifferentComponent(node.getComponent(), sb);
        }

        // # end tags

        // ## color
        if(next != null && style.color() != null && next.color() != style.color()) {
            sb.append(endColor(Objects.requireNonNull(style.color())));
        }

        // ## decoration
        // ### only end decoration if next tag is different
        if(next != null) {
            if(style.hasDecoration(TextDecoration.BOLD) && !next.hasDecoration(TextDecoration.BOLD)) {
                sb.append(endTag(BOLD));
            }
            if(style.hasDecoration(TextDecoration.ITALIC) && !next.hasDecoration(TextDecoration.ITALIC)) {
                sb.append(endTag(ITALIC));
            }
            if(style.hasDecoration(TextDecoration.OBFUSCATED) && !next.hasDecoration(TextDecoration.OBFUSCATED)) {
                sb.append(endTag(OBFUSCATED));
            }
            if(style.hasDecoration(TextDecoration.STRIKETHROUGH) && !next.hasDecoration(TextDecoration.STRIKETHROUGH)) {
                sb.append(endTag(STRIKETHROUGH));
            }
            if(style.hasDecoration(TextDecoration.UNDERLINED) && !next.hasDecoration(TextDecoration.UNDERLINED)) {
                sb.append(endTag(UNDERLINED));
            }
        }

        // ## hover
        // ### only end hover if next tag is different
        if(next != null && style.hoverEvent() != null) {
            if(areDifferent(Objects.requireNonNull(style.hoverEvent()), next.hoverEvent())) {
                sb.append(endTag(HOVER));
            }
        }

        // ## click
        // ### only end click if next tag is different
        if(next != null && style.clickEvent() != null) {
            if(areDifferent(Objects.requireNonNull(style.clickEvent()), next.clickEvent())) {
                sb.append(endTag(CLICK));
            }
        }

        // ## insertion
        // ### only end insertion if next tag is different
        if(next != null && style.insertion() != null) {
            if(!Objects.equals(style.insertion(), next.insertion())) {
                sb.append(endTag(INSERTION));
            }
        }

        // ## font
        // ### only end insertion if next tag is different
        if(next != null && style.font() != null) {
            if(!Objects.equals(style.font(), next.font())) {
                sb.append(endTag(FONT));
            }
        }
        return sb.toString();
    }

    private static boolean areDifferent(final @NonNull ClickEvent c1, final ClickEvent c2) {
        if(c2 == null) return true;
        return !c1.equals(c2) && (!c1.action().equals(c2.action()) || !c1.value().equals(c2.value()));
    }

    private static boolean areDifferent(final @NonNull HoverEvent<?> h1, final HoverEvent<?> h2) {
        if(h2 == null) return true;
        return !h1.equals(h2) && (!h1.action().equals(h2.action())); // TODO also compare value
    }

    private static @NonNull String startColor(final @NonNull TextColor color) {
        if(color instanceof NamedTextColor) {
            return startTag(Objects.requireNonNull(NamedTextColor.NAMES.key((NamedTextColor) color)));
        } else {
            return startTag(COLOR + SEPARATOR + color.asHexString());
        }
    }

    private static @NonNull String endColor(final @NonNull TextColor color) {
        if(color instanceof NamedTextColor) {
            return endTag(Objects.requireNonNull(NamedTextColor.NAMES.key((NamedTextColor) color)));
        } else {
            return endTag(COLOR + SEPARATOR + color.asHexString());
        }
    }

    private static @NonNull String startTag(final @NonNull String content) {
        return TAG_START + content + TAG_END;
    }

    private static @NonNull String endTag(final @NonNull String content) {
        return TAG_START + CLOSE_TAG + content + TAG_END;
    }

    private static void handleDifferentComponent(final @NonNull Component component, final @NonNull StringBuilder sb) {
        if(component instanceof KeybindComponent) {
            sb.append(startTag(KEYBIND + SEPARATOR + ((KeybindComponent) component).keybind()));
        } else if(component instanceof TranslatableComponent) {
            sb.append(startTag(TRANSLATABLE + SEPARATOR + ((TranslatableComponent) component).key()));
        }
    }

    private static class ComponentNode {
        private final Component component;
        private final Style style;

        public ComponentNode(@NonNull Component component) {
            this(component, null);
        }

        public ComponentNode(@NonNull Component component, Style parent) {
            this.component = component;
            this.style = (parent == null) ? component.style() : component.style().merge(parent, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
        }

        public Component getComponent() {
            return component;
        }

        public Style getStyle() {
            return style;
        }
    }
}


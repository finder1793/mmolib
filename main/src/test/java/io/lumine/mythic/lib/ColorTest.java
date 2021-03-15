package io.lumine.mythic.lib;

import io.lumine.mythic.lib.api.util.MiniMessageSerializer;
import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.adventure.text.ComponentLike;
import io.lumine.utils.adventure.text.TextComponent;
import io.lumine.utils.adventure.text.format.NamedTextColor;
import io.lumine.utils.adventure.text.format.TextDecoration;
import io.lumine.utils.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColorTest {

    // The problem is the white tag does not end.
    @Test
    public void testBug() {
        //final String text = "&8&m--&f&l &nGeneral&8 &m--";
        final String text = "<gray><strikethrough>--</gray></strikethrough> <white><bold><underlined>General</white></bold></underlined> <gray><strikethrough>--";
        //TextComponent deserialize = LegacyComponentSerializer.legacyAmpersand().deserialize(text);
        Component parse = MiniMessage.get().parse(text);
        final TextComponent.Builder builder = Component.text().content("--").color(NamedTextColor.GRAY).decorate(TextDecoration.STRIKETHROUGH)
                .append(Component.space())
                .append(Component.text("General", NamedTextColor.WHITE, TextDecoration.BOLD, TextDecoration.UNDERLINED))
                .append(Component.space())
                .append(Component.text("--", NamedTextColor.GRAY, TextDecoration.STRIKETHROUGH));
        test(builder, text);
    }

     private void test(final @NonNull ComponentLike builder, final @NonNull String expected) {
        final String string = MiniMessageSerializer.serialize(builder.asComponent());
        assertEquals(expected, string);
    }
}

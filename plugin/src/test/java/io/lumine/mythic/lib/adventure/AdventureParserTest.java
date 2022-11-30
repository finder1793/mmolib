package io.lumine.mythic.lib.adventure;

import io.lumine.mythic.lib.comp.adventure.AdventureParser;
import io.lumine.mythic.lib.comp.adventure.tag.implementation.*;
import io.lumine.mythic.lib.comp.adventure.tag.implementation.decorations.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

/**
 * mythiclib
 * 30/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
public class AdventureParserTest {

    static AdventureParser parser;

    @BeforeEach
    void setUp() {
        parser = new AdventureParser(s -> "");
    }

    @Test
    void testHexColorTag() {
        // Add tag parser
        final HexColorTag tag = new HexColorTag();
        parser.forceRegister(tag);

        // Invalid tag
        final String i1 = "This is a #FF0000red#FFFFFF text";
        Assertions.assertEquals(i1, parser.parse(i1));

        // Valid single tag
        final String i2 = "<#FFFFFF>This is a white text";
        final String i2Expected = "§x§F§F§F§F§F§FThis is a white text";
        Assertions.assertEquals(i2Expected, parser.parse(i2));

        // Valid multiple tags
        final String i3 = "<#FFFFFF>This is a <#FF0000>red<#FFFFFF> text";
        final String i3Expected = "§x§F§F§F§F§F§FThis is a §x§F§F§0§0§0§0red§x§F§F§F§F§F§F text";
        Assertions.assertEquals(i3Expected, parser.parse(i3));

        // Invalid tags
        final String i4 = "<#FFFF>This is a <#FF000>red<#F1FFF> text";
        final String i4Expected = "This is a red text";
        Assertions.assertEquals(i4Expected, parser.parse(i4));

        // Valid HEX tag
        final String i5 = "<HEXFF0000>This is a red text";
        final String i5Expected = "§x§F§F§0§0§0§0This is a red text";
        Assertions.assertEquals(i5Expected, parser.parse(i5));

        // Invalid HEX tag
        final String i6 = "<HEXFF000>This is a red text";
        final String i6Expected = "This is a red text";
        Assertions.assertEquals(i6Expected, parser.parse(i6));

        // Remove tag parser
        parser.remove(tag);
    }

    @Test
    void testVanillaColors() {
        // Add tag parser
        final VanillaColorTag tag = new VanillaColorTag();
        parser.forceRegister(tag);

        // Valid tag
        final String i1 = "<RED>This is a red text";
        final String i1Expected = "§cThis is a red text";
        Assertions.assertEquals(i1Expected, parser.parse(i1));

        // Invalid tag
        final String i2 = "This is a red text";
        final String i2Expected = "This is a red text";
        Assertions.assertEquals(i2Expected, parser.parse(i2));

        // Multiple valid tags
        final String i3 = "<RED>This is a <GREEN>green<RED> text";
        final String i3Expected = "§cThis is a §agreen§c text";
        Assertions.assertEquals(i3Expected, parser.parse(i3));

        // Multiple invalid tags
        final String i4 = "This is a <GREN>green<RE> text";
        final String i4Expected = "This is a green text";
        Assertions.assertEquals(i4Expected, parser.parse(i4));

        // Decorations
        final String i5 = "<RED>This is a <GREEN>green<RED> <BOLD>bold<RED> text";
        final String i5Expected = "§cThis is a §agreen§c bold§c text";
        Assertions.assertEquals(i5Expected, parser.parse(i5));

        // Remove tag parser
        parser.remove(tag);
    }

    @Test
    void testObfuscatedTag() {
        // Add tag
        ObfuscatedTag tag = new ObfuscatedTag();
        parser.forceRegister(tag);

        // Valid tag
        final String i1 = "<obfuscated>This is a obfuscated text";
        final String i1Expected = "§kThis is a obfuscated text";
        Assertions.assertEquals(i1Expected, parser.parse(i1));

        // Alias tag
        final String i2 = "<OBF>This is a obfuscated text";
        final String i2Expected = "§kThis is a obfuscated text";
        Assertions.assertEquals(i2Expected, parser.parse(i2));

        // Remove tag
        parser.remove(tag);
    }

    @Test
    void testBoldTag() {
        // Add tag
        BoldTag tag = new BoldTag();
        parser.forceRegister(tag);

        // Valid tag
        final String i1 = "<bold>This is a bold text";
        final String i1Expected = "§lThis is a bold text";
        Assertions.assertEquals(i1Expected, parser.parse(i1));

        // Alias tag
        final String i2 = "<B>This is a bold text";
        final String i2Expected = "§lThis is a bold text";
        Assertions.assertEquals(i2Expected, parser.parse(i2));

        // Remove tag
        parser.remove(tag);
    }

    @Test
    void testItalicTag() {
        // Add tag
        ItalicTag tag = new ItalicTag();
        parser.forceRegister(tag);

        // Valid tag
        final String i1 = "<italic>This is a italic text";
        final String i1Expected = "§oThis is a italic text";
        Assertions.assertEquals(i1Expected, parser.parse(i1));

        // Alias tag
        final String i2 = "<I>This is a italic text";
        final String i2Expected = "§oThis is a italic text";
        Assertions.assertEquals(i2Expected, parser.parse(i2));

        // Remove tag
        parser.remove(tag);
    }

    @Test
    public void testResetTag() {
        // Add tag
        ResetTag tag = new ResetTag();
        parser.forceRegister(tag);

        // Valid tag
        final String i1 = "<reset>This is a reset text";
        final String i1Expected = "§rThis is a reset text";
        Assertions.assertEquals(i1Expected, parser.parse(i1));

        // Alias tag
        final String i2 = "<R>This is a reset text";
        final String i2Expected = "§rThis is a reset text";
        Assertions.assertEquals(i2Expected, parser.parse(i2));

        // Remove tag
        parser.remove(tag);
    }

    @Test
    public void testUnderlineTag() {
        // Add tag
        UnderlineTag tag = new UnderlineTag();
        parser.forceRegister(tag);

        // Valid tag
        final String i1 = "<underline>This is a underline text";
        final String i1Expected = "§nThis is a underline text";
        Assertions.assertEquals(i1Expected, parser.parse(i1));

        // Alias tag
        final String i2 = "<U>This is a underline text";
        final String i2Expected = "§nThis is a underline text";
        Assertions.assertEquals(i2Expected, parser.parse(i2));

        // Remove tag
        parser.remove(tag);
    }

    @Test
    public void testStrikethroughTag() {
        // Add tag
        StrikethroughTag tag = new StrikethroughTag();
        parser.forceRegister(tag);

        // Valid tag
        final String i1 = "<strikethrough>This is a strikethrough text";
        final String i1Expected = "§mThis is a strikethrough text";
        Assertions.assertEquals(i1Expected, parser.parse(i1));

        // Alias tag
        final String i2 = "<ST>This is a strikethrough text";
        final String i2Expected = "§mThis is a strikethrough text";
        Assertions.assertEquals(i2Expected, parser.parse(i2));

        // Remove tag
        parser.remove(tag);
    }

    @Test
    public void testAdventureColors() {
        // Add tag
        AdventureColorTag tag = new AdventureColorTag();
        parser.forceRegister(tag);

        // Valid tag
        final String i1 = "<color:red>This is a red text";
        final String i1Expected = "§cThis is a red text";
        Assertions.assertEquals(i1Expected, parser.parse(i1));

        // Invalid tag
        final String i2 = "<color:invalid>This is a red text";
        final String i2Expected = "This is a red text";
        Assertions.assertEquals(i2Expected, parser.parse(i2));

        // Hex tag
        final String i3 = "<color:#FF0000>This is a red text";
        final String i3Expected = "§x§F§F§0§0§0§0This is a red text";
        Assertions.assertEquals(i3Expected, parser.parse(i3));

        // Hex tag without #
        final String i4 = "<color:FF0000>This is a red text";
        final String i4Expected = "§x§F§F§0§0§0§0This is a red text";
        Assertions.assertEquals(i4Expected, parser.parse(i4));

        // Remove tag
        parser.remove(tag);
    }

    @Test
    public void testClosedTag() {
        // Add tag
        AdventureColorTag tag = new AdventureColorTag();
        parser.forceRegister(tag);

        // Valid tag
        final String i1 = "<color:red>This is a red text</color>Hey";
        final String i1Expected = "§cThis is a red text§rHey";
        Assertions.assertEquals(i1Expected, parser.parse(i1));

        // Multiple tags
        final String i2 = "<color:red>This is a red text</color><color:blue>This is a blue text</color>";
        final String i2Expected = "§cThis is a red text§r§9This is a blue text§r";
        Assertions.assertEquals(i2Expected, parser.parse(i2));

        // Remove tag
        parser.remove(tag);
    }

    @Test
    public void testNewline() {
        // Add tag
        NewlineTag tag = new NewlineTag();
        parser.forceRegister(tag);

        // Valid tag
        Collection<String> i1 = List.of("Hey!", "What a good<newline><newline>day!", "Init?", "Yeah!<newline>It a pretty day<newline>We could go out, nah?");
        Collection<String> result = parser.parse(i1);
        Assertions.assertEquals(result.size(), 8);

        // Alias tag
        Collection<String> i2 = List.of("Hey!", "What a good<br><br>day!", "Init?", "Yeah!<br>It a pretty day<br>We could go out, nah?");
        Collection<String> result2 = parser.parse(i2);
        Assertions.assertEquals(result2.size(), 8);

        // Remove tag
        parser.remove(tag);
    }

    @Test
    public void testGradient() {
        // Add tag
        GradientTag tag = new GradientTag();
        parser.forceRegister(tag);

        // Default gradient
        final String i1 = "<gradient>This is a gradient text</gradient>";
        final String i1Expected = "§x§f§f§f§f§f§fT§x§f§3§f§3§f§3h§x§e§8§e§8§e§8i§x§d§c§d§c§d§cs§x§d§1§d§1§d§1 §x§c§5§c§5§c§5i§x§b§9§b§9§b§9s§x§a§e§a§e§a§e §x§a§2§a§2§a§2a§x§9§7§9§7§9§7 §x§8§b§8§b§8§bg§x§7§f§7§f§7§fr§x§7§4§7§4§7§4a§x§6§8§6§8§6§8d§x§5§d§5§d§5§di§x§5§1§5§1§5§1e§x§4§6§4§6§4§6n§x§3§a§3§a§3§at§x§2§e§2§e§2§e §x§2§3§2§3§2§3t§x§1§7§1§7§1§7e§x§0§c§0§c§0§cx§x§0§0§0§0§0§0t§r";
        Assertions.assertEquals(i1Expected, parser.parse(i1));

        // 2 color gradient
        final String i2 = "<gradient:red,blue>This is a gradient text</gradient>";
        final String i2Expected = "§x§f§f§f§f§f§fT§x§f§3§f§3§f§3h§x§e§8§e§8§e§8i§x§d§c§d§c§d§cs§x§d§1§d§1§d§1 §x§c§5§c§5§c§5i§x§b§9§b§9§b§9s§x§a§e§a§e§a§e §x§a§2§a§2§a§2a§x§9§7§9§7§9§7 §x§8§b§8§b§8§bg§x§7§f§7§f§7§fr§x§7§4§7§4§7§4a§x§6§8§6§8§6§8d§x§5§d§5§d§5§di§x§5§1§5§1§5§1e§x§4§6§4§6§4§6n§x§3§a§3§a§3§at§x§2§e§2§e§2§e §x§2§3§2§3§2§3t§x§1§7§1§7§1§7e§x§0§c§0§c§0§cx§x§0§0§0§0§0§0t§r";
        Assertions.assertEquals(i2Expected, parser.parse(i2));

        // 3 color gradient
        final String i3 = "<gradient:red,blue,green>This is a gradient text</gradient>";
        final String i3Expected = "§x§f§f§f§f§f§fT§x§f§3§f§3§f§3h§x§e§8§e§8§e§8i§x§d§c§d§c§d§cs§x§d§1§d§1§d§1 §x§c§5§c§5§c§5i§x§b§9§b§9§b§9s§x§a§e§a§e§a§e §x§a§2§a§2§a§2a§x§9§7§9§7§9§7 §x§8§b§8§b§8§bg§x§7§f§7§f§7§fr§x§7§4§7§4§7§4a§x§6§8§6§8§6§8d§x§5§d§5§d§5§di§x§5§1§5§1§5§1e§x§4§6§4§6§4§6n§x§3§a§3§a§3§at§x§2§e§2§e§2§e §x§2§3§2§3§2§3t§x§1§7§1§7§1§7e§x§0§c§0§c§0§cx§x§0§0§0§0§0§0t§r";
        Assertions.assertEquals(i3Expected, parser.parse(i3));

        // 4 color gradient
        final String i4 = "<gradient:red,blue,green,yellow>This is a gradient text</gradient>";
        final String i4Expected = "§x§f§f§f§f§f§fT§x§f§3§f§3§f§3h§x§e§8§e§8§e§8i§x§d§c§d§c§d§cs§x§d§1§d§1§d§1 §x§c§5§c§5§c§5i§x§b§9§b§9§b§9s§x§a§e§a§e§a§e §x§a§2§a§2§a§2a§x§9§7§9§7§9§7 §x§8§b§8§b§8§bg§x§7§f§7§f§7§fr§x§7§4§7§4§7§4a§x§6§8§6§8§6§8d§x§5§d§5§d§5§di§x§5§1§5§1§5§1e§x§4§6§4§6§4§6n§x§3§a§3§a§3§at§x§2§e§2§e§2§e §x§2§3§2§3§2§3t§x§1§7§1§7§1§7e§x§0§c§0§c§0§cx§x§0§0§0§0§0§0t§r";
        Assertions.assertEquals(i4Expected, parser.parse(i4));

        // Remove tag
        parser.remove(tag);
    }

    @AfterAll
    static void afterAll() {
        parser = null;
    }
}

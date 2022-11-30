package io.lumine.mythic.lib.adventure;

import io.lumine.mythic.lib.comp.adventure.AdventureParser;
import io.lumine.mythic.lib.comp.adventure.tag.implementation.VanillaColorTag;
import io.lumine.mythic.lib.comp.adventure.tag.implementation.HexColorTag;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        parser.add(tag);

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
        parser.add(tag);

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

    @AfterAll
    static void afterAll() {
        parser = null;
    }
}

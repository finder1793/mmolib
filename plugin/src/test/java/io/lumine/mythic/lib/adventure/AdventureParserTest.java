package io.lumine.mythic.lib.adventure;

import io.lumine.mythic.lib.comp.adventure.AdventureParser;
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
        parser = new AdventureParser();
    }

    @Test
    void testHexColorTag() {
        // Add tag parser
        final HexColorTag tag = new HexColorTag();
        parser.add(tag);

        // Invalid tag
        final String i1 = "This is a #FF0000red#FFFFFF text";
        Assertions.assertEquals(parser.parse(i1), i1);
        Assertions.assertEquals(parser.parse(i1).length(), i1.length());

        // Valid single tag
        final String i2 = "<#FFFFFF>This is a white text";
        final String i2Expected = "§x§F§F§F§F§F§FThis is a white text";
        Assertions.assertEquals(parser.parse(i2), i2Expected);
        Assertions.assertEquals(parser.parse(i2).length(), i2Expected.length());

        // Valid multiple tags
        final String i3 = "<#FFFFFF>This is a <#FF0000>red<#FFFFFF> text";
        final String i3Expected = "§x§F§F§F§F§F§FThis is a §x§F§F§0§0§0§0red§x§F§F§F§F§F§F text";
        Assertions.assertEquals(parser.parse(i3), i3Expected);
        Assertions.assertEquals(parser.parse(i3).length(), i3Expected.length());

        // Invalid tags
        final String i4 = "<#FFFF>This is a <#FF000>red<#F1FFF> text";
        final String i4Expected = "<#FFFF>This is a <#FF000>red<#F1FFF> text";
        Assertions.assertEquals(parser.parse(i4), i4Expected);
        Assertions.assertEquals(parser.parse(i4).length(), i4Expected.length());

        // Valid HEX tag
        final String i5 = "<HEXFF0000>This is a red text";
        final String i5Expected = "§x§F§F§0§0§0§0This is a red text";
        Assertions.assertEquals(parser.parse(i5), i5Expected);
        Assertions.assertEquals(parser.parse(i5).length(), i5Expected.length());

        // Invalid HEX tag
        final String i6 = "<HEXFF000>This is a red text";
        final String i6Expected = "<HEXFF000>This is a red text";
        Assertions.assertEquals(parser.parse(i6), i6Expected);
        Assertions.assertEquals(parser.parse(i6).length(), i6Expected.length());

        // Remove tag parser
        parser.remove(tag);
    }

    @AfterAll
    static void afterAll() {
        parser = null;
    }
}

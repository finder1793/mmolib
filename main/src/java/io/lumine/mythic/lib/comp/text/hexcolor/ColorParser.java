package io.lumine.mythic.lib.comp.text.hexcolor;

public interface ColorParser {

    /**
     * @param format
     *            The string from which to parse color codes
     * @return String with parsed color codes
     */
    String parseColorCodes(String format);
}

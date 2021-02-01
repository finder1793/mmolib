package io.lumine.mythic.lib.comp.text.component.font;

/**
 * The format goes as follows:
 *
 * <id=key>text
 * Id = identifier for the component. Ex. f or font
 * Key = info for the component. Ex. Font Name
 * Text = string that follows the format.
 *
 * @author Ehh
 */
public class ComponentData {
    public String key,text;

    public ComponentData(String key, String text) {
        this.text = text;
        this.key = key;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

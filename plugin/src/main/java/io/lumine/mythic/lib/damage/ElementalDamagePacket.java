package io.lumine.mythic.lib.damage;

import io.lumine.mythic.lib.element.Element;
import org.jetbrains.annotations.NotNull;

public

class ElementalDamagePacket extends DamagePacket {
    private Element element;

    @NotNull public Element getElement() { return element; }
    public void setElement(@NotNull Element element) { this.element = element; }

    public ElementalDamagePacket(double value, @NotNull Element element, @NotNull DamageType... types) {
        super(value, types);

        this.element = element;
    }
}
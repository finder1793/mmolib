package io.lumine.mythic.lib.element;

import io.lumine.mythic.lib.damage.DamagePacket;
import io.lumine.mythic.lib.damage.DamageType;
import io.lumine.mythic.lib.element.Element;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ElementalDamagePacket extends DamagePacket {
    @NotNull
    private Element element;

    public ElementalDamagePacket(double value, @NotNull Element element, @NotNull DamageType... types) {
        super(value, types);

        this.element = Objects.requireNonNull(element, "Element cannot be null");
    }

    @NotNull
    public Element getElement() {
        return element;
    }

    public void setElement(@NotNull Element element) {
        this.element = Objects.requireNonNull(element, "Element cannot be null");
    }
}
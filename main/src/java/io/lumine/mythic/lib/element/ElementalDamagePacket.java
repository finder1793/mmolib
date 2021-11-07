package io.lumine.mythic.lib.element;

import io.lumine.mythic.lib.damage.DamagePacket;
import io.lumine.mythic.lib.damage.DamageType;

import java.util.Set;

public class ElementalDamagePacket extends DamagePacket {
    private final Element element;

    @Deprecated
    public ElementalDamagePacket(double value, Element element, Set<DamageType> types) {
        this(value, element, types.toArray(new DamageType[0]));
    }

    public ElementalDamagePacket(double value, Element element, DamageType... types) {
        super(value, types);

        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}

package io.lumine.mythic.lib.util.gson;

import com.google.gson.*;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.util.annotation.BackwardsCompatibility;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;

import java.lang.reflect.Type;
import java.util.Objects;

public class AttributeModifierAdapter implements JsonSerializer<AttributeModifier>, JsonDeserializer<AttributeModifier> {

    @Override
    public AttributeModifier deserialize(JsonElement element, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject object = element.getAsJsonObject();

        @BackwardsCompatibility(version = "1.21") final NamespacedKey key;
        @BackwardsCompatibility(version = "1.21") final EquipmentSlotGroup group;

        if (object.has("Name")) {
            // UUID, Name, Slot
            key = new NamespacedKey(MythicLib.plugin, object.get("UUID").getAsString() + "_" + object.get("Name"));
            group = object.has("Slot") ? EquipmentSlot.valueOf(object.get("Slot").getAsString()).getGroup() : EquipmentSlotGroup.ANY;
        } else {
            // Key, Group
            key = NamespacedKey.fromString(object.get("Key").getAsString());
            group = Objects.requireNonNullElse(EquipmentSlotGroup.getByName(object.get("Group").getAsString()), EquipmentSlotGroup.ANY);
        }

        return new AttributeModifier(key, object.get("Amount").getAsDouble(), AttributeModifier.Operation.valueOf(object.get("Operation").getAsString()), group);
    }

    @Override
    public JsonElement serialize(AttributeModifier modifier, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject object = new JsonObject();
        object.addProperty("Key", modifier.getKey().toString());
        object.addProperty("Amount", modifier.getAmount());
        object.addProperty("Operation", modifier.getOperation().name());
        object.addProperty("Group", modifier.getSlotGroup().toString());
        return object;
    }
}
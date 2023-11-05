package io.lumine.mythic.lib.manager;

import io.lumine.mythic.lib.api.event.fake.FakeBlockBreakEvent;
import io.lumine.mythic.lib.api.event.fake.FakeEntityDamageByEntityEvent;
import io.lumine.mythic.lib.api.event.fake.FakeEventCaller;
import org.apache.commons.lang.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeEventManager {
    private final Map<Class, List<FakeEventCaller>> callers = new HashMap<>();

    public FakeEventManager() {

        /*
         * This will add compatibility with many plugins with the need
         * for code-specific hooks. This classifies damage events with 0
         * damage as fake. These will be instantly ignored!
         */
        registerFakeEventCaller(EntityDamageEvent.class, damageEvent -> damageEvent.getDamage() == 0);

        // MythicLib fake events
        registerFakeEventCaller(EntityDamageEvent.class, damageEvent -> damageEvent instanceof FakeEntityDamageByEntityEvent);
        registerFakeEventCaller(BlockBreakEvent.class, event -> event instanceof FakeBlockBreakEvent);
    }

    public <E extends Event> void registerFakeEventCaller(Class<E> eventClass, @NotNull FakeEventCaller<E> caller) {
        Validate.notNull(eventClass, "Event class cannot be null");
        Validate.notNull(caller, "Fake event caller cannot be null");
        final Class fixedClass = fixClass(eventClass);
        this.callers.computeIfAbsent(fixedClass, unused -> new ArrayList<>()).add(caller);
    }

    public boolean isFake(@NotNull Event event) {
        Validate.notNull(event, "Event cannot be null");
        final Class fixedClass = fixClass(event.getClass());
        final @Nullable List<FakeEventCaller> callers = this.callers.get(fixedClass);
        if (callers == null) return false;

        for (FakeEventCaller caller : callers) if (caller.isFake(event)) return true;
        return false;
    }

    /**
     * This takes into account the following simplifications:
     * - EntityDamageByEntityEvent and EntityDamageEvent are unified
     */
    @NotNull
    private Class<? extends Event> fixClass(@NotNull Class<? extends Event> eventClass) {
        if (eventClass == EntityDamageByEntityEvent.class) return EntityDamageEvent.class;
        return eventClass;
    }
}

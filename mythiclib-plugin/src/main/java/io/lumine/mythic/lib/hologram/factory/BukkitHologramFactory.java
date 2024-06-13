package io.lumine.mythic.lib.hologram.factory;

import com.google.common.base.Preconditions;
import io.lumine.mythic.lib.hologram.Hologram;
import io.lumine.mythic.lib.hologram.HologramFactory;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class BukkitHologramFactory implements HologramFactory {
    public BukkitHologramFactory() {
    }

    public Hologram newHologram(Location loc, List<String> lines) {
        return new TextDisplayHologram(loc, lines);
    }

    private static final class TextDisplayHologram implements Hologram {
        private final List<String> lines = new ArrayList<>();
        private final List<TextDisplay> spawnedEntities = new ArrayList<>();

        private Location loc;
        private boolean spawned = false;

        TextDisplayHologram(@NotNull Location loc, @NotNull List<String> lines) {
            this.loc = Objects.requireNonNull(loc, "hologram location").clone();
            this.updateLines(lines);

            spawn();
        }

        @Override
        public List<String> getLines() {
            return lines;
        }

        private Location getNewLinePosition() {
            if (this.spawnedEntities.isEmpty()) {
                return this.loc;
            } else {
                TextDisplay last = this.spawnedEntities.get(this.spawnedEntities.size() - 1);
                return last.getLocation().subtract(0.0D, 0.25D, 0.0D);
            }
        }

        public void spawn() {
            int linesSize = this.lines.size();
            int spawnedSize = this.spawnedEntities.size();

            // Remove un-necessary entities
            final int tooMuch = spawnedSize - linesSize;
            for (int j = 0; j < tooMuch; ++j)
                this.spawnedEntities.remove(this.spawnedEntities.size() - 1).remove();

            // Add new lines
            for (int i = 0; i < this.lines.size(); ++i) {
                final String line = this.lines.get(i);

                // Add new entity
                if (i >= this.spawnedEntities.size()) {
                    final Location loc = this.getNewLinePosition();
                    final Chunk chunk = loc.getChunk();
                    if (!chunk.isLoaded()) chunk.load();
                    final TextDisplay as = loc.getWorld().spawn(loc, TextDisplay.class);
                    as.setBillboard(Display.Billboard.CENTER);
                    this.spawnedEntities.add(as);

                    as.setText(line);
                }

                // Entity exists
                else {
                    final TextDisplay entity = this.spawnedEntities.get(i);
                    if (!Objects.equals(entity.getText(), line)) entity.setText(line);
                }
            }

            this.spawned = true;
        }

        @Override
        public void despawn() {
            this.spawnedEntities.forEach(Entity::remove);
            this.spawnedEntities.clear();
            this.spawned = false;
        }

        @Override
        public boolean isSpawned() {
            if (!this.spawned) {
                return false;
            } else {
                Iterator<TextDisplay> var1 = this.spawnedEntities.iterator();

                TextDisplay stand;
                do {
                    if (!var1.hasNext()) {
                        return true;
                    }

                    stand = var1.next();
                } while (stand.isValid());

                return false;
            }
        }

        // private static final double EPSILON = 1e-5;

        @Override
        public void updateLocation(Location newLoc) {
            /* Not very pretty to move around display entities
            Objects.requireNonNull(newLoc, "position");
            if (loc.distanceSquared(newLoc) < EPSILON) return;
            loc = newLoc.clone();
            if (!this.isSpawned()) {
                this.spawn();
            } else {
                double offset = 0.0D;

                for (Iterator<TextDisplay> var4 = this.getSpawnedEntities().iterator(); var4.hasNext(); offset += 0.25D) {
                    TextDisplay as = var4.next();
                    final Location asLoc = loc.clone().add(0.0D, offset, 0.0D);
                    as.setVelocity(asLoc.toVector().subtract(as.getLocation().toVector()).multiply(10));
                    as.teleport(asLoc);
                }
            }*/
        }

        @Override
        public void updateLines(@Nonnull List<String> lines) {
            Objects.requireNonNull(lines, "lines");
            Preconditions.checkArgument(!lines.isEmpty(), "lines cannot be empty");
            for (String line : lines)
                Preconditions.checkArgument(line != null, "null line");

            this.lines.clear();
            this.lines.addAll(lines);
        }

        @Override
        public Location getLocation() {
            return loc;
        }

        public List<TextDisplay> getSpawnedEntities() {
            return this.spawnedEntities;
        }
    }
}

package io.lumine.mythic.lib.version.wrapper;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.ChatMessage;
import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.Container;
import net.minecraft.server.v1_13_R2.ContainerAnvil;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagList;
import net.minecraft.server.v1_13_R2.NBTTagString;
import net.minecraft.server.v1_13_R2.PacketPlayInArmAnimation;
import net.minecraft.server.v1_13_R2.PacketPlayOutAnimation;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_13_R2.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_13_R2.PlayerConnection;
import net.minecraft.server.v1_13_R2.SoundEffect;
import net.minecraft.server.v1_13_R2.SoundEffectType;
import net.minecraft.server.v1_13_R2.TileEntitySkull;
import net.minecraft.server.v1_13_R2.World;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.MMORayTraceResult;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTCompound;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.util.NBTTypeHelper;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftMagicNumbers.NBT;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class VersionWrapper_1_13_R2 implements VersionWrapper {

	@Override
	public FurnaceRecipe getFurnaceRecipe(String path, ItemStack item, Material material, float exp, int cook) {
		return new FurnaceRecipe(new NamespacedKey(MythicLib.inst(), "mmoitems_furnace_" + path), item, material, exp, cook);
	}

	@Override
	public MMORayTraceResult rayTrace(Location loc, Vector direction, double range, Predicate<Entity> option) {
		RayTraceResult hit = loc.getWorld().rayTrace(loc, direction, range, FluidCollisionMode.NEVER, true, .2, option);
		return new MMORayTraceResult(hit != null ? (LivingEntity) hit.getHitEntity() : null,
				hit != null ? hit.getHitPosition().distance(loc.toVector()) : range);
	}

	@Override
	public NBTItem copyTexture(NBTItem item) {
		return getNBTItem(new ItemStack(item.getItem().getType())).addTag(new ItemTag("Damage", item.getInteger("Damage")));
	}

	@Override
	public ItemStack textureItem(Material material, int model) {
		return getNBTItem(new ItemStack(material)).addTag(new ItemTag("Damage", model)).toItem();
	}

	@Override
	public Enchantment getEnchantmentFromString(String s) {
		return Enchantment.getByKey(NamespacedKey.minecraft(s));
	}

	@Override
	public FurnaceRecipe getFurnaceRecipe(NamespacedKey key, ItemStack item, Material material, float exp, int cook) {
		return new FurnaceRecipe(key, item, material, exp, cook);
	}

	@Override
	public BossBar createBossBar(NamespacedKey key, String title, BarColor color, BarStyle style, BarFlag... flags) {
		return Bukkit.createBossBar(key, title, color, style, flags);
	}

	@Override
	public boolean isCropFullyGrown(Block block) {
		if (block.getBlockData() instanceof Ageable) {
			Ageable ageable = (Ageable) block.getBlockData();
			return ageable.getAge() == ageable.getMaximumAge();
		}
		return false;
	}

	@Override
	public boolean isUndead(Entity entity) {
		EntityType type = entity.getType();
		return type == EntityType.SKELETON || type == EntityType.STRAY || type == EntityType.WITHER_SKELETON || type == EntityType.ZOMBIE
				|| type == EntityType.DROWNED || type == EntityType.HUSK || type.name().equals("PIG_ZOMBIE") || type == EntityType.ZOMBIE_VILLAGER
				|| type == EntityType.PHANTOM || type == EntityType.WITHER || type == EntityType.SKELETON_HORSE || type == EntityType.ZOMBIE_HORSE;
	}

	@Override
	public void applyDurabilityData(ItemStack item, ItemStack data) {
		item.setItemMeta(data.getItemMeta());
	}

	@Override
	public void sendJson(Player player, String message) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(message)));
	}

	@Override
	public void sendActionBar(Player player, String message) {
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket(new PacketPlayOutChat(ChatSerializer.a("{\"text\": \"" + message + "\"}"), ChatMessageType.GAME_INFO));
	}

	@Override
	public int getNextContainerId(Player player) {
		return ((CraftPlayer) player).getHandle().nextContainerCounter();
	}

	@Override
	public void handleInventoryCloseEvent(Player player) {
		CraftEventFactory.handleInventoryCloseEvent(((CraftPlayer) player).getHandle());
	}

	@Override
	public void sendPacketOpenWindow(Player player, int containerId) {
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket(new PacketPlayOutOpenWindow(containerId, "minecraft:anvil", new ChatMessage(Blocks.ANVIL.a() + ".name")));
	}

	@Override
	public void sendPacketCloseWindow(Player player, int containerId) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutCloseWindow(containerId));
	}

	@Override
	public void setActiveContainerDefault(Player player) {
		((CraftPlayer) player).getHandle().activeContainer = ((CraftPlayer) player).getHandle().defaultContainer;
	}

	@Override
	public void setActiveContainer(Player player, Object container) {
		((CraftPlayer) player).getHandle().activeContainer = (Container) container;
	}

	@Override
	public void setActiveContainerId(Object container, int containerId) {
		((Container) container).windowId = containerId;
	}

	@Override
	public void addActiveContainerSlotListener(Object container, Player player) {
		((Container) container).addSlotListener(((CraftPlayer) player).getHandle());
	}

	@Override
	public Inventory toBukkitInventory(Object container) {
		return ((Container) container).getBukkitView().getTopInventory();
	}

	@Override
	public Object newContainerAnvil(Player player) {
		return new AnvilContainer(((CraftPlayer) player).getHandle());
	}

	private static class AnvilContainer extends ContainerAnvil {
		public AnvilContainer(EntityHuman entityhuman) {
			super(entityhuman.inventory, entityhuman.world, new BlockPosition(0, 0, 0), entityhuman);
			this.checkReachable = false;
		}
	}

	@Override
	public NBTItem getNBTItem(org.bukkit.inventory.ItemStack item) {
		return new NBTItem_v1_13_R2(item);
	}

	public static class NBTItem_v1_13_R2 extends NBTItem {
		private final net.minecraft.server.v1_13_R2.ItemStack nms;
		private final NBTTagCompound compound;

		public NBTItem_v1_13_R2(org.bukkit.inventory.ItemStack item) {
			super(item);

			nms = CraftItemStack.asNMSCopy(item);
			compound = nms.hasTag() ? nms.getTag() : new NBTTagCompound();
		}

		@Override
		public Object get(String path) { return compound.get(path); }

		@Override
		public String getString(String path) {
			return compound.getString(path);
		}

		@Override
		public boolean hasTag(String path) {
			return compound.hasKey(path);
		}

		@Override
		public boolean getBoolean(String path) {
			return compound.getBoolean(path);
		}

		@Override
		public double getDouble(String path) {
			return compound.getDouble(path);
		}

		@Override
		public int getInteger(String path) {
			return compound.getInt(path);
		}

		@Override
		public NBTCompound getNBTCompound(String path) {
			return new NBTCompound_v1_13_R2(this, path);
		}

		@Override
		public NBTItem addTag(List<ItemTag> tags) {
			tags.forEach(tag -> {
				if (tag.getValue() instanceof Boolean)
					compound.setBoolean(tag.getPath(), (boolean) tag.getValue());
				else if (tag.getValue() instanceof Double)
					compound.setDouble(tag.getPath(), (double) tag.getValue());
				else if (tag.getValue() instanceof String)
					compound.setString(tag.getPath(), (String) tag.getValue());
				else if (tag.getValue() instanceof Integer)
					compound.setInt(tag.getPath(), (int) tag.getValue());
				else if (tag.getValue() instanceof List<?>) {
					NBTTagList tagList = new NBTTagList();
					for (Object s : (List<?>) tag.getValue())
						if (s instanceof String)
							tagList.add(new NBTTagString((String) s));
					compound.set(tag.getPath(), tagList);
				}
			});
			return this;
		}

		@Override
		public NBTItem removeTag(String... paths) {
			for (String path : paths)
				compound.remove(path);
			return this;
		}

		@Override
		public Set<String> getTags() {
			return compound.getKeys();
		}

		@Override
		public org.bukkit.inventory.ItemStack toItem() {
			nms.setTag(compound);
			return CraftItemStack.asBukkitCopy(nms);
		}

		@Override
		public int getTypeId(String path) {
			return compound.get(path).getTypeId();
		}

		@Override
		public Component getDisplayNameComponent() {
			if (compound.getCompound("display").hasKey("Name")) {
				return GsonComponentSerializer.gson().deserialize(compound.getCompound("display").getString("Name"));
			}
			return Component.empty();
		}

		@Override
		// Replaces the current name component with the passed parameter.
		public void setDisplayNameComponent(Component component) {
			if (component != null)
				compound.getCompound("display").setString("Name", GsonComponentSerializer.gson().serialize(component));
			else
				compound.getCompound("display").remove("Name");
		}

		@Override
		public List<Component> getLoreComponents() {
			List<Component> lore = new ArrayList<>();

			if (compound.getCompound("display").hasKey("Lore")) {
				NBTTagList strings = compound.getCompound("display").getList("Lore", NBT.TAG_STRING);
				for (int i = 0; i < strings.size(); i++)
					lore.add(GsonComponentSerializer.gson().deserialize(strings.getString(i)));
			}

			return lore;
		}

		public void setLoreComponents(List<Component> components) {
			NBTTagList lore = new NBTTagList();
			if (components != null && !components.isEmpty()) {
				for (Component component : components)
					lore.add(new NBTTagString(GsonComponentSerializer.gson().serialize(component)));

				compound.getCompound("display").set("Lore", lore);
			}
			else {
				compound.getCompound("display").remove("Lore");
			}
		}

		@Override
		public NBTItem_v1_13_R2 cancelVanillaAttributeModifiers() {
			return this;
		}
	}

	private static class NBTCompound_v1_13_R2 extends NBTCompound {
		private final NBTTagCompound compound;

		public NBTCompound_v1_13_R2(NBTItem_v1_13_R2 item, String path) {
			super();
			compound = (item.hasTag(path) && NBTTypeHelper.COMPOUND.is(item.getTypeId(path))) ? item.compound.getCompound(path) : new NBTTagCompound();
		}

		public NBTCompound_v1_13_R2(NBTCompound_v1_13_R2 comp, String path) {
			super();
			compound = (comp.hasTag(path) && NBTTypeHelper.COMPOUND.is(comp.getTypeId(path))) ? comp.compound.getCompound(path) : new NBTTagCompound();
		}

		@Override
		public boolean hasTag(String path) {
			return compound.hasKey(path);
		}

		@Override
		public Object get(String path) {
			return compound.get(path);
		}

		@Override
		public NBTCompound getNBTCompound(String path) {
			return new NBTCompound_v1_13_R2(this, path);
		}

		@Override
		public String getString(String path) {
			return compound.getString(path);
		}

		@Override
		public boolean getBoolean(String path) {
			return compound.getBoolean(path);
		}

		@Override
		public double getDouble(String path) {
			return compound.getDouble(path);
		}

		@Override
		public int getInteger(String path) {
			return compound.getInt(path);
		}

		@Override
		public Set<String> getTags() {
			return compound.getKeys();
		}

		@Override
		public int getTypeId(String path) {
			return compound.get(path).getTypeId();
		}
	}

	@Override
	public boolean isInBoundingBox(Entity entity, Location loc) {
		return entity.getBoundingBox().expand(.2, .2, .2, .2, .2, .2).contains(loc.toVector());
	}

	@Override
	public double distanceSquaredFromBoundingBox(Entity entity, Location loc) {
		BoundingBox box = entity.getBoundingBox().expand(.2, .2, .2, .2, .2, .2);

		double dx = loc.getX() > box.getMinX() && loc.getX() < box.getMaxX() ? 0
				: Math.min(Math.abs(box.getMinX() - loc.getX()), Math.abs(box.getMaxX() - loc.getX()));
		double dy = loc.getY() > box.getMinY() && loc.getY() < box.getMaxY() ? 0
				: Math.min(Math.abs(box.getMinY() - loc.getY()), Math.abs(box.getMaxY() - loc.getY()));
		double dz = loc.getZ() > box.getMinZ() && loc.getZ() < box.getMaxZ() ? 0
				: Math.min(Math.abs(box.getMinZ() - loc.getZ()), Math.abs(box.getMaxZ() - loc.getZ()));

		return dx * dx + dx * dy + dz * dz;
	}

	@Override
	public void playArmAnimation(Player player) {
		EntityPlayer p = ((CraftPlayer) player).getHandle();
		PlayerConnection connection = p.playerConnection;
		PacketPlayOutAnimation armSwing = new PacketPlayOutAnimation(p, 0);
		connection.sendPacket(armSwing);
		connection.a(new PacketPlayInArmAnimation(EnumHand.MAIN_HAND));
	}

	@Override
	public Sound getBlockPlaceSound(org.bukkit.block.Block block) {
		try {
			World nmsWorld = ((CraftWorld) block.getWorld()).getHandle();

			net.minecraft.server.v1_13_R2.Block nmsBlock = nmsWorld.getType(new BlockPosition(block.getX(), block.getY(), block.getZ())).getBlock();
			SoundEffectType soundEffectType = nmsBlock.getStepSound();

			Field breakSound = SoundEffectType.class.getDeclaredField("q");
			breakSound.setAccessible(true);
			SoundEffect nmsSound = (SoundEffect) breakSound.get(soundEffectType);

			Field keyField = SoundEffect.class.getDeclaredField("a");
			keyField.setAccessible(true);
			MinecraftKey nmsString = (MinecraftKey) keyField.get(nmsSound);

			return Sound.valueOf(nmsString.getKey().replace(".", "_").toUpperCase());
		} catch (IllegalAccessException | NoSuchFieldException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public String getSkullValue(Block block) {
		TileEntitySkull skullTile = (TileEntitySkull) ((CraftWorld) block.getWorld()).getHandle()
				.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
		if (skullTile.getGameProfile() == null)
			return "";
		return skullTile.getGameProfile().getProperties().get("textures").iterator().next().getValue();
	}

	@Override
	public void setSkullValue(Block block, String value) {
		TileEntitySkull skullTile = (TileEntitySkull) ((CraftWorld) block.getWorld()).getHandle()
				.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("textures", value));
		skullTile.setGameProfile(profile);
		skullTile.update();
	}
}

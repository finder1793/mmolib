package io.lumine.mythic.lib.version.wrapper;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.Container;
import net.minecraft.server.v1_16_R3.ContainerAccess;
import net.minecraft.server.v1_16_R3.ContainerAnvil;
import net.minecraft.server.v1_16_R3.Containers;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumHand;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagByte;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagDouble;
import net.minecraft.server.v1_16_R3.NBTTagFloat;
import net.minecraft.server.v1_16_R3.NBTTagInt;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NBTTagLong;
import net.minecraft.server.v1_16_R3.NBTTagShort;
import net.minecraft.server.v1_16_R3.NBTTagString;
import net.minecraft.server.v1_16_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_16_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import net.minecraft.server.v1_16_R3.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.SoundEffect;
import net.minecraft.server.v1_16_R3.SoundEffectType;
import net.minecraft.server.v1_16_R3.TileEntitySkull;
import net.minecraft.server.v1_16_R3.World;
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
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers.NBT;
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
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

public class VersionWrapper_1_16_R3 implements VersionWrapper {
	private static final Map<Material, Material> oreDrops = new HashMap<>();

	static {
		oreDrops.put(Material.IRON_ORE, Material.IRON_INGOT);
		oreDrops.put(Material.GOLD_ORE, Material.GOLD_INGOT);
		oreDrops.put(Material.ANCIENT_DEBRIS, Material.NETHERITE_INGOT);
	}

	@Override
	public Map<Material, Material> getOreDrops() {
		return oreDrops;
	}

	@Override
	public int getFoodRestored(ItemStack item) {
		return CraftItemStack.asNMSCopy(item).getItem().getFoodInfo().getNutrition();
	}

	@Override
	public float getSaturationRestored(ItemStack item) {
		return CraftItemStack.asNMSCopy(item).getItem().getFoodInfo().getSaturationModifier();
	}

	@Override
	public void sendJson(Player player, String message) {
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket(new PacketPlayOutChat(ChatSerializer.a(message), ChatMessageType.CHAT, UUID.randomUUID()));
	}

	@Override
	public void sendActionBar(Player player, String message) {
		((CraftPlayer) player).getHandle().playerConnection
				.sendPacket(new PacketPlayOutChat(ChatSerializer.a("{\"text\": \"" + message + "\"}"), ChatMessageType.GAME_INFO, UUID.randomUUID()));
	}

	@Override
	public int getNextContainerId(Player player) {
		return toNMS(player).nextContainerCounter();
	}

	@Override
	public void handleInventoryCloseEvent(Player player) {
		CraftEventFactory.handleInventoryCloseEvent(toNMS(player));
	}

	@Override
	public void sendPacketOpenWindow(Player player, int containerId) {
		toNMS(player).playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, Containers.ANVIL, new ChatMessage("Repair & Name")));
	}

	@Override
	public void sendPacketCloseWindow(Player player, int containerId) {
		toNMS(player).playerConnection.sendPacket(new PacketPlayOutCloseWindow(containerId));
	}

	@Override
	public void setActiveContainerDefault(Player player) {
		toNMS(player).activeContainer = toNMS(player).defaultContainer;
	}

	@Override
	public void setActiveContainer(Player player, Object container) {
		toNMS(player).activeContainer = (Container) container;
	}

	@Override
	public void setActiveContainerId(Object container, int containerId) {
		Field field = null;

		try {
			field = Container.class.getField("windowId");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		FieldUtils.removeFinalModifier(field);

		try {
			FieldUtils.writeField(field, container, containerId);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addActiveContainerSlotListener(Object container, Player player) {
		((Container) container).addSlotListener(toNMS(player));
	}

	@Override
	public Inventory toBukkitInventory(Object container) {
		return ((Container) container).getBukkitView().getTopInventory();
	}

	@Override
	public Object newContainerAnvil(Player player) {
		return new AnvilContainer(player);
	}

	private EntityPlayer toNMS(Player player) {
		return ((CraftPlayer) player).getHandle();
	}

	private class AnvilContainer extends ContainerAnvil {
		public AnvilContainer(Player player) {
			super(getNextContainerId(player), ((CraftPlayer) player).getHandle().inventory,
					ContainerAccess.at(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(0, 0, 0)));
			this.checkReachable = false;
			setTitle(new ChatMessage("Repair & Name"));
		}
	}

	@Override
	public NBTItem getNBTItem(org.bukkit.inventory.ItemStack item) {
		return new NBTItem_v1_16_R3(item);
	}

	public static class NBTItem_v1_16_R3 extends NBTItem {
		private final net.minecraft.server.v1_16_R3.ItemStack nms;
		private final NBTTagCompound compound;

		public NBTItem_v1_16_R3(org.bukkit.inventory.ItemStack item) {
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
			return new NBTCompound_v1_16_R3(this, path);
		}

		@Override
		public NBTItem addTag(List<ItemTag> tags) {
			tags.forEach(tag -> put(compound, tag.getPath(), tag.getValue()));
			return this;
		}

		private static void put(NBTTagCompound compound, String path, Object value) {
			// remove the tag if the inserted value is null
			if(value == null) {
				compound.remove(path);
				return;
			}
			NBTBase tag = tagOf(value, compound.getCompound(path));
			if (tag == null) {
				// silently ignore it when the tag could not be converted
			} else if (tag instanceof NBTTagCompound) {
				compound.set(path, compound.getCompound(path).a((NBTTagCompound) tag));
			} else {
				compound.set(path, tag);
			}
		}

		private static NBTBase tagOf(Object value, NBTTagCompound existing) {
			if (value instanceof Map<?, ?>) return getCompoundTag((Map<?, ?>) value, existing);
			if (value instanceof List<?>)   return getListTag((List<?>) value);
			if (value instanceof Boolean)   return NBTTagByte.a((boolean) value);
			if (value instanceof String)    return NBTTagString.a((String) value);
			if (value instanceof Double)    return NBTTagDouble.a((double) value);
			if (value instanceof Float)     return NBTTagFloat.a((float) value);
			if (value instanceof Long)      return NBTTagLong.a((long) value);
			if (value instanceof Integer)   return NBTTagInt.a((int) value);
			if (value instanceof Short)     return NBTTagShort.a((short) value);
			if (value instanceof Byte)      return NBTTagByte.a((byte) value);
			return null;
		}

		private static NBTTagList getListTag(List<?> list) {
			NBTTagList tagList = new NBTTagList();
			for (Object o : list) {
				NBTBase tag = tagOf(o, null);
				if(tag == null) {
					// could not turn the object to a tag,
					// or it was already null
					// ignore the tag
					continue;
				}
				if(!tagList.b(tagList.size(), tag)) {
					// could not add tag to the list
					// do something ?
				}
			}
			return tagList;
		}

		private static NBTTagCompound getCompoundTag(Map<?, ?> map, @Nullable NBTTagCompound previous) {
			final NBTTagCompound compoundTag;
			if (previous == null) {
				compoundTag = new NBTTagCompound();
			} else {
				compoundTag = previous;
			}
			map.entrySet().forEach(entry -> put(compoundTag, entry.getKey().toString(), entry.getValue()));
			return compoundTag;
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

		@Override
		// Replaces the current lore component with the passed parameter.
		public void setLoreComponents(List<Component> components) {
			NBTTagList lore = new NBTTagList();
			if (components != null && !components.isEmpty()) {
				for (Component component : components)
					lore.add(NBTTagString.a(GsonComponentSerializer.gson().serialize(component)));

				compound.getCompound("display").set("Lore", lore);
			}
			else {
				compound.getCompound("display").remove("Lore");
			}
		}

		@Override
		public NBTItem_v1_16_R3 cancelVanillaAttributeModifiers() {
			return this;
		}
	}

	private static class NBTCompound_v1_16_R3 extends NBTCompound {
		private final NBTTagCompound compound;

		public NBTCompound_v1_16_R3(NBTItem_v1_16_R3 item, String path) {
			super();
			compound = (item.hasTag(path) && NBTTypeHelper.COMPOUND.is(item.getTypeId(path))) ? item.compound.getCompound(path) : new NBTTagCompound();
		}

		public NBTCompound_v1_16_R3(NBTCompound_v1_16_R3 comp, String path) {
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
			return new NBTCompound_v1_16_R3(this, path);
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

			net.minecraft.server.v1_16_R3.Block nmsBlock = nmsWorld.getType(new BlockPosition(block.getX(), block.getY(), block.getZ())).getBlock();
			SoundEffectType soundEffectType = nmsBlock.getStepSound(nmsBlock.getBlockData());

			Field breakSound = SoundEffectType.class.getDeclaredField("Z");
			breakSound.setAccessible(true);
			SoundEffect nmsSound = (SoundEffect) breakSound.get(soundEffectType);

			Field keyField = SoundEffect.class.getDeclaredField("b");
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
		if (skullTile.gameProfile == null)
			return "";
		return skullTile.gameProfile.getProperties().get("textures").iterator().next().getValue();
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
	public void applyDurabilityData(ItemStack item, ItemStack data) {
		item.setItemMeta(data.getItemMeta());
	}

	@Override
	public NBTItem copyTexture(NBTItem item) {
		return getNBTItem(new ItemStack(item.getItem().getType())).addTag(new ItemTag("CustomModelData", item.getInteger("CustomModelData")));
	}

	@Override
	public ItemStack textureItem(Material material, int model) {
		return getNBTItem(new ItemStack(material)).addTag(new ItemTag("CustomModelData", model)).toItem();
	}

	@Override
	public BossBar createBossBar(NamespacedKey key, String title, BarColor color, BarStyle style, BarFlag... flags) {
		return Bukkit.createBossBar(key, title, color, style, flags);
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
}

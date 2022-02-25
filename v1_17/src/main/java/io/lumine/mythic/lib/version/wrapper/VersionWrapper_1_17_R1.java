package io.lumine.mythic.lib.version.wrapper;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.MMORayTraceResult;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTCompound;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.util.NBTTypeHelper;
import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftMagicNumbers.NBT;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class VersionWrapper_1_17_R1 implements VersionWrapper {
	@Override
	public void sendJson(Player player, String message) {
		((CraftPlayer) player).getHandle().connection.send(
				new ClientboundChatPacket(net.minecraft.network.chat.Component.Serializer.fromJson(message), ChatType.CHAT,
						UUID.randomUUID()));
	}

	@Override
	public void sendActionBar(Player player, String message) {
		((CraftPlayer) player).getHandle().connection.send(
				new ClientboundChatPacket(net.minecraft.network.chat.Component.Serializer.fromJson("{\"text\": \"" + message + "\"}"),
						ChatType.GAME_INFO, UUID.randomUUID()));
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
		/*toNMS(player).playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, Container.b, new ChatMessage("Repair & Name")));**/
	}

	@Override
	public void sendPacketCloseWindow(Player player, int containerId) {
		toNMS(player).connection.send(new ClientboundContainerClosePacket(containerId));
	}

	@Override
	public void setActiveContainerDefault(Player player) {
		/*toNMS(player).activeContainer = toNMS(player).defaultContainer;**/
	}

	@Override
	public void setActiveContainer(Player player, Object container) {
		/*toNMS(player).activeContainer = (Container) container;*/
	}

	@Override
	public void setActiveContainerId(Object container, int containerId) {
		/*Field field = null;

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
		}*/
	}

	@Override
	public void addActiveContainerSlotListener(Object container, Player player) {
		/*((Container) container).addSlotListener(toNMS(player));*/
	}

	@Override
	public Inventory toBukkitInventory(Object container) {
		return ((AbstractContainerMenu) container).getBukkitView().getTopInventory();
	}

	@Override
	public Object newContainerAnvil(Player player) {
		return new AnvilContainer(player);
	}

	private ServerPlayer toNMS(Player player) {
		return ((CraftPlayer) player).getHandle();
	}

	private class AnvilContainer extends AnvilMenu {
		public AnvilContainer(Player player) {
			super(getNextContainerId(player), ((CraftPlayer) player).getHandle().getInventory(),
					ContainerLevelAccess.create(((CraftWorld) player.getWorld()).getHandle(), new BlockPos(0, 0, 0)));
			this.checkReachable = false;
			setTitle(new TextComponent("Repair & Name"));
		}
	}

	@Override
	public NBTItem getNBTItem(ItemStack item) {
		return new NBTItem_v1_17_R1(item);
	}

	public static class NBTItem_v1_17_R1 extends NBTItem {
		private final net.minecraft.world.item.ItemStack nms;
		private final CompoundTag compound;

		public NBTItem_v1_17_R1(ItemStack item) {
			super(item);

			nms = CraftItemStack.asNMSCopy(item);
			compound = nms.hasTag() ? nms.getTag() : new CompoundTag();
		}

		@Override
		public Object get(String path) {
			return compound.get(path);
		}

		@Override
		public String getString(String path) {
			return compound.getString(path);
		}

		@Override
		public boolean hasTag(String path) {
			return compound.contains(path);
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
			return new NBTCompound_v1_17_R1(this, path);
		}

		@Override
		public NBTItem addTag(List<ItemTag> tags) {
			tags.forEach(tag -> {
				if (tag.getValue() instanceof Boolean) compound.putBoolean(tag.getPath(), (boolean) tag.getValue());
				else if (tag.getValue() instanceof Double) compound.putDouble(tag.getPath(), (double) tag.getValue());
				else if (tag.getValue() instanceof String) compound.putString(tag.getPath(), (String) tag.getValue());
				else if (tag.getValue() instanceof Integer) compound.putInt(tag.getPath(), (int) tag.getValue());
				else if (tag.getValue() instanceof List<?>) {
					ListTag tagList = new ListTag();
					for (Object s : (List<?>) tag.getValue())
						if (s instanceof String) tagList.add(StringTag.valueOf((String) s));
					compound.put(tag.getPath(), tagList);
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
			return compound.getAllKeys();
		}

		@Override
		public ItemStack toItem() {
			nms.setTag(compound);
			return CraftItemStack.asBukkitCopy(nms);
		}

		@Override
		public int getTypeId(String path) {
			return compound.get(path).getId();
		}

		@Override
		public Component getDisplayNameComponent() {
			if (compound.getCompound("display").contains("Name")) {
				return GsonComponentSerializer.gson().deserialize(compound.getCompound("display").getString("Name"));
			}
			return Component.empty();
		}

		@Override
		// Replaces the current name component with the passed parameter.
		public void setDisplayNameComponent(Component component) {
			if (component != null) compound.getCompound("display").putString("Name", GsonComponentSerializer.gson().serialize(component));
			else compound.getCompound("display").remove("Name");
		}

		@Override
		public List<Component> getLoreComponents() {
			List<Component> lore = new ArrayList<>();

			if (compound.getCompound("display").contains("Lore")) {
				ListTag strings = compound.getCompound("display").getList("Lore", NBT.TAG_STRING);
				for (int i = 0; i < strings.size(); i++)
					lore.add(GsonComponentSerializer.gson().deserialize(strings.getString(i)));
			}

			return lore;
		}

		@Override
		// Replaces the current lore component with the passed parameter.
		public void setLoreComponents(List<Component> components) {
			ListTag lore = new ListTag();
			if (components != null && !components.isEmpty()) {
				for (Component component : components)
					lore.add(StringTag.valueOf(GsonComponentSerializer.gson().serialize(component)));

				compound.getCompound("display").put("Lore", lore);
			} else {
				compound.getCompound("display").remove("Lore");
			}
		}

		@Override
		public NBTItem_v1_17_R1 cancelVanillaAttributeModifiers() {
			return this;
		}
	}

	private static class NBTCompound_v1_17_R1 extends NBTCompound {
		private final CompoundTag compound;

		public NBTCompound_v1_17_R1(NBTItem_v1_17_R1 item, String path) {
			super();
			compound = (item.hasTag(path) && NBTTypeHelper.COMPOUND.is(item.getTypeId(path))) ? item.compound.getCompound(path) : new CompoundTag();
		}

		public NBTCompound_v1_17_R1(NBTCompound_v1_17_R1 comp, String path) {
			super();
			compound = (comp.hasTag(path) && NBTTypeHelper.COMPOUND.is(comp.getTypeId(path))) ? comp.compound.getCompound(path) : new CompoundTag();
		}

		@Override
		public boolean hasTag(String path) {
			return compound.contains(path);
		}

		@Override
		public Object get(String path) {
			return compound.get(path);
		}

		@Override
		public NBTCompound getNBTCompound(String path) {
			return new NBTCompound_v1_17_R1(this, path);
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
			return compound.getAllKeys();
		}

		@Override
		public int getTypeId(String path) {
			return compound.get(path).getId();
		}
	}

	@Override
	public boolean isInBoundingBox(Entity entity, Location loc) {
		return entity.getBoundingBox().expand(.2, .2, .2, .2, .2, .2).contains(loc.toVector());
	}

	@Override
	public double distanceSquaredFromBoundingBox(Entity entity, Location loc) {
		BoundingBox box = entity.getBoundingBox().expand(.2, .2, .2, .2, .2, .2);

		double dx = loc.getX() > box.getMinX() && loc.getX() < box.getMaxX() ? 0 : Math.min(Math.abs(box.getMinX() - loc.getX()),
				Math.abs(box.getMaxX() - loc.getX()));
		double dy = loc.getY() > box.getMinY() && loc.getY() < box.getMaxY() ? 0 : Math.min(Math.abs(box.getMinY() - loc.getY()),
				Math.abs(box.getMaxY() - loc.getY()));
		double dz = loc.getZ() > box.getMinZ() && loc.getZ() < box.getMaxZ() ? 0 : Math.min(Math.abs(box.getMinZ() - loc.getZ()),
				Math.abs(box.getMaxZ() - loc.getZ()));

		return dx * dx + dx * dy + dz * dz;
	}

	@Override
	public void playArmAnimation(Player player) {
		ServerPlayer p = ((CraftPlayer) player).getHandle();
		ServerGamePacketListenerImpl connection = p.connection;
		ClientboundAnimatePacket armSwing = new ClientboundAnimatePacket(p, 0);
		connection.send(armSwing);
		connection.handleAnimate(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
	}

	@Override
	public Sound getBlockPlaceSound(Block block) {
		ServerLevel nmsWorld = ((CraftWorld) block.getWorld()).getHandle();
		BlockState state = nmsWorld.getBlockState(new BlockPos(block.getX(), block.getY(), block.getZ()));
		SoundEvent event = state.getBlock().getSoundType(state).getPlaceSound();

		return Sound.valueOf(event.getLocation().getPath().replace(".", "_").toUpperCase());
	}

	@Override
	public String getSkullValue(Block block) {
		SkullBlockEntity skull = (SkullBlockEntity) ((CraftWorld) block.getWorld()).getHandle()
				.getBlockEntity(new BlockPos(block.getX(), block.getY(), block.getZ()));
		if (skull.getOwnerProfile() == null) return "";
		return skull.getOwnerProfile().getProperties().get("textures").iterator().next().getValue();
	}

	@Override
	public void setSkullValue(Block block, String value) {
		SkullBlockEntity skull = (SkullBlockEntity) ((CraftWorld) block.getWorld()).getHandle()
				.getBlockEntity(new BlockPos(block.getX(), block.getY(), block.getZ()));
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("textures", value));
		skull.setOwner(profile);
		skull.setChanged();
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
		return type == EntityType.SKELETON || type == EntityType.STRAY || type == EntityType.WITHER_SKELETON || type == EntityType.ZOMBIE || type == EntityType.DROWNED || type == EntityType.HUSK || type.name()
				.equals("PIG_ZOMBIE") || type == EntityType.ZOMBIE_VILLAGER || type == EntityType.PHANTOM || type == EntityType.WITHER || type == EntityType.SKELETON_HORSE || type == EntityType.ZOMBIE_HORSE;
	}
}

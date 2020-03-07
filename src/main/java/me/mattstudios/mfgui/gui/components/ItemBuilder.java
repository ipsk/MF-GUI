package me.mattstudios.mfgui.gui.components;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

@SuppressWarnings("unused")
public final class ItemBuilder {

    private final ItemStack itemStack;
    private ItemMeta meta;

    /**
     * Constructor of the item builder
     *
     * @param itemStack The ItemStack of the item
     */
    public ItemBuilder(final ItemStack itemStack) {
        Validate.notNull(itemStack, "Item can't be null!");

        this.itemStack = itemStack;
        meta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
    }

    /**
     * Secondary constructor with only Material
     *
     * @param material The material of the ItemStack
     */
    public ItemBuilder(final Material material) {
        this(new ItemStack(material));
    }

    /**
     * Builds the item into ItemStack
     *
     * @return The fully built item
     */
    public ItemStack build() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Set display name of the item
     *
     * @param name the display name of the item
     * @return The ItemBuilder
     */
    public ItemBuilder setName(final String name) {
        meta.setDisplayName(name);
        return this;
    }

    /**
     * Sets the amount of items
     *
     * @param amount the amount of items
     * @return The ItemBuilder
     */
    public ItemBuilder setAmount(final int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Set the lore lines of an item
     *
     * @param lore the lore lines to set
     * @return The ItemBuilder
     */
    public ItemBuilder setLore(final String... lore) {
        meta.setLore(Arrays.asList(lore));
        return this;
    }

    /**
     * Add enchantments to an item
     *
     * @param enchantment            the enchantment to add
     * @param level                  the level of the enchantment
     * @param ignoreLevelRestriction If should or not ignore it
     * @return The ItemBuilder
     */
    public ItemBuilder addEnchantment(final Enchantment enchantment, final int level, final boolean ignoreLevelRestriction) {
        meta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }

    /**
     * Add enchantments to an item
     *
     * @param enchantment the enchantment to add
     * @param level       the level of the enchantment
     * @return The ItemBuilder
     */
    public ItemBuilder addEnchantment(final Enchantment enchantment, final int level) {
        return addEnchantment(enchantment, level, true);
    }

    /**
     * Add enchantments to an item
     *
     * @param enchantment the enchantment to add
     * @return The ItemBuilder
     */
    public ItemBuilder addEnchantment(final Enchantment enchantment) {
        return addEnchantment(enchantment, 1, true);
    }

    /**
     * Removes a certain enchantment from the item
     *
     * @param enchantment The enchantment to remove
     * @return The ItemBuilder
     */
    public ItemBuilder removeEnchantment(final Enchantment enchantment) {
        itemStack.removeEnchantment(enchantment);
        return this;
    }

    /**
     * Add a custom item flag to the item
     *
     * @param flags the flags to add
     * @return The ItemBuilder
     */
    public ItemBuilder addItemFlags(final ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    /**
     * Sets the item as unbreakable
     *
     * @param unbreakable If should or not be unbreakable
     * @return The ItemBuilder
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Makes the Item glow
     *
     * @return The ItemBuilder
     */
    public ItemBuilder glow() {
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        return this;
    }

    public ItemBuilder setSkullTexture(final String texture) {
        if (itemStack.getType() != Material.PLAYER_HEAD) return this;

        SkullMeta skullMeta = (SkullMeta) meta;
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        Field profileField;

        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        meta = skullMeta;

        return this;
    }

}
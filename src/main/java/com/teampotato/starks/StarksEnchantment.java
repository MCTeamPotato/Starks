package com.teampotato.starks;


import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import static com.teampotato.starks.Starks.*;

public class StarksEnchantment extends Enchantment {
    private static Rarity getRarityInConfig() {
        switch (rarity.get()) {
            case "COMMON" -> {
                return Rarity.COMMON;
            }
            case "UNCOMMON" -> {
                return Rarity.UNCOMMON;
            }
            case "RARE" -> {
                return Rarity.RARE;
            }
            case "VERY_RARE" -> {
                return Rarity.VERY_RARE;
            }
            default -> {
                LOGGER.error("Your rarity value in Starks config is invalid. Switch to UNCOMMON rarity");
                return Rarity.UNCOMMON;
            }
        }
    }

    protected StarksEnchantment() {
        super(getRarityInConfig(), EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});
    }

    public boolean isTreasureOnly() {
        return isTreasureOnly.get();
    }

    public boolean isCurse() {
        return isCurse.get();
    }

    public boolean isTradeable() {
        return isTradeable.get();
    }

    public boolean isDiscoverable() {
        return isDiscoverable.get();
    }

    public boolean isAllowedOnBooks() {
        return isAllowedOnBooks.get();
    }
}
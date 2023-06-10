package com.teampotato.starks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

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
        super(getRarityInConfig(), EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});
    }

    public boolean isTreasure() {
        return isTreasureOnly.get();
    }

    public boolean isCursed() {
        return isCurse.get();
    }

    public boolean isAvailableForEnchantedBookOffer() {
        return isTradeable.get();
    }

    public boolean isAvailableForRandomSelection() {
        return isDiscoverable.get();
    }
}

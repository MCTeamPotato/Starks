package com.teampotato.starks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.teampotato.starks.Starks.*;

public class StarksEnchantment extends Enchantment {
    private static final EquipmentSlotType MAINHAND = EquipmentSlotType.MAINHAND;
    private static final EquipmentSlotType OFFHAND = EquipmentSlotType.OFFHAND;
    private static final EquipmentSlotType HEAD = EquipmentSlotType.HEAD;
    private static final EquipmentSlotType CHEST = EquipmentSlotType.CHEST;
    private static final EquipmentSlotType LEGS = EquipmentSlotType.LEGS;
    private static final EquipmentSlotType FEET = EquipmentSlotType.FEET;

    private static final Map<String, EquipmentSlotType> map = new HashMap<>();
    private static final EnchantmentType ENCHANTMENT_TYPE = EnchantmentType.create(ID + ":starks", null);

    @SuppressWarnings("unchecked")
    private static EquipmentSlotType[] getValidTypes() {
        if (map.isEmpty()) {
            map.put(MAINHAND.toString(), MAINHAND);
            map.put(OFFHAND.toString(), OFFHAND);
            map.put(HEAD.toString(), HEAD);
            map.put(CHEST.toString(), CHEST);
            map.put(LEGS.toString(), LEGS);
            map.put(FEET.toString(), FEET);
        }
        List<String> validSlotTypes = (List<String>) validEquipmentSlotTypes.get();
        EquipmentSlotType[] typeList = new EquipmentSlotType[validSlotTypes.size()];
        int index = 0;
        for (String type : validSlotTypes) {
            typeList[index++] = map.get(type);
        }
        return typeList;
    }

    private static Rarity getRarityInConfig() {
        switch (rarity.get()) {
            case "COMMON":
                return Rarity.COMMON;
            case "UNCOMMON":
                return Rarity.UNCOMMON;
            case "RARE":
                return Rarity.RARE;
            case "VERY_RARE":
                return Rarity.VERY_RARE;
            default:
                LOGGER.error("Your rarity value in Starks config is invalid. Switch to UNCOMMON rarity");
                return Rarity.UNCOMMON;
        }
    }

    protected StarksEnchantment() {
        super(getRarityInConfig(), ENCHANTMENT_TYPE, getValidTypes());
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
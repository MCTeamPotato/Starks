package com.teampotato.starks;

import com.google.common.collect.Lists;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Starks implements ModInitializer {
	public static final String ID = "starks";
	public static final Logger LOGGER = LogManager.getLogger("Starks");

	public void onInitialize() {
		ModLoadingContext.registerConfig(ID, ModConfig.Type.COMMON, configSpec);
		Registry.register(Registry.ENCHANTMENT, new Identifier(ID, ID), new StarksEnchantment());
	}

	public static ForgeConfigSpec configSpec;
	public static ForgeConfigSpec.BooleanValue isTradeable, isCurse, isTreasureOnly, isDiscoverable, isAverageHealAmounts;
	public static ForgeConfigSpec.ConfigValue<String> rarity;
	public static ForgeConfigSpec.DoubleValue playerAroundX, playerAroundY, playerAroundZ, playerHealthPercentage;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> invalidDamageSourceTypes;

	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.push("Starks");
		builder.comment(
				"The enchantment makes your attack to creatures heal your pets, but the pets should be close to you",
				"Here are the expanded coordinates data of the judgment, centered on your location"
		);
		playerAroundX = builder.defineInRange("playerAroundX", 10.00, 1.00, Double.MAX_VALUE);
		playerAroundY = builder.defineInRange("playerAroundY", 10.00, 1.00, Double.MAX_VALUE);
		playerAroundZ = builder.defineInRange("playerAroundZ", 10.00, 1.00, Double.MAX_VALUE);
		builder.comment("\n");
		playerHealthPercentage = builder
				.comment("If player's current health is less than 'maxHealth * playerHealthPercentage', players will not take damage for their pets")
				.defineInRange("playerHealthPercentage", 0.30, 0.00, 1.00);
		builder.comment("\n");
		builder.comment("Enchantment's properties");
		isTradeable = builder.define("isTradeable", true);
		isCurse = builder.define("isCurse", false);
		isTreasureOnly = builder.define("isTreasure", false);
		isDiscoverable = builder.define("canBeFoundInLoot", true);
		rarity = builder
				.comment("Allowed value: COMMON, UNCOMMON, RARE, VERY_RARE")
				.define("rarity", "UNCOMMON");
		builder.comment("\n");
		isAverageHealAmounts = builder
				.comment(
						"Your attack to creatures will heal your pets based on the damage amounts.",
						"But if you turn this off, the damage amounts won't be average according to the pets number, but all amounts persent on each heal."
				)
				.define("isAverageHealAmounts", true);
		invalidDamageSourceTypes = builder
				.comment(
						"Here are the types of damage source that the player will not take instead for the pets.",
						"Allowed values: " +
								"inFire, lightningBolt, onFire, " +
								"lava, hotFloor, inWall, " +
								"cramming, drown, starve, " +
								"cactus, fall, flyIntoWall, " +
								"outOfWorld, generic, magic, " +
								"wither, anvil, fallingBlock, " +
								"dragonBreath, dryout, sweetBerryBush"
				)
				.defineList("invalidDamageSourceTypes", Lists.newArrayList("onFire", "drown", "fall", "outOfWorld", "cactus", "lava"), o -> o instanceof String);
		builder.pop();
		configSpec = builder.build();
	}

	public static boolean isStarksPresent(PlayerEntity player) {
		return player.getEquippedStack(EquipmentSlot.HEAD).getEnchantments().toString().contains(ID);
	}
}
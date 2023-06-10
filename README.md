# Starks

This mod adds a new interesting enchantment: Starks.

This enchantment is only valid on helmets.

## Brief Ability Intro

1. This enchantment will make your attack to creatures heal your pets around you according to your damage amount.

2. This enchantment will let you take damage for your pets instead if your health is enough.

## Config

a lot of things are configurable in the mod's config, including:

**playerAroundX**, **playerAroundY**, **playerAroundZ** (intro:The enchantment makes your attack to creatures heal your pets, but the pets should be close to you. Here are the expanded coordinates data of the judgment, centered on your location)

**playerHealthPercentage** (intro: If player's current health is less than 'maxHealth * playerHealthPercentage', players will not take damage for their pets. Additionally, players won't take damage that is more than their health too)

Enchantment's properties: **isTradeable**, **isCurse**, **isTreasureOnly**, **isDiscoverable**, **isAllowedOnBooks** (forge only), **rarity**

**isAverageHealAmounts** (intro: Your attack to creatures will heal your pets based on the damage amounts. But if you turn this off, the damage amounts won't be average according to the pets number, but all amounts persent on each heal.)

**validDamageSourceTypes** (intro: Here are the types of damage source that the player will take instead for the pets. Allowed values: inFire, lightningBolt, onFire, lava, hotFloor, inWall, cramming, drown, starve, cactus, fall, flyIntoWall, outOfWorld, generic, magic, wither, anvil, fallingBlock, dragonBreath, dryout, sweetBerryBush.)

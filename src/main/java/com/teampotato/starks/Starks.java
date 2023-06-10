package com.teampotato.starks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(Starks.ID)
@Mod.EventBusSubscriber
public class Starks {
    public static final String ID = "starks";
    public static final Logger LOGGER = LogManager.getLogger("Starks");

    public static ForgeConfigSpec configSpec;
    public static ForgeConfigSpec.BooleanValue isTradeable, isCurse, isTreasureOnly, isDiscoverable, isAllowedOnBooks, isAverageHealAmounts;
    public static ForgeConfigSpec.ConfigValue<String> rarity;
    public static ForgeConfigSpec.DoubleValue playerAroundX, playerAroundY, playerAroundZ, playerHealthPercentage;

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
        isAllowedOnBooks = builder.define("isAllowedOnBooks", true);
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
        builder.pop();
        configSpec = builder.build();
    }

    public static final DeferredRegister<Enchantment> ENCHANTMENT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ID);

    @SuppressWarnings("unused")
    public static final RegistryObject<Enchantment> STARKS = ENCHANTMENT_DEFERRED_REGISTER.register(ID, StarksEnchantment::new);

    private static boolean isStarksPresent(PlayerEntity player) {
        return player.getItemBySlot(EquipmentSlotType.HEAD).getEnchantmentTags().toString().contains(ID);
    }

    @SubscribeEvent
    public static void onPlayerAttack(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getDirectEntity();
            if (!isStarksPresent(player)) return;
            AxisAlignedBB playerAABB = player.getBoundingBox();
            List<TameableEntity> pets = player.level.getEntitiesOfClass(TameableEntity.class,
                    new AxisAlignedBB(
                            playerAABB.minX - playerAroundX.get(),
                            playerAABB.minY - playerAroundY.get(),
                            playerAABB.minZ - playerAroundZ.get(),
                            playerAABB.maxX + playerAroundX.get(),
                            playerAABB.maxY + playerAroundY.get(),
                            playerAABB.maxZ + playerAroundZ.get()
                    )
            );
            float amount;
            if(isAverageHealAmounts.get()){
                amount = event.getAmount() / pets.size();
            } else {
                amount = event.getAmount();
            }
            for (TameableEntity pet : pets) {
                pet.setHealth(pet.getHealth() + amount);
            }
        }
    }

    @SubscribeEvent
    public static void onPetHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof TameableEntity) {
            TameableEntity pet = (TameableEntity) event.getEntityLiving();
            if (pet.getOwner() instanceof PlayerEntity) {
                DamageSource source = event.getSource();
                double amount = event.getAmount();
                PlayerEntity player = (PlayerEntity) pet.getOwner();
                double health = player.getHealth();
                double maxHealth = player.getMaxHealth();
                if (
                        health >= maxHealth * playerHealthPercentage.get() &&
                        health > amount &&
                        isStarksPresent(player)
                ) {
                    player.hurt(source, (float) amount);
                    event.setCanceled(true);
                }
            }
        }
    }

    public Starks() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configSpec);
        ENCHANTMENT_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
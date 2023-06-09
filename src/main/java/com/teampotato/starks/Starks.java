package com.teampotato.starks;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
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
public class Starks {
    public static final String ID = "starks";
    public static final Logger LOGGER = LogManager.getLogger("Starks");

    public static final DeferredRegister<Enchantment> ENCHANTMENT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ID);

    @SuppressWarnings("unused")
    public static final RegistryObject<Enchantment> STARKS = ENCHANTMENT_DEFERRED_REGISTER.register("starks", StarksEnchantment::new);

    public Starks() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configSpec);
        ENCHANTMENT_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ForgeConfigSpec configSpec;
    public static ForgeConfigSpec.BooleanValue isTradeable, isCurse, isTreasureOnly, isDiscoverable, isAllowedOnBooks, averageHealAmounts;
    public static ForgeConfigSpec.ConfigValue<String> rarity;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> validEquipmentSlotTypes;
    public static ForgeConfigSpec.ConfigValue<Float> playerHealthPercentage;
    public static ForgeConfigSpec.DoubleValue playerAroundX, playerAroundY, playerAroundZ;

    public static final List<String> validEquipmentSlotTypeList = Lists.newArrayList("MAINHAND", "OFFHAND", "HEAD", "CHEST", "LEGS", "FEET");

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
        playerHealthPercentage = builder
                .comment("If player's current health is less than 'maxHealth * playerHealthPercentage', players will not take damage for their pets")
                .defineInRange("playerHealthPercentage", 0.3F, 0.0F, 1.0F, Float.class);
        builder.comment("Enchantment's properties");
        isTradeable = builder.define("isTradeable", true);
        isCurse = builder.define("isCurse", false);
        isTreasureOnly = builder.define("isTreasure", false);
        isDiscoverable = builder.define("canBeFoundInLoot", true);
        isAllowedOnBooks = builder.define("isAllowedOnBooks", true);
        rarity = builder
                .comment("Allowed value: COMMON, UNCOMMON, RARE, VERY_RARE")
                .define("rarity", "UNCOMMON");
        validEquipmentSlotTypes = builder
                .comment("Allowed values: MAINHAND, OFFHAND, HEAD, CHEST, LEGS, FEET.", "Can be multiple, such as [\"HEAD\", \"FEET\"]")
                .defineList("validEquipmentSlotTypes", Lists.newArrayList("HEAD"), o -> o instanceof String && validEquipmentSlotTypeList.contains((String) o));
        averageHealAmounts = builder
                .comment(
                        "Your attack to creatures will heal your pets based on the damage amounts.",
                        "But if you turn this off, the damage amounts won't be average according to the pets number, but all amounts persent on each heal."
                )
                .define("averageHealAmounts", true);
        builder.pop();
        configSpec = builder.build();
    }

    @SubscribeEvent
    public static void onPlayerAttack(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getSource().getDirectEntity();
            if (!isStarksPresent(player)) return;
            List<TameableEntity> pets = player.level.getEntitiesOfClass(TameableEntity.class, player.getBoundingBox().expandTowards(playerAroundX.get(), playerAroundY.get(), playerAroundZ.get()));
            float amount;
            if(averageHealAmounts.get()){
                amount = event.getAmount() / pets.size();
            } else {
                amount = event.getAmount();
            }
            for (TameableEntity pet : pets) {
                pet.setHealth(pet.getHealth() + amount);
            }
        }
    }

    public static boolean isStarksPresent(PlayerEntity player) {
        if (player.getMainHandItem().getEnchantmentTags().toString().contains("starks") || player.getOffhandItem().getEnchantmentTags().toString().contains("starks")) return true;
        for (ItemStack armorSlot : player.getArmorSlots()) {
            if (armorSlot.getEnchantmentTags().toString().contains("starks")) return true;
        }
        return false;
    }
}
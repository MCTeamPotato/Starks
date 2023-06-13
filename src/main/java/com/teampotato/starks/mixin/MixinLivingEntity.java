package com.teampotato.starks.mixin;

import com.teampotato.starks.Starks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.teampotato.starks.Starks.*;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setAbsorptionAmount(F)V", ordinal = 0, shift = At.Shift.BEFORE), cancellable = true)
    private void onLivingHurt(DamageSource source, float amount, CallbackInfo ci) {
        if (source.getSource() instanceof PlayerEntity player) {
            if (!Starks.isStarksPresent(player)) return;
            Box playerAABB = player.getBoundingBox();
            List<TameableEntity> pets = player.world.getNonSpectatingEntities(TameableEntity.class,
                    new Box(
                            playerAABB.minX - playerAroundX.get(),
                            playerAABB.minY - playerAroundY.get(),
                            playerAABB.minZ - playerAroundZ.get(),
                            playerAABB.maxX + playerAroundX.get(),
                            playerAABB.maxY + playerAroundY.get(),
                            playerAABB.maxZ + playerAroundZ.get()
                    )
            );
            float healAmount;
            if(isAverageHealAmounts.get()) {
                healAmount = amount / pets.size();
            } else {
                healAmount = amount;
            }
            for (TameableEntity pet : pets) {
                pet.setHealth(pet.getHealth() + healAmount);
                ci.cancel();
            }
        }

        if (((LivingEntity)(Object)this) instanceof TameableEntity pet) {
            if (pet.getOwner() instanceof PlayerEntity player && !invalidDamageSourceTypes.get().contains(source.getName())) {
                double health = player.getHealth();
                double maxHealth = player.getMaxHealth();
                if (
                        health >= maxHealth * playerHealthPercentage.get() &&
                        health > (double) amount &&
                        isStarksPresent(player)
                ) {
                    player.damage(source, amount);
                    if (!ci.isCancelled()) ci.cancel();
                }
            }
        }
    }
}
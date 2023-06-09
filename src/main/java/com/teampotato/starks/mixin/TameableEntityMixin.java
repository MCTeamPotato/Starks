package com.teampotato.starks.mixin;

import com.teampotato.starks.Starks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin extends Entity {
    @Shadow @Nullable public abstract LivingEntity getOwner();

    public TameableEntityMixin(EntityType<?> pType, World pLevel) {
        super(pType, pLevel);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getOwner() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) this.getOwner();
            float health = player.getHealth();
            if (health >= player.getMaxHealth() * Starks.playerHealthPercentage.get() && health > amount && Starks.isStarksPresent(player)) {
                player.hurt(source, amount);
                return false;
            }
        }
        return super.hurt(source, amount);
    }
}

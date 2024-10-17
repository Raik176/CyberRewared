package org.rhm.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rhm.CyberRewaredMod;
import org.rhm.registries.EntityRegistry;
import org.rhm.util.config.Config;

import java.util.Objects;

public class CyberzombieEntity extends Zombie {
    private static final EntityDataAccessor<Boolean> BRUTE = SynchedEntityData.defineId(CyberzombieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final ResourceLocation BRUTE_BONUS = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "brute_bonus");
    // originally was 1.2f
    private static final float BRUTE_SCALE = 1.1f;

    public CyberzombieEntity(Level world) {
        this(EntityRegistry.CYBERZOMBIE, world);
    }

    public CyberzombieEntity(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);
        this.entityData.set(BRUTE, false);
    }

    public static AttributeSupplier.Builder createCyberzombieAttributes() {
        AttributeSupplier.Builder builder = Zombie.createAttributes();
        builder.add(Attributes.MOVEMENT_SPEED, 0.27); // this is not in the base mod but i think it fits
        return builder;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BRUTE, false);
    }

    @Override
    public void setBaby(boolean baby) {
        super.setBaby(baby);
        if (baby) this.setBrute(false);
    }

    @Override
    public float getScale() {
        return isBrute() ? BRUTE_SCALE : 1;
    }

    @Override
    protected boolean convertsInWater() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("IsBrute", isBrute());
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull ServerLevel level, @NotNull DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
    }

    @Override
    protected @NotNull ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setBrute(nbt.getBoolean("IsBrute"));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        SpawnGroupData data = super.finalizeSpawn(world, difficulty, spawnReason, entityData);
        if (this.level().getRandom().nextFloat() < Config.getCast(Config.NATURAL_BRUTE_CHANCE, Float.class) / 100) {
            setBrute(true);
        }
        this.setHealth(this.getMaxHealth());
        return data;
    }

    public boolean isBrute() {
        return this.getEntityData().get(BRUTE);
    }

    public void setBrute(boolean brute) {
        if (!this.level().isClientSide) {
            // 15 hearts for brutes, original has 13 but 15 is better i think.
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).removeModifier(BRUTE_BONUS);
            Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).removeModifier(BRUTE_BONUS);
            if (brute) {
                this.entityData.set(BRUTE, true);
                this.refreshDimensions();
                this.setBaby(false);
                Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier(
                    BRUTE_BONUS,
                    10,
                    AttributeModifier.Operation.ADD_VALUE
                ));
                Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).addPermanentModifier(new AttributeModifier(
                    BRUTE_BONUS,
                    1,
                    AttributeModifier.Operation.ADD_VALUE
                ));
            } else {
                this.entityData.set(BRUTE, false);
            }
        }
    }
}

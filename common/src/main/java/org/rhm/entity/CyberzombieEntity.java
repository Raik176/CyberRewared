package org.rhm.entity;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.rhm.CyberRewaredMod;

public class CyberzombieEntity extends ZombieEntity {
    private static final TrackedData<Boolean> BRUTE = DataTracker.registerData(CyberzombieEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final Identifier BRUTE_BONUS = Identifier.of(CyberRewaredMod.MOD_ID, "brute_bonus");
    // originally was 1.2f
    private static final float BRUTE_SCALE = 1.1f;

    public CyberzombieEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createCyberzombieAttributes() {
        DefaultAttributeContainer.Builder builder = ZombieEntity.createZombieAttributes();
        builder.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.27); // this is not in the base mod but i think it fits
        return builder;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);

        builder.add(BRUTE, false);
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
    protected boolean canConvertInWater() {
        return false;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsBrute", isBrute());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setBrute(nbt.getBoolean("IsBrute"));
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
        if (this.getWorld().getRandom().nextFloat() < CyberRewaredMod.NATURAL_BRUTE_CHANCE / 100) {
            setBrute(true);
        }
        this.setHealth(this.getMaxHealth());
        return data;
    }

    public boolean isBrute() {
        return this.dataTracker != null && this.getDataTracker().get(BRUTE);
    }

    public void setBrute(boolean brute) {
        if (this.getWorld() != null && !this.getWorld().isClient) {
            // 15 hearts for brutes, original has 13 but 15 is better i think.
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).removeModifier(BRUTE_BONUS);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).removeModifier(BRUTE_BONUS);
            if (brute) {
                this.dataTracker.set(BRUTE, true);
                this.calculateDimensions();
                this.setBaby(false);
                this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(new EntityAttributeModifier(
                    BRUTE_BONUS,
                    10,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ));
                this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(new EntityAttributeModifier(
                    BRUTE_BONUS,
                    1,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ));
            } else {
                this.dataTracker.set(BRUTE, false);
            }
        }
    }
}

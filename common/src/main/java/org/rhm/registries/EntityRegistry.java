package org.rhm.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import org.rhm.CyberRewaredMod;
import org.rhm.entity.CyberzombieEntity;

import java.awt.*;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class EntityRegistry {
    public static final EntityType<CyberzombieEntity> CYBERZOMBIE = register("cyberzombie", (EntityFactory<CyberzombieEntity>) CyberzombieEntity::new,
        new EntityTypeBuilder().setDimension(0.6f, 1.95f).setEyeHeight(1.74f)
            .setOffsets(2.0125f, -0.7f).setMaxTrackingRange(8),
        CyberzombieEntity::createCyberzombieAttributes,
        new AbstractMap.SimpleEntry<>(Color.decode("#383636"), Color.decode("#5b362a"))
    );

    public static <T extends Mob> EntityType<T> register(String path, EntityFactory<T> ef, EntityTypeBuilder builder, Supplier<AttributeSupplier.Builder> attributeBuilder, Map.Entry<Color, Color> spawnEggColor) {
        EntityType<?> type = CyberRewaredMod.entityRegisterFunc.apply(ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, path), ef, builder, attributeBuilder);
        if (spawnEggColor != null) {
            ItemRegistry.register(path + "_spawn_egg", new SpawnEggItem((EntityType<? extends Mob>) type, spawnEggColor.getKey().getRGB(), spawnEggColor.getValue().getRGB(), new Item.Properties()));
        }
        return (EntityType<T>) type;
    }

    public static void initialize() {

    }

    @FunctionalInterface
    public interface EntityFactory<T extends LivingEntity> {
        T create(EntityType<? extends T> entityType, Level world);
    }
    public static class EntityTypeBuilder {
        private float width, height;
        private float eyeHeight;
        private float passengerOffsetY, vehicleOffsetY;
        private int maxTrackingRange = 0;

        public EntityTypeBuilder() {
            width = height = eyeHeight = 1;
            passengerOffsetY = vehicleOffsetY = 0;
            maxTrackingRange = 0;
        }

        public EntityTypeBuilder setDimension(float width, float height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public EntityTypeBuilder setOffsets(float passengerOffsetY, float vehicleOffsetY) {
            this.passengerOffsetY = passengerOffsetY;
            this.vehicleOffsetY = vehicleOffsetY;
            return this;
        }

        public float getWidth() {
            return width;
        }

        public EntityTypeBuilder setWidth(float width) {
            this.width = width;
            return this;
        }

        public float getHeight() {
            return height;
        }

        public EntityTypeBuilder setHeight(float height) {
            this.height = height;
            return this;
        }

        public float getEyeHeight() {
            return eyeHeight;
        }

        public EntityTypeBuilder setEyeHeight(float eyeHeight) {
            this.eyeHeight = eyeHeight;
            return this;
        }

        public float getPassengerOffsetY() {
            return passengerOffsetY;
        }

        public EntityTypeBuilder setPassengerOffsetY(float passengerOffsetY) {
            this.passengerOffsetY = passengerOffsetY;
            return this;
        }

        public float getVehicleOffsetY() {
            return vehicleOffsetY;
        }

        public EntityTypeBuilder setVehicleOffsetY(float vehicleOffsetY) {
            this.vehicleOffsetY = vehicleOffsetY;
            return this;
        }

        public int getMaxTrackingRange() {
            return maxTrackingRange;
        }

        public EntityTypeBuilder setMaxTrackingRange(int maxTrackingRange) {
            this.maxTrackingRange = maxTrackingRange;
            return this;
        }
    }
}

package org.rhm.registries;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.function.TriConsumer;
import org.rhm.CyberRewaredMod;
import org.rhm.gui.EngineeringTableScreenHandler;
import org.rhm.network.EngineeringTableSmashPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PacketRegistry {
    private static final Map<CustomPayload.Id<?>, TriConsumer<CustomPayload, ClientPlayerEntity,MinecraftClient>> clientHandlers = new HashMap<>();

    public static BiConsumer<ServerPlayerEntity, CustomPayload> sendPacketS2C;
    public static Consumer<CustomPayload> sendPacketC2S;
    public static final CustomPayload.Id<? extends CustomPayload> ENGINEERING_SMASH_PACKET = registerC2S(
        Identifier.of(CyberRewaredMod.MOD_ID, "engineering_smash"),
        EngineeringTableSmashPacket.CODEC,
        (payload, player, instance) -> {
            if (player.currentScreenHandler instanceof EngineeringTableScreenHandler etsh) {
                etsh.smash(player, (EngineeringTableSmashPacket)payload);
            }
        }
    );
    /*
    public static final CustomPayload.Id<? extends CustomPayload> ENGINEERING_ADDON_PACKET = registerS2C(
        Identifier.of(CyberRewaredMod.MOD_ID, "engineering_addon"),
        EngineeringTableGuiAddonPacket.PACKET_CODEC,
        (payload, player, instance) -> {
            EngineeringTableGuiAddonPacket packet = (EngineeringTableGuiAddonPacket) payload;
            if (instance.currentScreen instanceof EngineeringTableScreen ets) {

            }
        }
    );
     */

    public static <T extends CustomPayload> CustomPayload.Id<T> registerS2C(Identifier id, PacketCodec<RegistryByteBuf, T> codec, TriConsumer<CustomPayload, ClientPlayerEntity, MinecraftClient> handler) {
        CustomPayload.Id<T> packetId = new CustomPayload.Id<>(id);
        CyberRewaredMod.packetS2CRegisterFunc.accept(packetId, codec);
        clientHandlers.put(packetId, handler);
        return packetId;
    }
    public static <T extends CustomPayload> CustomPayload.Id<T> registerC2S(Identifier id, PacketCodec<RegistryByteBuf, T> codec, TriConsumer<CustomPayload, ServerPlayerEntity, MinecraftServer> handler) {
        CustomPayload.Id<T> packetId = new CustomPayload.Id<>(id);
        CyberRewaredMod.packetC2SRegisterFunc.accept(packetId, codec);
        CyberRewaredMod.packetC2SHandlerRegisterFunc.accept(packetId, handler);
        return packetId;
    }

    public static void initialize() {

    }
    public static void initializeClient() {
        clientHandlers.forEach(((id, handler) -> {
            CyberRewaredMod.packetS2CHandlerRegisterFunc.accept(id, handler);
        }));
    }
}

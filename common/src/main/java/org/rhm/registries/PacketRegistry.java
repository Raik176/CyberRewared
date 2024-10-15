package org.rhm.registries;

import commonnetwork.api.Network;
import org.rhm.network.EngineeringTableInfoPacket;
import org.rhm.network.EngineeringTableSmashPacket;
import org.rhm.network.ScannerProgressPacket;

public class PacketRegistry {
    /*
    private static final Map<CustomPacketPayload.Type<?>, TriConsumer<CustomPacketPayload, LocalPlayer,Minecraft>> clientHandlers = new HashMap<>();

    public static BiConsumer<ServerPlayer, CustomPacketPayload> sendPacketS2C;
    public static Consumer<CustomPacketPayload> sendPacketC2S;
    public static final CustomPacketPayload.Type<? extends CustomPacketPayload> ENGINEERING_SMASH_PACKET = registerC2S(
        ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "engineering_smash"),
        EngineeringTableSmashPacket.class,
        EngineeringTableSmashPacket::encode,

        (payload, player, instance) -> {
            if (player.containerMenu instanceof EngineeringTableScreenHandler etsh) {
                etsh.smash(player, (EngineeringTableSmashPacket)payload);
            }
        }
    );
     */

    public static void initialize() {
        Network
            .registerPacket(EngineeringTableSmashPacket.type(), EngineeringTableSmashPacket.class, EngineeringTableSmashPacket.CODEC, EngineeringTableSmashPacket::handle)
            .registerPacket(ScannerProgressPacket.type(), ScannerProgressPacket.class, ScannerProgressPacket.CODEC, ScannerProgressPacket::handle)
            .registerPacket(EngineeringTableInfoPacket.type(), EngineeringTableInfoPacket.class, EngineeringTableInfoPacket.CODEC, EngineeringTableInfoPacket::handle);
    }

    public static void initializeClient() {

    }
}

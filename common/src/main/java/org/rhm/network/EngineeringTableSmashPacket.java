package org.rhm.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.rhm.registries.PacketRegistry;

public record EngineeringTableSmashPacket(boolean craftAll) implements CustomPayload {
    public static final PacketCodec<RegistryByteBuf, EngineeringTableSmashPacket> CODEC = PacketCodec.tuple(
        PacketCodecs.BOOL,
        EngineeringTableSmashPacket::craftAll,
        EngineeringTableSmashPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PacketRegistry.ENGINEERING_SMASH_PACKET;
    }
}

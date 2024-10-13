package org.rhm.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record EngineeringTableGuiAddonPacket(boolean hasBlueprint, BlockPos bap, boolean hasComponent, BlockPos cbp) implements CustomPayload {
    public static final PacketCodec<RegistryByteBuf, EngineeringTableGuiAddonPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.BOOL, EngineeringTableGuiAddonPacket::hasBlueprint,
        BlockPos.PACKET_CODEC, EngineeringTableGuiAddonPacket::bap,
        PacketCodecs.BOOL, EngineeringTableGuiAddonPacket::hasComponent,
        BlockPos.PACKET_CODEC, EngineeringTableGuiAddonPacket::cbp,
        EngineeringTableGuiAddonPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return null;
        //return PacketRegistry.ENGINEERING_ADDON_PACKET;
    }
}

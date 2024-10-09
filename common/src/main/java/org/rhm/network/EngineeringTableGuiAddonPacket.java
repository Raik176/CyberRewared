package org.rhm.network;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

@SuppressWarnings("rawtypes")
public class EngineeringTableGuiAddonPacket implements CustomPayload {
    public static final CustomPayload.Id<EngineeringTableGuiAddonPacket> ID = CustomPayload.id("engineering_gui_addon");
    public static final PacketCodec<PacketByteBuf, EngineeringTableGuiAddonPacket> STREAM_CODEC =
        PacketCodec.ofStatic(EngineeringTableGuiAddonPacket::encode, EngineeringTableGuiAddonPacket::new);

    public EngineeringTableGuiAddonPacket(PacketByteBuf buf) {

    }

    private static void encode(PacketByteBuf packetByteBuf, EngineeringTableGuiAddonPacket engineeringTableGuiAddonPacket) {

    }

    public static void handle(PacketContext<EngineeringTableGuiAddonPacket> ctx) {
        if (Side.CLIENT.equals(ctx.side())) {

        } else {

        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

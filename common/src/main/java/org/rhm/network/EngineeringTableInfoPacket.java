package org.rhm.network;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.rhm.gui.EngineeringTableScreen;

public record EngineeringTableInfoPacket(float successChance) {
    public static final ResourceLocation CHANNEL = ResourceLocation.fromNamespaceAndPath(
        Constants.MOD_ID,
        "engineering_info"
    );
    public static final StreamCodec<FriendlyByteBuf, EngineeringTableInfoPacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT,
        EngineeringTableInfoPacket::successChance,
        EngineeringTableInfoPacket::new
    );

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void handle(PacketContext<EngineeringTableInfoPacket> ctx) {
        if (ctx.side() != Side.CLIENT) return;
        if (Minecraft.getInstance().screen instanceof EngineeringTableScreen ets) {
            ets.chance = ctx.message().successChance;
        }
    }
}

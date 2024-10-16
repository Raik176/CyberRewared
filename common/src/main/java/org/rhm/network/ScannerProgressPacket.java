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
import org.rhm.gui.ScannerScreen;

public record ScannerProgressPacket(int containerId, long currentTicks, long maxTicks, float successChance) {
    public static final ResourceLocation CHANNEL = ResourceLocation.fromNamespaceAndPath(
        Constants.MOD_ID,
        "scanner_progress"
    );
    public static final StreamCodec<FriendlyByteBuf, ScannerProgressPacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        ScannerProgressPacket::containerId,
        ByteBufCodecs.VAR_LONG,
        ScannerProgressPacket::currentTicks,
        ByteBufCodecs.VAR_LONG,
        ScannerProgressPacket::maxTicks,
        ByteBufCodecs.FLOAT,
        ScannerProgressPacket::successChance,
        ScannerProgressPacket::new
    );

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void handle(PacketContext<ScannerProgressPacket> ctx) {
        if (ctx.side() != Side.CLIENT) return;
        if (Minecraft.getInstance().screen instanceof ScannerScreen ss && ss.getContainerId() == ctx.message().containerId) {
            ss.scanTime = ctx.message().currentTicks;
            ss.scanTimeMax = ctx.message().maxTicks;
            ss.chance = ctx.message().successChance;
        }
    }
}

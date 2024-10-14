package org.rhm.network;

import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.rhm.gui.EngineeringTableScreenHandler;

public record EngineeringTableSmashPacket(boolean craftAll) {
    public static final ResourceLocation CHANNEL = ResourceLocation.fromNamespaceAndPath(
        Constants.MOD_ID,
        "engineering_smash"
    );
    public static final StreamCodec<FriendlyByteBuf, EngineeringTableSmashPacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL,
        EngineeringTableSmashPacket::craftAll,
        EngineeringTableSmashPacket::new
    );

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void handle(PacketContext<EngineeringTableSmashPacket> ctx) {
        if (ctx.side() != Side.SERVER) return;
        if (ctx.sender().containerMenu instanceof EngineeringTableScreenHandler etsh) {
            etsh.smash(ctx.sender(), ctx.message());
        }
    }
}

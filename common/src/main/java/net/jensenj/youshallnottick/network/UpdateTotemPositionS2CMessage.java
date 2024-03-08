package net.jensenj.youshallnottick.network;

import dev.architectury.networking.NetworkManager;
import net.jensenj.youshallnottick.YouShallNotTick;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class UpdateTotemPositionS2CMessage {
    public static final ResourceLocation PACKET_ID = new ResourceLocation(YouShallNotTick.MOD_ID, "s2c_update_totem");
    private final boolean shouldAdd;
    private final ResourceLocation dimension;
    private final BlockPos pos;

    public UpdateTotemPositionS2CMessage(FriendlyByteBuf buf) {
        this.shouldAdd = buf.readBoolean();
        this.dimension = buf.readResourceLocation();
        this.pos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(shouldAdd);
        buf.writeResourceLocation(dimension);
        buf.writeBlockPos(pos);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {

    }
}

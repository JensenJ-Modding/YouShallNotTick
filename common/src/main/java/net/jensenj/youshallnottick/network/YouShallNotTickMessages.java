package net.jensenj.youshallnottick.network;

import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import net.jensenj.youshallnottick.YouShallNotTick;
import net.jensenj.youshallnottick.registry.TickingTotemBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class YouShallNotTickMessages {
    public static final NetworkChannel CHANNEL = NetworkChannel.create(new ResourceLocation(YouShallNotTick.MOD_ID, "networking_channel"));

    public static void registerS2CMessages(){
        CHANNEL.register(UpdateTotemPositionS2CMessage.class, UpdateTotemPositionS2CMessage::encode, UpdateTotemPositionS2CMessage::new, UpdateTotemPositionS2CMessage::apply);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, UpdateTotemPositionS2CMessage.PACKET_ID, (buf, context) -> {
            boolean shouldAdd = buf.readBoolean();
            ResourceLocation dimension = buf.readResourceLocation();
            BlockPos pos = buf.readBlockPos();
            if(shouldAdd)
                TickingTotemBlockEntity.addTickingTotemPosition(dimension, pos);
            else
                TickingTotemBlockEntity.removeTickingTotemPosition(dimension, pos);
        });
    }
}

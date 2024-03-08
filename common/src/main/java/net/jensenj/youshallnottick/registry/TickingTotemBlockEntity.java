package net.jensenj.youshallnottick.registry;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.jensenj.youshallnottick.Utils;
import net.jensenj.youshallnottick.network.UpdateTotemPositionS2CMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TickingTotemBlockEntity extends BlockEntity {

    public static final Map<ResourceLocation, Set<BlockPos>> TICKING_TOTEM_LOCATIONS = new HashMap<>();

    public TickingTotemBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(YouShallNotTickRegistry.TICKING_TOTEM_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public static void ServerSendTickingTotemUpdateToClients(LevelAccessor level, BlockPos pos, boolean shouldAdd){
        ResourceLocation dimension = Utils.getDimensionLocation(level);
        if(dimension == null)
            return;
        if(level.isClientSide())
            return;

        if(shouldAdd) //Server side updating totems
            addTickingTotemPosition(dimension, pos);
        else
            removeTickingTotemPosition(dimension, pos);

        FriendlyByteBuf totemBuf = new FriendlyByteBuf(Unpooled.buffer());
        totemBuf.writeBoolean(shouldAdd);
        totemBuf.writeResourceLocation(dimension);
        totemBuf.writeBlockPos(pos);

        NetworkManager.sendToPlayers(level.players().stream()
                .filter(player -> player instanceof ServerPlayer)
                .map(player -> (ServerPlayer) player)
                .collect(Collectors.toList()), UpdateTotemPositionS2CMessage.PACKET_ID, totemBuf);
    }

    public static void addTickingTotemPosition(ResourceLocation dimension, BlockPos pos){
        if(dimension == null)
            return;
        Set<BlockPos> blockPosSet = TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.computeIfAbsent(dimension, k -> new HashSet<>());
        blockPosSet.add(pos);
    }

    public static void removeTickingTotemPosition(ResourceLocation dimension, BlockPos pos){
        if(dimension == null)
            return;
        TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.computeIfPresent(dimension, (k, blockPosSet) -> {
            blockPosSet.remove(pos);
            return blockPosSet.isEmpty() ? null : blockPosSet;
        });
    }

    public static void handleChunkLoading(LevelAccessor level, ChunkAccess chunk){
        if(level.isClientSide())
            return;
        for(BlockPos pos : chunk.getBlockEntitiesPos()){
            if(!(chunk.getBlockEntity(pos) instanceof TickingTotemBlockEntity))
                continue;
            if(chunk.getBlockState(pos).getValue(TickingTotemBlock.POWERED)) //Skip powered totems
                continue;
            ServerSendTickingTotemUpdateToClients(level, pos, true);
            return;
        }
    }

    public static void handleChunkUnloading(LevelAccessor level, ChunkAccess chunk){
        if(level.isClientSide())
            return;
        for(BlockPos pos : chunk.getBlockEntitiesPos()){
            if(!(chunk.getBlockEntity(pos) instanceof TickingTotemBlockEntity))
                continue;
            if(!chunk.getBlockState(pos).getValue(TickingTotemBlock.POWERED)) //Skip unpowered totems
                continue;
            ServerSendTickingTotemUpdateToClients(level, pos, false);
            return;
        }
    }

    public static void sendFullTotemMapToPlayer(Player player) {
        for(var dimensionBlockPosMap : TICKING_TOTEM_LOCATIONS.entrySet()){
            ResourceLocation dimension = dimensionBlockPosMap.getKey();
            for(var pos : dimensionBlockPosMap.getValue()){
                //Prepare network packet
                FriendlyByteBuf totemBuf = new FriendlyByteBuf(Unpooled.buffer());
                totemBuf.writeBoolean(true);
                totemBuf.writeResourceLocation(dimension);
                totemBuf.writeBlockPos(pos);

                //Send packet to client
                NetworkManager.sendToPlayer((ServerPlayer) player, UpdateTotemPositionS2CMessage.PACKET_ID, totemBuf);
            }
        }
    }
}

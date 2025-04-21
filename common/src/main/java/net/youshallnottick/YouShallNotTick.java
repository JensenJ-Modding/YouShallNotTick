package net.youshallnottick;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import dev.architectury.event.events.common.PlayerEvent;
import net.youshallnottick.registry.TickingTotemBlock;
import net.youshallnottick.registry.TickingTotemBlockEntity;
import net.youshallnottick.registry.YouShallNotTickRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YouShallNotTick {
    public static final String MOD_ID = "youshallnottick";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        YouShallNotTickRegistry.BLOCKS.register();
        YouShallNotTickRegistry.ITEMS.register();
        YouShallNotTickRegistry.BLOCK_ENTITIES.register();

        PlayerEvent.PLAYER_JOIN.register((ServerPlayer player) -> {
            // This sends all late joining players the current values of the server totem maps to prevent on-join
            // desync.
            Map<ResourceLocation, Set<BlockPos>> allTotems = new HashMap<>();
            for (Map.Entry<ResourceLocation, Set<BlockPos>> entry :
                    TickingTotemBlockEntity.ACTIVE_TICKING_TOTEMS.entrySet()) {
                allTotems.put(entry.getKey(), new HashSet<>(entry.getValue()));
            }

            for (Map.Entry<ResourceLocation, Set<BlockPos>> entry :
                    TickingTotemBlockEntity.OUTLINED_TICKING_TOTEMS.entrySet()) {
                allTotems.merge(entry.getKey(), new HashSet<>(entry.getValue()), (set1, set2) -> {
                    set1.addAll(set2);
                    return set1;
                });
            }

            for (Map.Entry<ResourceLocation, Set<BlockPos>> entry : allTotems.entrySet()) {
                ResourceLocation dim = entry.getKey();
                for (BlockPos pos : entry.getValue()) {
                    TickingTotemBlock.sendDataToClient(
                            dim,
                            player.level(),
                            pos,
                            TickingTotemBlockEntity.ACTIVE_TICKING_TOTEMS
                                    .get(dim)
                                    .contains(pos),
                            TickingTotemBlockEntity.OUTLINED_TICKING_TOTEMS
                                    .get(dim)
                                    .contains(pos));
                }
            }
        });
    }
}

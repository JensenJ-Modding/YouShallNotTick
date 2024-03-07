package net.jensenj.youshallnottick.mixin;

import net.jensenj.youshallnottick.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.jensenj.youshallnottick.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@SuppressWarnings("unused")
@Mixin(value = NaturalSpawner.class, priority = 1100)
public class EntitySpawnMixin {
    @Inject(at = @At("HEAD"), method = "getRandomSpawnMobAt", cancellable = true)
    private static void youshallnottick$getRandomSpawnMobAt(ServerLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, MobCategory mobCategory, RandomSource randomSource, BlockPos blockPos, CallbackInfoReturnable<Optional<MobSpawnSettings.SpawnerData>> cir) {
        if (Utils.enoughPlayers(level) && ServerConfig.shouldEnableSpawnMixin.get()) {
            int playerHorizontalDist = ServerConfig.playerMaxEntitySpawnHorizontalDist.get();
            int playerVerticalDist = ServerConfig.playerMaxEntitySpawnVerticalDist.get();
            int totemHorizontalDist = ServerConfig.totemMaxEntitySpawnHorizontalDist.get();
            int totemVerticalDist = ServerConfig.totemMaxEntitySpawnVerticalDist.get();
            if (!Utils.isNearPlayer(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), playerHorizontalDist, playerVerticalDist, totemHorizontalDist, totemVerticalDist)) {
                cir.setReturnValue(Optional.empty());
            }
        }
    }
}

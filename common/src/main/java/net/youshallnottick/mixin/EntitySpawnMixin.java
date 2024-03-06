package net.youshallnottick.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.youshallnottick.Config;
import net.youshallnottick.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Random;

@Mixin(value = NaturalSpawner.class, priority = 1100)
public class EntitySpawnMixin {
    @Inject(at = @At("HEAD"), method = "getRandomSpawnMobAt", cancellable = true)
    private static void getRandomSpawnMobAt(ServerLevel level, StructureFeatureManager arg2, ChunkGenerator arg3, MobCategory arg4, Random arg5, BlockPos blockPos, CallbackInfoReturnable<Optional<MobSpawnSettings.SpawnerData>> cir) {
        if (!Utils.isInExemptChunk(level, blockPos) && Utils.enoughPlayers(level)) {
            int maxHorizontalDist = Config.maxEntitySpawnDistanceHorizontal.get();
            int maxVerticalDist = Config.maxEntitySpawnDistanceVertical.get();

            if (!Utils.isNearPlayer(level, blockPos, maxHorizontalDist, maxVerticalDist)) {
                cir.setReturnValue(Optional.empty());
            }
        }
    }
}

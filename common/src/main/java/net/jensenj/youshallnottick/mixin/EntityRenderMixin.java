package net.jensenj.youshallnottick.mixin;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.jensenj.youshallnottick.config.ClientConfig;
import net.jensenj.youshallnottick.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityRenderDispatcher.class, priority = 1100)
public class EntityRenderMixin {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void youshallnottick$shouldRenderEntities(E entity, Frustum clippingHelper, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir) {
        if(shouldCancelRender(entity, clippingHelper, cameraX, cameraY, cameraZ)){
            cir.cancel();
        }
    }

    private static boolean shouldCancelRender(Entity entity, Frustum clippingHelper, double cameraX, double cameraY, double cameraZ){
        //If the client should render entities, we should not cancel rendering
        if(ClientConfig.shouldRenderFrozenEntities.get())
            return false;

        //If there is not enough players for the mod to activate, then we should not cancel rendering
        if(!Utils.enoughPlayers(entity.level))
            return false;

        //If the entity is ignored, we should not cancel rendering
        if(Utils.isIgnoredEntity(entity))
            return false;

        //If the entity is not within distance we should cancel rendering
        return !Utils.isEntityWithinDistance(entity, cameraX, cameraY, cameraZ);
    }

}

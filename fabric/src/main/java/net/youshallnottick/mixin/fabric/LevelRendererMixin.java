package net.youshallnottick.mixin.fabric;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import net.youshallnottick.YouShallNotTickClient;
import org.joml.Matrix4f;

@Mixin(value = LevelRenderer.class, priority = 10100)
public abstract class LevelRendererMixin {

    // This injection point is identical to where Forge's AFTER_SOLID_BLOCKS render stage is
    @Inject(
            method = "renderChunkLayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;clearRenderState()V"))
    private void youshallnottick$renderOutlines(
            RenderType renderType,
            PoseStack poseStack,
            double d,
            double e,
            double f,
            Matrix4f matrix4f,
            CallbackInfo ci) {
        YouShallNotTickClient.renderTotemOutlines(poseStack);
    }
}

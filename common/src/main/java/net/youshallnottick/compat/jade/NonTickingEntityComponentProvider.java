package net.youshallnottick.compat.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import net.youshallnottick.Utils;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum NonTickingEntityComponentProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if (entityAccessor.getServerData().contains("Ticking")) {
            if (!entityAccessor.getServerData().getBoolean("Ticking")) {
                iTooltip.add(Component.translatable("tooltip.youshallnottick.nontickingentity")
                        .withStyle(style -> style.withColor(ChatFormatting.DARK_RED)));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, EntityAccessor entityAccessor) {
        Entity entity = entityAccessor.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            compoundTag.putBoolean("Ticking", Utils.shouldProcessEntityTick(livingEntity));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return YouShallNotTickJadePlugin.NON_TICKING_ENTITY;
    }
}

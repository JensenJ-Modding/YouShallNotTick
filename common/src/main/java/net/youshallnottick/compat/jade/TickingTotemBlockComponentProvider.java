package net.youshallnottick.compat.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.youshallnottick.config.ServerConfig;
import net.youshallnottick.registry.TickingTotemBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum TickingTotemBlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("Enabled")) {
            if (!blockAccessor.getServerData().getBoolean("Enabled")) {
                iTooltip.add(Component.translatable("tooltip.youshallnottick.ticking_totem.disabled")
                        .withStyle(style -> style.withColor(ChatFormatting.DARK_RED)));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        BlockEntity block = blockAccessor.getBlockEntity();
        if (block instanceof TickingTotemBlockEntity) {
            compoundTag.putBoolean("Enabled", ServerConfig.shouldEnableTotemOfTicking.get());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return YouShallNotTickJadePlugin.TICKING_TOTEM;
    }
}

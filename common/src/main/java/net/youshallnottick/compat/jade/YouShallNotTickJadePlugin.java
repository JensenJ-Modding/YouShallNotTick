package net.youshallnottick.compat.jade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import net.youshallnottick.YouShallNotTick;
import net.youshallnottick.registry.TickingTotemBlock;
import net.youshallnottick.registry.TickingTotemBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class YouShallNotTickJadePlugin implements IWailaPlugin {

    public static final ResourceLocation NON_TICKING_ENTITY =
            ResourceLocation.fromNamespaceAndPath(YouShallNotTick.MOD_ID, "nontickingentity");
    public static final ResourceLocation TICKING_TOTEM =
            ResourceLocation.fromNamespaceAndPath(YouShallNotTick.MOD_ID, "tickingtotem");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(NonTickingEntityComponentProvider.INSTANCE, LivingEntity.class);
        registration.registerBlockDataProvider(
                TickingTotemBlockComponentProvider.INSTANCE, TickingTotemBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(NonTickingEntityComponentProvider.INSTANCE, LivingEntity.class);
        registration.registerBlockComponent(TickingTotemBlockComponentProvider.INSTANCE, TickingTotemBlock.class);
    }
}

package net.jensenj.youshallnottick.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
public class UtilsImpl {

    public static ResourceLocation getEntityRegistrationLocation(Entity entity){
        return ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
    }
}

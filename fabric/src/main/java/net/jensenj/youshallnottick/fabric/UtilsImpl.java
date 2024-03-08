package net.jensenj.youshallnottick.fabric;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

@SuppressWarnings("unused")
public class UtilsImpl {

    public static ResourceLocation getEntityRegistrationLocation(Entity entity){
        return BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
    }
}

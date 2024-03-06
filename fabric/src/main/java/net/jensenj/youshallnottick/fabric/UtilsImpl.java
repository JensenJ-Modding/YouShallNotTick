package net.jensenj.youshallnottick.fabric;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class UtilsImpl {

    public static ResourceLocation getEntityRegistrationLocation(Entity entity){
        return Registry.ENTITY_TYPE.getKey(entity.getType());
    }
}

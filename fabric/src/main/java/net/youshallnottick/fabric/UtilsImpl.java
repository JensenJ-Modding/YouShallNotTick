package net.youshallnottick.fabric;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class UtilsImpl {

    public static void handleGuardEntityTick(Consumer<Entity> consumer, Entity entity) {
        try {
            consumer.accept(entity);
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Ticking entity");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being ticked");
            entity.fillCrashReportCategory(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    public static ResourceLocation getEntityRegistrationLocation(Entity entity){
        return Registry.ENTITY_TYPE.getKey(entity.getType());
    }
}

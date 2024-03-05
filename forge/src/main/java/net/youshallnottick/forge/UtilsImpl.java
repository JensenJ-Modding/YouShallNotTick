package net.youshallnottick.forge;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.timings.TimeTracker;
import net.youshallnottick.Config;

import java.util.function.Consumer;

import static net.youshallnottick.Utils.isIgnored;

public class UtilsImpl {

    public static void handleGuardEntityTick(Consumer<Entity> consumer, Entity entity) {
        try {
            TimeTracker.ENTITY_UPDATE.trackStart(entity);
            consumer.accept(entity);
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Ticking entity");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being ticked");
            entity.fillCrashReportCategory(crashreportcategory);
            throw new ReportedException(crashreport);
        }
        finally {
            TimeTracker.ENTITY_UPDATE.trackEnd(entity);
        }
    }

    public static ResourceLocation getEntityRegistrationLocation(Entity entity){
        return ForgeRegistries.ENTITIES.getKey(entity.getType());
    }
}

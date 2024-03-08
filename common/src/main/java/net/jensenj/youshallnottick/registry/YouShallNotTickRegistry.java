package net.jensenj.youshallnottick.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.jensenj.youshallnottick.YouShallNotTick;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class YouShallNotTickRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(YouShallNotTick.MOD_ID, Registry.ITEM_REGISTRY);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(YouShallNotTick.MOD_ID, Registry.BLOCK_REGISTRY);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(YouShallNotTick.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final RegistrySupplier<Block> TICKING_TOTEM_BLOCK = registerTotemBlock(
            () -> new TickingTotemBlock(BlockBehaviour.Properties.copy(Blocks.CHEST)
                    .strength(0.5f, 5.0f)
                    .sound(SoundType.WOOD)
                    .noOcclusion())
    );

    public static final RegistrySupplier<BlockEntityType<TickingTotemBlockEntity>> TICKING_TOTEM_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ticking_totem", () -> BlockEntityType.Builder.of(TickingTotemBlockEntity::new, TICKING_TOTEM_BLOCK.get()).build(null));

    private static <T extends Block> RegistrySupplier<T> registerTotemBlock(Supplier<T> block){
        RegistrySupplier<T> toReturn = BLOCKS.register("ticking_totem", block);
        registerTotemBlockItem(toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerTotemBlockItem(RegistrySupplier<T> block){
        ITEMS.register("ticking_totem", () -> new BlockItem(block.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    }
}


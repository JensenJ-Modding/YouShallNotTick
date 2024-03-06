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
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(YouShallNotTick.MOD_ID, net.minecraft.core.Registry.BLOCK_REGISTRY);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(YouShallNotTick.MOD_ID, net.minecraft.core.Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final RegistrySupplier<Block> TICKING_TOTEM_BLOCK = registerBlock("ticking_totem",
            () -> new TickingTotemBlock(BlockBehaviour.Properties.copy(Blocks.CHEST)
                    .strength(0.5f, 5.0f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()),
            CreativeModeTab.TAB_MISC);
    public static final RegistrySupplier<BlockEntityType<TickingTotemBlockEntity>> TICKING_TOTEM_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ticking_totem", () -> BlockEntityType.Builder.of(TickingTotemBlockEntity::new, TICKING_TOTEM_BLOCK.get()).build(null));



    private static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistrySupplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistrySupplier<T> block, CreativeModeTab tab){
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
}


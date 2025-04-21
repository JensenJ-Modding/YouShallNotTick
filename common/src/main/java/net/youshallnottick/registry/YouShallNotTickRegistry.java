package net.youshallnottick.registry;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.youshallnottick.YouShallNotTick;

public class YouShallNotTickRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(YouShallNotTick.MOD_ID, Registries.ITEM);
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(YouShallNotTick.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(YouShallNotTick.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static final RegistrySupplier<Block> TICKING_TOTEM_BLOCK =
            registerTotemBlock(() -> new TickingTotemBlock(BlockBehaviour.Properties.copy(Blocks.CHEST)
                    .strength(0.5f, 5.0f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));
    public static final RegistrySupplier<BlockEntityType<TickingTotemBlockEntity>> TICKING_TOTEM_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ticking_totem", () -> BlockEntityType.Builder.of(
                            TickingTotemBlockEntity::new, TICKING_TOTEM_BLOCK.get())
                    .build(null));

    public static final ResourceLocation UPDATE_TICKING_TOTEM_PACKET_ID =
            new ResourceLocation(YouShallNotTick.MOD_ID, "update_ticking_totem");

    private static <T extends Block> RegistrySupplier<T> registerTotemBlock(Supplier<T> block) {
        RegistrySupplier<T> toReturn = BLOCKS.register("ticking_totem", block);
        registerTotemBlockItem(toReturn);
        return toReturn;
    }

    @SuppressWarnings("all")
    private static <T extends Block> void registerTotemBlockItem(RegistrySupplier<T> block) {
        ITEMS.register(
                "ticking_totem",
                () -> new BlockItem(block.get(), new Item.Properties().arch$tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)));
    }

    // Should only be called from the client side registration
    public static void registerS2CPackets() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, UPDATE_TICKING_TOTEM_PACKET_ID, (buf, context) -> {
            ResourceLocation dim = buf.readResourceLocation();
            BlockPos pos = buf.readBlockPos();
            boolean active = buf.readBoolean();
            boolean outlined = buf.readBoolean();

            YouShallNotTick.LOGGER.info("Updated ticking totem on client: {} {} {}", pos, active, outlined);

            if (active) TickingTotemBlockEntity.addActiveTickingTotem(dim, pos);
            else TickingTotemBlockEntity.removeActiveTickingTotem(dim, pos);

            if (outlined) TickingTotemBlockEntity.addOutlinedTickingTotem(dim, pos);
            else TickingTotemBlockEntity.removeOutlinedTickingTotem(dim, pos);

            YouShallNotTick.LOGGER.info("ACTIVE: {}", TickingTotemBlockEntity.ACTIVE_TICKING_TOTEMS);
            YouShallNotTick.LOGGER.info("OUTLINED: {}", TickingTotemBlockEntity.OUTLINED_TICKING_TOTEMS);
        });
    }
}

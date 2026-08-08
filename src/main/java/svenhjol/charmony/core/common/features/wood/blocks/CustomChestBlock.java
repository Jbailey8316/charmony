package svenhjol.charmony.core.common.features.wood.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.api.core.FuelProvider;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.WoodRegistry;
import svenhjol.charmony.core.common.features.wood.blocks.entity.CustomChestBlockEntity;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CustomChestBlock extends ChestBlock {
    private final WoodMaterial material;

    public CustomChestBlock(net.minecraft.resources.ResourceKey<Block> key, WoodMaterial material) {
        super(WoodRegistry::chestBlockEntity, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE,
            Properties.ofFullCopy(Blocks.CHEST).setId(key));
        this.material = material;
    }

    public WoodMaterial getMaterial() {
        return material;
    }

    @Override
    public boolean chestCanConnectTo(BlockState other) {
        return other.getBlock() instanceof CustomChestBlock chest
            && chest.material == material;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CustomChestBlockEntity(WoodRegistry.chestBlockEntity(), pos, state);
    }

    public static class ChestBlockItem extends BlockItem implements FuelProvider {
        private final WoodMaterial material;

        public ChestBlockItem(net.minecraft.resources.ResourceKey<Item> key, Supplier<CustomChestBlock> block) {
            super(block.get(), new Item.Properties().setId(key));
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}

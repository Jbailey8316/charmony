package svenhjol.charmony.core.common.features.wood.blocks;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import svenhjol.charmony.api.core.FuelProvider;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;

import java.util.function.Supplier;

/** A material-specific chiseled bookshelf retaining vanilla's six-slot behavior. */
public class CustomChiseledBookshelfBlock extends ChiseledBookShelfBlock {
    private final WoodMaterial material;

    public CustomChiseledBookshelfBlock(ResourceKey<Block> key, WoodMaterial material) {
        super(Properties.ofFullCopy(Blocks.CHISELED_BOOKSHELF).setId(key));
        this.material = material;
    }

    public WoodMaterial getMaterial() {
        return material;
    }

    public static class ChiseledBookshelfBlockItem extends BlockItem implements FuelProvider {
        private final WoodMaterial material;

        public ChiseledBookshelfBlockItem(ResourceKey<Item> key, Supplier<CustomChiseledBookshelfBlock> block, WoodMaterial material) {
            super(block.get(), new Properties().setId(key));
            this.material = material;
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}

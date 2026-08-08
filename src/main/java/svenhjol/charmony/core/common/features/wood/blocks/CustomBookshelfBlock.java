package svenhjol.charmony.core.common.features.wood.blocks;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charmony.api.core.FuelProvider;
import svenhjol.charmony.api.core.IgniteProvider;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;

import java.util.function.Supplier;

/** A material-specific bookshelf with vanilla bookshelf behavior. */
public class CustomBookshelfBlock extends Block implements IgniteProvider {
    private final WoodMaterial material;

    public CustomBookshelfBlock(ResourceKey<Block> key, WoodMaterial material) {
        super(Properties.ofFullCopy(Blocks.BOOKSHELF).setId(key));
        this.material = material;
    }

    @Override
    public int igniteChance() {
        return material.isFlammable() ? 30 : 0;
    }

    @Override
    public int burnChance() {
        return material.isFlammable() ? 20 : 0;
    }

    public WoodMaterial getMaterial() {
        return material;
    }

    public static class BookshelfBlockItem extends BlockItem implements FuelProvider {
        private final WoodMaterial material;

        public BookshelfBlockItem(ResourceKey<Item> key, Supplier<CustomBookshelfBlock> block, WoodMaterial material) {
            super(block.get(), new Properties().setId(key));
            this.material = material;
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}

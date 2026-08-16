package svenhjol.charmony.core.common.features.wood.types;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.api.core.FuelProvider;
import svenhjol.charmony.api.core.IgniteProvider;
import svenhjol.charmony.core.common.features.wood.CustomWood;
import svenhjol.charmony.core.common.features.wood.CustomWoodType;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.WoodRegistry;

import java.util.List;
import java.util.function.Supplier;

/** A wood-family Shelf that delegates all behavior to vanilla ShelfBlock. */
public final class Shelf extends CustomWoodType {
    public final Supplier<CustomShelfBlock> block;
    public final Supplier<CustomShelfBlock.ShelfBlockItem> item;

    public Shelf(WoodRegistry registry, WoodMaterial material) {
        super(registry, material);
        var common = registry.commonRegistry();
        var id = material.getSerializedName() + "_shelf";

        block = common.block(id, key -> new CustomShelfBlock(key, material));
        item = common.item(id, key -> new CustomShelfBlock.ShelfBlockItem(key, block, material));

        common.blocksForBlockEntity(() -> BlockEntityType.SHELF, List.of(block));
        common.ignite(block);
        common.fuel(item);
        registry.addItemToCreativeTab(item, material, CustomWood.SHELF);
    }

    /** ShelfBlock with the existing Charmony wood fuel/flammability conventions. */
    public static final class CustomShelfBlock extends ShelfBlock implements IgniteProvider {
        private final WoodMaterial material;

        public CustomShelfBlock(ResourceKey<Block> key, WoodMaterial material) {
            super(Properties.ofFullCopy(Blocks.OAK_SHELF).setId(key));
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

        public static final class ShelfBlockItem extends BlockItem implements FuelProvider {
            private final WoodMaterial material;

            public ShelfBlockItem(ResourceKey<Item> key, Supplier<CustomShelfBlock> block, WoodMaterial material) {
                super(block.get(), new Item.Properties().setId(key));
                this.material = material;
            }

            @Override
            public int fuelTime() {
                return material.fuelTime();
            }
        }
    }
}

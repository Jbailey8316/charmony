package svenhjol.charmony.core.common.features.wood.types;

import svenhjol.charmony.core.common.features.wood.CustomWood;
import svenhjol.charmony.core.common.features.wood.CustomWoodType;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.WoodRegistry;
import svenhjol.charmony.core.common.features.wood.blocks.CustomBookshelfBlock;

import java.util.function.Supplier;

public final class Bookshelf extends CustomWoodType {
    public final Supplier<CustomBookshelfBlock> block;
    public final Supplier<CustomBookshelfBlock.BookshelfBlockItem> item;
    public Bookshelf(WoodRegistry registry, WoodMaterial material) {
        super(registry, material);
        var common = registry.commonRegistry();
        var id = material.getSerializedName() + "_bookshelf";
        block = common.block(id, key -> new CustomBookshelfBlock(key, material));
        item = common.item(id, key -> new CustomBookshelfBlock.BookshelfBlockItem(key, block, material));
        common.ignite(block); common.fuel(item);
        registry.addItemToCreativeTab(item, material, CustomWood.BOOKSHELF);
    }
}

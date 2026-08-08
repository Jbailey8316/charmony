package svenhjol.charmony.core.common.features.wood.types;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.core.common.features.wood.CustomWood;
import svenhjol.charmony.core.common.features.wood.CustomWoodType;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.WoodRegistry;
import svenhjol.charmony.core.common.features.wood.blocks.CustomChiseledBookshelfBlock;

import java.util.List;
import java.util.function.Supplier;

public final class ChiseledBookshelf extends CustomWoodType {
    public final Supplier<CustomChiseledBookshelfBlock> block;
    public final Supplier<CustomChiseledBookshelfBlock.ChiseledBookshelfBlockItem> item;
    public ChiseledBookshelf(WoodRegistry registry, WoodMaterial material) {
        super(registry, material);
        var common = registry.commonRegistry();
        var id = material.getSerializedName() + "_chiseled_bookshelf";
        block = common.block(id, key -> new CustomChiseledBookshelfBlock(key, material));
        item = common.item(id, key -> new CustomChiseledBookshelfBlock.ChiseledBookshelfBlockItem(key, block, material));
        common.fuel(item); common.blocksForBlockEntity(() -> BlockEntityType.CHISELED_BOOKSHELF, List.of(block));
        registry.addItemToCreativeTab(item, material, CustomWood.CHISELED_BOOKSHELF);
    }
}

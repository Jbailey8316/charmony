package svenhjol.charmony.core.common.features.wood.types;

import svenhjol.charmony.core.common.features.wood.CustomWood;
import svenhjol.charmony.core.common.features.wood.CustomWoodType;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.WoodRegistry;
import svenhjol.charmony.core.common.features.wood.blocks.CustomLadderBlock;

import java.util.function.Supplier;

public final class Ladder extends CustomWoodType {
    public final Supplier<CustomLadderBlock> block;
    public final Supplier<CustomLadderBlock.LadderBlockItem> item;

    public Ladder(WoodRegistry registry, WoodMaterial material) {
        super(registry, material);
        var common = registry.commonRegistry();
        var id = material.getSerializedName() + "_ladder";
        block = common.block(id, key -> new CustomLadderBlock(key, material));
        item = common.item(id, key -> new CustomLadderBlock.LadderBlockItem(key, block, material));
        common.fuel(item);
        registry.addItemToCreativeTab(item, material, CustomWood.LADDER);
    }
}

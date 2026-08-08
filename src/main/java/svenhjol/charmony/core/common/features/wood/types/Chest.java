package svenhjol.charmony.core.common.features.wood.types;

import svenhjol.charmony.core.common.features.wood.CustomWood;
import svenhjol.charmony.core.common.features.wood.CustomWoodType;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.WoodRegistry;
import svenhjol.charmony.core.common.features.wood.blocks.CustomChestBlock;

import java.util.function.Supplier;

public class Chest extends CustomWoodType {
    public final Supplier<CustomChestBlock> block;
    public final Supplier<CustomChestBlock.ChestBlockItem> item;

    public Chest(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);
        var id = material.getSerializedName() + "_chest";

        block = woodRegistry.commonRegistry().block(id, key -> new CustomChestBlock(key, material));
        item = woodRegistry.commonRegistry().item(id, key -> new CustomChestBlock.ChestBlockItem(key, block));
        woodRegistry.commonRegistry().fuel(item);
        woodRegistry.commonRegistry().blocksForBlockEntity(WoodRegistry::chestBlockEntity, java.util.List.of(block));
        woodRegistry.addItemToCreativeTab(item, material, CustomWood.CHEST);
        WoodRegistry.CHESTS.add(() -> this);
    }
}

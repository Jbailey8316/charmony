package svenhjol.charmony.core.common.features.wood.types;

import svenhjol.charmony.core.common.features.wood.CustomWood;
import svenhjol.charmony.core.common.features.wood.CustomWoodType;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.WoodRegistry;
import svenhjol.charmony.core.common.features.wood.blocks.CustomTrappedChestBlock;

import java.util.function.Supplier;

public class TrappedChest extends CustomWoodType {
    public final Supplier<CustomTrappedChestBlock> block;
    public final Supplier<CustomTrappedChestBlock.TrappedChestBlockItem> item;

    public TrappedChest(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);
        var id = "trapped_" + material.getSerializedName() + "_chest";

        block = woodRegistry.commonRegistry().block(id, key -> new CustomTrappedChestBlock(key, material));
        item = woodRegistry.commonRegistry().item(id, key -> new CustomTrappedChestBlock.TrappedChestBlockItem(key, block));
        woodRegistry.commonRegistry().fuel(item);
        woodRegistry.commonRegistry().blocksForBlockEntity(WoodRegistry::trappedChestBlockEntity, java.util.List.of(block));
        woodRegistry.addItemToCreativeTab(item, material, CustomWood.CHEST);
        WoodRegistry.TRAPPED_CHESTS.add(() -> this);
    }
}

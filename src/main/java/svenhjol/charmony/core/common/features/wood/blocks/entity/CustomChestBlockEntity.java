package svenhjol.charmony.core.common.features.wood.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.blocks.CustomChestBlock;
import svenhjol.charmony.core.common.features.wood.blocks.CustomTrappedChestBlock;

import javax.annotation.Nullable;

public class CustomChestBlockEntity extends ChestBlockEntity {
    public CustomChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nullable
    public WoodMaterial getMaterial() {
        if (getBlockState().getBlock() instanceof CustomChestBlock chest) {
            return chest.getMaterial();
        }
        if (getBlockState().getBlock() instanceof CustomTrappedChestBlock chest) {
            return chest.getMaterial();
        }
        return null;
    }
}

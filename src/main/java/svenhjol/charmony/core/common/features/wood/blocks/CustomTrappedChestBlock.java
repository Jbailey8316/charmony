package svenhjol.charmony.core.common.features.wood.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.api.core.FuelProvider;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.WoodRegistry;
import svenhjol.charmony.core.common.features.wood.blocks.entity.CustomTrappedChestBlockEntity;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CustomTrappedChestBlock extends ChestBlock {
    private final WoodMaterial material;

    public CustomTrappedChestBlock(ResourceKey<Block> key, WoodMaterial material) {
        super(WoodRegistry::trappedChestBlockEntity, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE,
            Properties.ofFullCopy(Blocks.TRAPPED_CHEST).setId(key));
        this.material = material;
    }

    public WoodMaterial getMaterial() {
        return material;
    }

    @Override
    public boolean chestCanConnectTo(BlockState other) {
        return other.getBlock() instanceof CustomTrappedChestBlock chest
            && chest.material == material;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CustomTrappedChestBlockEntity(WoodRegistry.trappedChestBlockEntity(), pos, state);
    }

    @Override
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return Mth.clamp(CustomTrappedChestBlockEntity.getOpenCount(level, pos), 0, 15);
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return direction == Direction.UP ? state.getSignal(level, pos, direction) : 0;
    }

    public static class TrappedChestBlockItem extends BlockItem implements FuelProvider {
        private final WoodMaterial material;

        public TrappedChestBlockItem(ResourceKey<Item> key, Supplier<CustomTrappedChestBlock> block) {
            super(block.get(), new Item.Properties().setId(key));
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}

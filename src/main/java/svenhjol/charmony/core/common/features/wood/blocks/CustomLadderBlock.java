package svenhjol.charmony.core.common.features.wood.blocks;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import svenhjol.charmony.api.core.FuelProvider;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;

import java.util.function.Supplier;

/** A material-specific ladder retaining vanilla placement, climbing and waterlogging. */
public class CustomLadderBlock extends LadderBlock {
    private final WoodMaterial material;

    public CustomLadderBlock(ResourceKey<Block> key, WoodMaterial material) {
        super(Properties.ofFullCopy(Blocks.LADDER).setId(key));
        this.material = material;
    }

    public WoodMaterial getMaterial() {
        return material;
    }

    public static class LadderBlockItem extends BlockItem implements FuelProvider {
        private final WoodMaterial material;

        public LadderBlockItem(ResourceKey<Item> key, Supplier<CustomLadderBlock> block, WoodMaterial material) {
            super(block.get(), new Properties().setId(key));
            this.material = material;
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}

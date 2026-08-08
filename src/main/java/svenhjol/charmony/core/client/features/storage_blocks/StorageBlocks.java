package svenhjol.charmony.core.client.features.storage_blocks;

import net.minecraft.world.item.CreativeModeTabs;
import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;

@FeatureDefinition(side = Side.Client, description = "Creative inventory entries for storage blocks.")
public final class StorageBlocks extends SidedFeature {
    public StorageBlocks(Mod mod) { super(mod); }

    @Override
    public void run() {
        var common = Mod.getSidedFeature(svenhjol.charmony.core.common.features.storage_blocks.StorageBlocks.class);
        var registry = svenhjol.charmony.core.client.ClientRegistry.forFeature(this);
        registry.itemTab(common.registers.enderPearlItem.get(), CreativeModeTabs.FUNCTIONAL_BLOCKS, null);
        registry.itemTab(common.registers.gunpowderItem.get(), CreativeModeTabs.FUNCTIONAL_BLOCKS, common.registers.enderPearlItem.get());
        registry.itemTab(common.registers.sugarItem.get(), CreativeModeTabs.FUNCTIONAL_BLOCKS, common.registers.gunpowderItem.get());
    }
}

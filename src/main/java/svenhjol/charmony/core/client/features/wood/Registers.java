package svenhjol.charmony.core.client.features.wood;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.client.ClientRegistry;
import svenhjol.charmony.core.common.features.wood.CustomWood;
import svenhjol.charmony.core.common.features.wood.WoodRegistry;
import svenhjol.charmony.core.common.features.wood.blocks.entity.CustomTrappedChestBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class Registers extends Setup<Wood> {
    public Registers(Wood feature) {
        super(feature);
    }

    @Override
    public Runnable boot() {
        var clientRegistry = ClientRegistry.forFeature(feature());

        return () -> {
            if (!WoodRegistry.CHESTS.isEmpty()) {
                clientRegistry.blockEntityRenderer(WoodRegistry.chestBlockEntity(), CustomChestRenderer::new);
                for (var chest : WoodRegistry.CHESTS) {
                    var type = chest.get();
                    var material = type.material();
                    var feature = type.feature();
                    var name = material.getSerializedName();
                    ChestMaterials.add(material, ChestType.SINGLE, feature.registryId("entity/chest/" + name + "_normal"));
                    ChestMaterials.add(material, ChestType.LEFT, feature.registryId("entity/chest/" + name + "_normal_left"));
                    ChestMaterials.add(material, ChestType.RIGHT, feature.registryId("entity/chest/" + name + "_normal_right"));
                }
            }
            if (!WoodRegistry.TRAPPED_CHESTS.isEmpty()) {
            clientRegistry.blockEntityRenderer(WoodRegistry.trappedChestBlockEntity(),
                CustomChestRenderer<CustomTrappedChestBlockEntity>::new);
            for (var chest : WoodRegistry.TRAPPED_CHESTS) {
                var type = chest.get();
                var material = type.material();
                var name = material.getSerializedName();
                var chestFeature = type.feature();
                ChestMaterials.addTrapped(material, ChestType.SINGLE,
                    chestFeature.registryId("entity/chest/" + name + "_trapped"));
                ChestMaterials.addTrapped(material, ChestType.LEFT,
                    chestFeature.registryId("entity/chest/" + name + "_trapped_left"));
                ChestMaterials.addTrapped(material, ChestType.RIGHT,
                    chestFeature.registryId("entity/chest/" + name + "_trapped_right"));
                }
            }

            // Register models for custom boats.
            for (var boat : WoodRegistry.BOATS) {
                var materialName = boat.get().material().getSerializedName();
                var feature = boat.get().feature();

                var boatEntity = boat.get().boat.get();
                var chestBoatEntity = boat.get().chestBoat.get();

                var boatLayer = new ModelLayerLocation(feature.registryId("boat/" + materialName), "main");
                var chestBoatLayer = new ModelLayerLocation(feature.registryId("chest_boat/" + materialName), "main");

                clientRegistry.modelLayer(boatLayer, BoatModel::createBoatModel);
                clientRegistry.modelLayer(chestBoatLayer, BoatModel::createChestBoatModel);

                clientRegistry.entityRenderer(boatEntity, context -> new BoatRenderer(context, boatLayer));
                clientRegistry.entityRenderer(chestBoatEntity, context -> new BoatRenderer(context, chestBoatLayer));
            }

            // Custom ladders use the same cutout render layer as vanilla ladders.
            for (var ladder : WoodRegistry.LADDERS) {
                clientRegistry.blockRenderType(ladder.get().block.get(), ChunkSectionLayer.CUTOUT);
            }

            // Register the woodtype for custom signs.
            for (var sign : WoodRegistry.SIGNS) {
                var feature = sign.get().feature();
                var woodType = sign.get().material().woodType();

                Sheets.SIGN_MATERIALS.put(woodType, new Material(Sheets.SIGN_SHEET, feature.registryId("entity/signs/" + woodType.name())));
                Sheets.HANGING_SIGN_MATERIALS.put(woodType, new Material(Sheets.SIGN_SHEET, feature.registryId("entity/signs/hanging/" + woodType.name())));
            }

            // Add all custom items to creative tabs.
            var table = WoodRegistry.ITEM_CREATIVE_TABS;
            var map = table.rowMap();

            for (var item : map.keySet()) {
                for (var entry : map.get(item).entrySet()) {
                    var after = entry.getKey();
                    var customType = entry.getValue();
                    List<ResourceKey<CreativeModeTab>> tabs = new ArrayList<>();

                    if (CustomWood.BUILDING_BLOCKS.contains(customType)) {
                        tabs.add(CreativeModeTabs.BUILDING_BLOCKS);
                    }
                    if (CustomWood.FUNCTIONAL_BLOCKS.contains(customType)) {
                        tabs.add(CreativeModeTabs.FUNCTIONAL_BLOCKS);
                    }
                    if (CustomWood.NATURAL_BLOCKS.contains(customType)) {
                        tabs.add(CreativeModeTabs.NATURAL_BLOCKS);
                    }
                    if (CustomWood.TOOLS_AND_UTILITIES.contains(customType)) {
                        tabs.add(CreativeModeTabs.TOOLS_AND_UTILITIES);
                    }

                    for (var tab : tabs) {
                        clientRegistry.itemTab(item.get(), tab, after.get());
                    }
                }
            }
        };
    }
}

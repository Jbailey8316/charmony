package svenhjol.charmony.core.client.features.wood;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.blocks.entity.CustomChestBlockEntity;
import svenhjol.charmony.core.common.features.wood.blocks.entity.CustomTrappedChestBlockEntity;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class ChestMaterials {
    private static final Map<WoodMaterial, Map<ChestType, Material>> MATERIALS = new HashMap<>();
    private static final Map<WoodMaterial, Map<ChestType, Material>> TRAPPED_MATERIALS = new HashMap<>();

    private ChestMaterials() {}

    public static void add(WoodMaterial material, ChestType type, ResourceLocation texture) {
        add(MATERIALS, material, type, texture);
    }

    public static void addTrapped(WoodMaterial material, ChestType type, ResourceLocation texture) {
        add(TRAPPED_MATERIALS, material, type, texture);
    }

    private static void add(Map<WoodMaterial, Map<ChestType, Material>> target, WoodMaterial material,
                            ChestType type, ResourceLocation texture) {
        target.computeIfAbsent(material, ignored -> new EnumMap<>(ChestType.class))
            .put(type, new Material(Sheets.CHEST_SHEET, texture));
    }

    public static Material get(BlockEntity blockEntity, ChestType type) {
        if (!(blockEntity instanceof CustomChestBlockEntity chest)) return null;
        var material = chest.getMaterial();
        var textures = blockEntity instanceof CustomTrappedChestBlockEntity
            ? TRAPPED_MATERIALS.get(material) : MATERIALS.get(material);
        return textures == null ? null : textures.get(type);
    }
}

package svenhjol.charmony.core.client.features.wood;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.blocks.entity.CustomChestBlockEntity;
import svenhjol.charmony.core.common.features.wood.blocks.entity.CustomTrappedChestBlockEntity;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class ChestMaterials {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<String, Map<ChestType, Material>> MATERIALS = new HashMap<>();
    private static final Map<String, Map<ChestType, Material>> TRAPPED_MATERIALS = new HashMap<>();

    private ChestMaterials() {}

    public static void add(WoodMaterial material, ChestType type, ResourceLocation texture) {
        add(MATERIALS, material, type, texture);
    }

    public static void addTrapped(WoodMaterial material, ChestType type, ResourceLocation texture) {
        add(TRAPPED_MATERIALS, material, type, texture);
    }

    private static void add(Map<String, Map<ChestType, Material>> target, WoodMaterial material,
                            ChestType type, ResourceLocation texture) {
        var name = material.getSerializedName();
        target.computeIfAbsent(name, ignored -> new EnumMap<>(ChestType.class))
            .put(type, new Material(Sheets.CHEST_SHEET, texture));
        LOGGER.info("[Charm Q10.1] chest material registered wood={} type={} texture={}", name, type, texture);
    }

    public static Material get(BlockEntity blockEntity, ChestType type) {
        if (!(blockEntity instanceof CustomChestBlockEntity chest)) return null;
        var material = chest.getMaterial();
        var name = material == null ? null : material.getSerializedName();
        var textures = blockEntity instanceof CustomTrappedChestBlockEntity
            ? TRAPPED_MATERIALS.get(name) : MATERIALS.get(name);
        var result = textures == null ? null : textures.get(type);
        LOGGER.info("[Charm Q10.1] chest material lookup block={} wood={} type={} resolved={}",
            blockEntity.getBlockState().getBlock(), name, type, result == null ? "<null>" : materialTexture(result));
        return result;
    }

    private static String materialTexture(Material material) {
        try {
            return String.valueOf(Material.class.getMethod("texture").invoke(material));
        } catch (ReflectiveOperationException e) {
            return "<texture-api-unavailable>";
        }
    }
}

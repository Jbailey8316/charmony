package svenhjol.charmony.core.client.features.wood;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ChestModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charmony.core.common.features.wood.blocks.entity.CustomChestBlockEntity;

public final class CustomChestRenderer implements BlockEntityRenderer<CustomChestBlockEntity, CustomChestRenderState> {
    private final MaterialSet materials;
    private final ChestModel singleModel;
    private final ChestModel doubleLeftModel;
    private final ChestModel doubleRightModel;
    private final ChestRenderer<CustomChestBlockEntity> vanillaStateExtractor;

    public CustomChestRenderer(BlockEntityRendererProvider.Context context) {
        materials = context.materials();
        singleModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
        doubleLeftModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT));
        doubleRightModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT));
        vanillaStateExtractor = new ChestRenderer<>(context);
    }

    @Override
    public CustomChestRenderState createRenderState() {
        return new CustomChestRenderState();
    }

    @Override
    public void extractRenderState(CustomChestBlockEntity blockEntity, CustomChestRenderState state, float partialTick,
                                   net.minecraft.world.phys.Vec3 cameraPosition,
                                   ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        vanillaStateExtractor.extractRenderState(blockEntity, state, partialTick, cameraPosition, breakProgress);
        state.chestMaterial = ChestMaterials.get(blockEntity, state.type);
    }

    @Override
    public void submit(CustomChestRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState camera) {
        if (state.chestMaterial == null) return;

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.angle));
        poseStack.translate(-0.5f, -0.5f, -0.5f);

        float open = 1.0f - state.open;
        open = 1.0f - open * open * open;
        var renderType = state.chestMaterial.renderType(RenderType::entityCutout);
        var sprite = materials.get(state.chestMaterial);
        var model = state.type == ChestType.LEFT ? doubleLeftModel
            : state.type == ChestType.RIGHT ? doubleRightModel : singleModel;

        collector.submitModel(model, open, poseStack, renderType, state.lightCoords,
            OverlayTexture.NO_OVERLAY, -1, sprite, 0, state.breakProgress);
        poseStack.popPose();
    }
}

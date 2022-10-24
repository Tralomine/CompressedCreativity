package com.lgmrszd.compressedcreativity.blocks.advanced_air_blower;

import com.jozufozu.flywheel.core.PartialModel;
import com.lgmrszd.compressedcreativity.content.Mesh;
import com.lgmrszd.compressedcreativity.index.BlockPartials;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import com.simibubi.create.foundation.utility.AngleHelper;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.common.heat.HeatUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class AdvancedAirBlowerRenderer extends SafeTileEntityRenderer<AdvancedAirBlowerTileEntity> {
    public AdvancedAirBlowerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(AdvancedAirBlowerTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        // TODO: fix Flywheel Instance
//        if (Backend.canUseInstancing(te.getLevel())) return;

        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        BlockState blockState = te.getBlockState();
        Direction facing = blockState.getValue(AdvancedAirBlowerBlock.FACING);

        Mesh.MeshType meshType = te.getMeshType();
        if (meshType == null) return;
        Optional<PartialModel> meshPartial = meshType.getModel();
        meshPartial.ifPresent((meshModel) -> {
            SuperByteBuffer mesh = CachedBufferer.partialFacing(meshModel, blockState);
            rotateToFacing(mesh, facing);
            if (meshType.shouldTint()) {
                te.getCapability(PNCCapabilities.HEAT_EXCHANGER_CAPABILITY).ifPresent((cap) -> {
//                    mesh.color(HeatUtil.getColourForTemperature(cap.getTemperatureAsInt()).getRGB());
                    mesh.color(meshType.getTintColor(cap.getTemperatureAsInt()));
                });
            }
            mesh.light(light);
            mesh.renderInto(ms, vb);
        });
    }

    private static void rotateToFacing(SuperByteBuffer buffer, Direction facing) {
        buffer.centre()
                .rotateY(AngleHelper.horizontalAngle(facing))
                .rotateX(90)
                .unCentre();
    }
}

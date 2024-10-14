package me.Thelnfamous1.joyofpaintingextras.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.Thelnfamous1.joyofpaintingextras.CustomCanvasType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xerca.xercapaint.client.EaselCanvasLayer;
import xerca.xercapaint.common.entity.EntityEasel;
import xerca.xercapaint.common.item.ItemCanvas;

@Mixin(EaselCanvasLayer.class)
public class EaselCanvasLayerMixin {

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILxerca/xercapaint/common/entity/EntityEasel;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockEntityWithoutLevelRenderer;renderByItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"))
    private void pre_renderToItem_render(PoseStack poseStack, MultiBufferSource bufferSource, int i, EntityEasel entity, float v, float v1, float v2, float v3, float v4, float v5, CallbackInfo ci, @Local(ordinal = 0) ItemStack stack, @Local(ordinal = 0) ItemCanvas itemCanvas){
        if(itemCanvas.getCanvasType() == CustomCanvasType.CUSTOM){
            int customWidth = CustomCanvasType.getCustomWidth(stack);
            int customHeight = CustomCanvasType.getCustomHeight(stack);
            boolean small;
            float poseStackScale;
            if ((customWidth / 16.0F) <= 1.0F && (customHeight / 16.0F) <= 1.0F) {
                poseStackScale = 2.0F;
                small = true;
            } else {
                poseStackScale = 3.3F;
                small = false;
            }
            poseStack.scale(poseStackScale, poseStackScale, poseStackScale);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            poseStack.mulPose(Vector3f.XP.rotationDegrees(-15.0F));
            float xShift = -0.75F + ((0.03125F / poseStackScale) * (customWidth));
            poseStack.translate(xShift, small ? -1.0F : -0.8125F, -0.5F);
        }
    }
}

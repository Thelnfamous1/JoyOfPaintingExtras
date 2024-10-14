package me.Thelnfamous1.joyofpaintingextras.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import me.Thelnfamous1.joyofpaintingextras.CustomCanvasType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xerca.xercapaint.client.CanvasItemRenderer;

@Mixin(CanvasItemRenderer.class)
public class CanvasItemRendererMixin {

    @ModifyVariable(method = "renderByItem", at = @At(value = "STORE", ordinal = 0))
    private CompoundTag modify_getTag_renderByItem(CompoundTag original, ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay){
        if(CustomCanvasType.isUnmodifiedCustomCanvas(stack)){
            return null;
        }
        return original;
    }

    @ModifyExpressionValue(method = "renderByItem", at = @At(value = "INVOKE", target = "Lxerca/xercapaint/common/item/ItemCanvas;getWidth()I", remap = false))
    private int modify_getWidth_renderByItem(int original, ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay){
        if(CustomCanvasType.isCustomCanvas(stack)){
            return CustomCanvasType.getCustomWidth(stack);
        }
        return original;
    }

    @ModifyExpressionValue(method = "renderByItem", at = @At(value = "INVOKE", target = "Lxerca/xercapaint/common/item/ItemCanvas;getHeight()I", remap = false))
    private int modify_getHeight_renderByItem(int original, ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay){
        if(CustomCanvasType.isCustomCanvas(stack)){
            return CustomCanvasType.getCustomHeight(stack);
        }
        return original;
    }
}

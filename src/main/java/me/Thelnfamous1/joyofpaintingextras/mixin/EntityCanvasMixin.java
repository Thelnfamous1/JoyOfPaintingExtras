package me.Thelnfamous1.joyofpaintingextras.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.Thelnfamous1.joyofpaintingextras.CustomCanvasType;
import me.Thelnfamous1.joyofpaintingextras.JOPExtrasMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xerca.xercapaint.common.CanvasType;
import xerca.xercapaint.common.entity.EntityCanvas;
import xerca.xercapaint.common.item.ItemCanvas;

@Mixin(EntityCanvas.class)
public class EntityCanvasMixin {

    @Shadow(remap = false) private CanvasType canvasType;
    @Unique
    private int joyofpaintingextras$customWidth;
    @Unique
    private int joyofpaintingextras$customHeight;

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lxerca/xercapaint/common/CanvasType;I)V", at = @At("TAIL"))
    private void post_init(Level world, CompoundTag canvasNBT, BlockPos pos, Direction facing, CanvasType canvasType, int rotation, CallbackInfo ci){
        this.joyofpaintingextras$readCustomDimensions(canvasNBT);
    }

    @Unique
    private void joyofpaintingextras$readCustomDimensions(CompoundTag canvasNBT) {
        if(canvasNBT.contains(CustomCanvasType.CANVAS_WIDTH_TAG_KEY)){
            this.joyofpaintingextras$customWidth = canvasNBT.getInt(CustomCanvasType.CANVAS_WIDTH_TAG_KEY);
        }
        if(canvasNBT.contains(CustomCanvasType.CANVAS_HEIGHT_TAG_KEY)){
            this.joyofpaintingextras$customHeight = canvasNBT.getInt(CustomCanvasType.CANVAS_HEIGHT_TAG_KEY);
        }
    }

    @Inject(method = "getWidth", at = @At("HEAD"), cancellable = true)
    private void pre_getWidth(CallbackInfoReturnable<Integer> cir){
        if(this.canvasType == CustomCanvasType.CUSTOM){
            cir.setReturnValue(this.joyofpaintingextras$customWidth);
        }
    }

    @Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
    private void pre_getHeight(CallbackInfoReturnable<Integer> cir){
        if(this.canvasType == CustomCanvasType.CUSTOM){
            cir.setReturnValue(this.joyofpaintingextras$customHeight);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
    private void post_getCompound_readAdditionalSaveData(CompoundTag tagCompound, CallbackInfo ci, @Local(ordinal = 1) CompoundTag canvasNBT){
        this.joyofpaintingextras$readCustomDimensions(canvasNBT);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void post_addAdditionalSaveData(CompoundTag tagCompound, CallbackInfo ci){
        this.joyofpaintingextras$writeCustomDimensions(tagCompound);
    }

    @Unique
    private void joyofpaintingextras$writeCustomDimensions(CompoundTag tagCompound) {
        tagCompound.putInt(CustomCanvasType.CANVAS_WIDTH_TAG_KEY, this.joyofpaintingextras$customWidth);
        tagCompound.putInt(CustomCanvasType.CANVAS_HEIGHT_TAG_KEY, this.joyofpaintingextras$customHeight);
    }

    @ModifyExpressionValue(method = "dropItem", remap = false, at = @At(value = "FIELD", target = "Lxerca/xercapaint/common/CanvasType;SMALL:Lxerca/xercapaint/common/CanvasType;", remap = false))
    private CanvasType modify_smallCanvasType_dropItem(CanvasType original){
        if(this.canvasType == CustomCanvasType.CUSTOM){
            return CustomCanvasType.CUSTOM;
        }
        return original;
    }

    @ModifyExpressionValue(method = "dropItem", remap = false, at = @At(value = "FIELD", target = "Lxerca/xercapaint/common/item/Items;ITEM_CANVAS:Lnet/minecraftforge/registries/RegistryObject;", remap = false))
    private RegistryObject<ItemCanvas> modify_itemCanvas_dropItem(RegistryObject<ItemCanvas> original){
        if(this.canvasType == CustomCanvasType.CUSTOM){
            return JOPExtrasMod.CUSTOM_CANVAS;
        }
        return original;
    }

    @Inject(method = "dropItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setTag(Lnet/minecraft/nbt/CompoundTag;)V"))
    private void post_getCompound_readAdditionalSaveData(Entity brokenEntity, CallbackInfo ci, @Local(ordinal = 0) CompoundTag nbt){
        this.joyofpaintingextras$writeCustomDimensions(nbt);
    }

    @Inject(method = "writeSpawnData", remap = false, at = @At("TAIL"))
    private void post_writeSpawnData(FriendlyByteBuf buffer, CallbackInfo ci){
        if(this.canvasType == CustomCanvasType.CUSTOM){
            buffer.writeInt(this.joyofpaintingextras$customWidth);
            buffer.writeInt(this.joyofpaintingextras$customHeight);
        }
    }

    @Inject(method = "readSpawnData", remap = false, at = @At("TAIL"))
    private void post_readSpawnData(FriendlyByteBuf buffer, CallbackInfo ci){
        if(this.canvasType == CustomCanvasType.CUSTOM){
            this.joyofpaintingextras$customWidth = buffer.readInt();
            this.joyofpaintingextras$customHeight = buffer.readInt();
        }
    }
}

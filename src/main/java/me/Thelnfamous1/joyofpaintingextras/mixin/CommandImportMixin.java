package me.Thelnfamous1.joyofpaintingextras.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.Thelnfamous1.joyofpaintingextras.CustomCanvasType;
import me.Thelnfamous1.joyofpaintingextras.JOPExtrasMod;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xerca.xercapaint.common.CanvasType;
import xerca.xercapaint.common.CommandImport;
import xerca.xercapaint.common.item.ItemCanvas;

@Mixin(value = CommandImport.class, remap = false)
public class CommandImportMixin {

    @ModifyExpressionValue(method = "doImport", at = @At(value = "INVOKE", target = "Lxerca/xercapaint/common/CanvasType;fromByte(B)Lxerca/xercapaint/common/CanvasType;"))
    private static CanvasType modify_fromByte_doImport(CanvasType original, @Local(ordinal = 0) byte canvasType){
        if(canvasType == CustomCanvasType.CUSTOM.ordinal()){
            return CanvasType.SMALL;
        }
        return original;
    }

    @ModifyExpressionValue(method = "doImport", at = @At(value = "FIELD", target = "Lxerca/xercapaint/common/item/Items;ITEM_CANVAS:Lnet/minecraftforge/registries/RegistryObject;"))
    private static RegistryObject<ItemCanvas> modify_fromByte_doImport(RegistryObject<ItemCanvas> original, @Local(ordinal = 0) byte canvasType){
        if(canvasType == CustomCanvasType.CUSTOM.ordinal()){
            return JOPExtrasMod.CUSTOM_CANVAS;
        }
        return original;
    }
}

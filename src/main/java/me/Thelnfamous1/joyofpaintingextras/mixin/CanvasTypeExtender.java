package me.Thelnfamous1.joyofpaintingextras.mixin;

import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import me.Thelnfamous1.joyofpaintingextras.CustomCanvasType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xerca.xercapaint.common.CanvasType;

@Mixin(value = CanvasType.class, remap = false)
public class CanvasTypeExtender {
    @Shadow
    @Final
    private static CanvasType[] $VALUES;

    @Invoker(value="<init>")
    private static CanvasType create(String name, int ordinal) {
        throw new IllegalStateException("Unreachable");
    }

    static {
        var entry = create(CustomCanvasType.CUSTOM_CANVAS_TYPE_NAME, $VALUES.length); // subsequent additions would be length+1, +2, ...

        //noinspection ShadowFinalModification
        $VALUES = ArrayUtils.add($VALUES, entry); // use addAll if you have more than one entry to be added, and make sure they are precisely in the order you defined the ordinals, so length before length+1, before length+2, ...
    }

    @Inject(method = "fromByte", at = @At("HEAD"), cancellable = true)
    private static void pre_fromByte(byte x, CallbackInfoReturnable<CanvasType> cir){
        if(x == CustomCanvasType.CUSTOM.ordinal()){
            cir.setReturnValue(CustomCanvasType.CUSTOM);
        }
    }

    @Inject(method = "getWidth", at = @At("HEAD"), cancellable = true)
    private static void pre_getWidth(CanvasType canvasType, CallbackInfoReturnable<Integer> cir){
        if(canvasType == CustomCanvasType.CUSTOM){
            cir.setReturnValue(1);
        }
    }

    @Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
    private static void pre_getHeight(CanvasType canvasType, CallbackInfoReturnable<Integer> cir){
        if(canvasType == CustomCanvasType.CUSTOM){
            cir.setReturnValue(1);
        }
    }
}
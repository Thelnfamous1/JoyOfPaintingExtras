package me.Thelnfamous1.joyofpaintingextras.mixin;

import me.Thelnfamous1.joyofpaintingextras.CustomCanvasType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xerca.xercapaint.client.GuiCanvasView;
import xerca.xercapaint.common.CanvasType;
import xerca.xercapaint.common.entity.EntityEasel;

import java.util.Arrays;

@Mixin(GuiCanvasView.class)
public class GuiCanvasViewMixin {

    @Shadow(remap = false) @Final private CanvasType canvasType;

    @Mutable
    @Shadow(remap = false) @Final private int canvasPixelWidth;

    @Mutable
    @Shadow(remap = false) @Final private int canvasPixelHeight;

    @Mutable
    @Shadow(remap = false) @Final private int canvasWidth;

    @Mutable
    @Shadow(remap = false) @Final private int canvasPixelScale;

    @Shadow(remap = false) private int[] pixels;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void modifyCanvasDimensions_init(CompoundTag canvasTag, Component title, CanvasType canvasType, EntityEasel easel, CallbackInfo ci){
        if(this.canvasType == CustomCanvasType.CUSTOM){
            this.canvasPixelWidth = CustomCanvasType.getCustomWidth(canvasTag);
            this.canvasPixelHeight = CustomCanvasType.getCustomHeight(canvasTag);
            this.canvasPixelScale = CustomCanvasType.getPixelScale(this.canvasPixelWidth, this.canvasPixelHeight);
            int canvasPixelArea = this.canvasPixelHeight * this.canvasPixelWidth;
            this.canvasWidth = this.canvasPixelWidth * this.canvasPixelScale;
            if (canvasTag != null && !canvasTag.isEmpty()) {
                if(canvasTag.contains("pixels")){
                    int[] nbtPixels = canvasTag.getIntArray("pixels");
                    this.pixels = Arrays.copyOfRange(nbtPixels, 0, canvasPixelArea);
                } else{
                    this.pixels = null;
                }
            }
        }
    }
}

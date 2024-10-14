package me.Thelnfamous1.joyofpaintingextras.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import xerca.xercapaint.common.CanvasType;
import xerca.xercapaint.common.item.ItemCanvas;

@Mixin(ItemCanvas.class)
public interface ItemCanvasAccessor {

    @Invoker("<init>")
    static ItemCanvas joyofpaintingextras$new(CanvasType canvasType){
        throw new AssertionError("ItemCanvasAccessor not applied!");
    }
}

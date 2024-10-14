package me.Thelnfamous1.joyofpaintingextras.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import xerca.xercapaint.client.BasePalette;
import xerca.xercapaint.common.PaletteUtil;

@Mixin(BasePalette.class)
public interface BasePaletteAccessor {

    @Accessor(value = "basicColors", remap = false)
    static PaletteUtil.Color[] joyofpaintingextras$getbasicColors(){
        throw new AssertionError("BasePaletteAccessor not applied!");
    }
}

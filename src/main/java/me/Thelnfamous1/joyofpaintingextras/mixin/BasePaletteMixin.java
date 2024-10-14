package me.Thelnfamous1.joyofpaintingextras.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import xerca.xercapaint.client.BasePalette;
import xerca.xercapaint.common.CanvasType;

import java.util.Arrays;

@Mixin(BasePalette.class)
public class BasePaletteMixin {


    @Mutable
    @Shadow(remap = false) @Final
    static double[] paletteXs;

    @Mutable
    @Shadow(remap = false) @Final
    static double[] paletteYs;

    static {
        int values = CanvasType.values().length;
        assert values > 4;
        paletteXs = new double[values]; // Added 1 for the CUSTOM CanvasType
        Arrays.fill(paletteXs, -1000.0D);
        paletteYs = new double[values]; // Added 1 for the CUSTOM CanvasType
        Arrays.fill(paletteYs, -1000.0D);
    }
}

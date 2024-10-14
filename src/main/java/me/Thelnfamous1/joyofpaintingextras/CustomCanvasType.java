package me.Thelnfamous1.joyofpaintingextras;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import xerca.xercapaint.common.CanvasType;
import xerca.xercapaint.common.item.ItemCanvas;

public class CustomCanvasType {
    public static final String CUSTOM_CANVAS_TYPE_NAME = "JOYOFPAINTINGEXTRAS_CUSTOM";
    public static final CanvasType CUSTOM = Enum.valueOf(CanvasType.class, CUSTOM_CANVAS_TYPE_NAME);
    public static final String CANVAS_WIDTH_TAG_KEY = "CanvasWidth";
    public static final String CANVAS_HEIGHT_TAG_KEY = "CanvasHeight";

    public static boolean isCustomCanvas(ItemStack stack){
        return stack.getItem() instanceof ItemCanvas itemCanvas && itemCanvas.getCanvasType() == CUSTOM;
    }

    public static boolean isUnmodifiedCustomCanvas(ItemStack stack){
        return isCustomCanvas(stack) && (stack.getTag() == null || !stack.getTag().contains("pixels"));
    }

    public static int getCustomWidth(ItemStack stack){
        CompoundTag tag = stack.getTag();
        return getCustomWidth(tag);
    }

    public static int getCustomWidth(@Nullable CompoundTag tag) {
        if(tag != null && tag.contains(CANVAS_WIDTH_TAG_KEY)){
            return tag.getInt(CANVAS_WIDTH_TAG_KEY);
        }
        return 1;
    }

    public static int getCustomHeight(ItemStack stack){
        CompoundTag tag = stack.getTag();
        return getCustomHeight(tag);
    }

    public static int getCustomHeight(@Nullable CompoundTag tag) {
        if(tag != null && tag.contains(CANVAS_HEIGHT_TAG_KEY)){
            return tag.getInt(CANVAS_HEIGHT_TAG_KEY);
        }
        return 1;
    }

    public static void setCustomWidth(ItemStack stack, int width){
        stack.getOrCreateTag().putInt(CANVAS_WIDTH_TAG_KEY, width);
    }

    public static void setCustomHeight(ItemStack stack, int height){
        stack.getOrCreateTag().putInt(CANVAS_HEIGHT_TAG_KEY, height);
    }

    public static int getPixelScale(int width, int height) {
        int maxDimension = Math.max(width, height);
        /*
            1-16: 10 / 1
            17-32: 10 / 2
            33-48: 10 / 3
            49-64: 10 / 4
            ...
            241-256: 10 / 10
            257+: 10 / 10
         */
        return Math.max(1, 10 / Math.max(1, Mth.ceil(maxDimension / 16.0F)));
    }

    public static float getPoseStackScale(int width, int height) {
        int maxDimension = Math.max(width, height);
        /*
            16: 1.0 + 0.5 * 1
            32: 1.0 + 0.5 * 2
            48: 1.0 + 0.5 * 3
            64: 1.0 + 0.5 * 4
            ...
            256: 1.0 + 0.5 * 10
            ...
         */
        return Math.max(1.0F, 1.0F + (0.5F * maxDimension / 16.0F));
    }
}